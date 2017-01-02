/*
 * Copyright (C) 2016 Scot P. Floess
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bak;

import org.solenopsis.keraiai.soap.port.WebServiceTypeEnum;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.LoggerUtils;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;
import org.flossware.jcore.utils.soap.ServiceUtils;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.SecurityMgr;

/**
 * SOAP utility class.
 *
 * @author Scot P. Floess
 */
final class SoapUtils {

    /**
     * Our logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SoapUtils.class.getName());

    /**
     * When setting up the soap header, we need to set the session header using this attribute.
     */
    static final String SESSION_HEADER = "SessionHeader";

    /**
     * This is the session id on the session header.
     */
    static final String SESSION_ID = "sessionId";

    /**
     * Return the LOGGER.
     */
    private static Logger getLogger() {
        return LOGGER;
    }

    /**
     * Using service, create a QName for the SOAP session header.
     *
     * @param service the service for whom we desire a QName for the session header.
     *
     * @return a QName for the session header.
     *
     * @throws IllegalArgumentException if service is null.
     */
    static QName computeSessionHeaderName(final Service service) {
        ObjectUtils.ensureObject(service, "Must provide a service!");

        return new QName(service.getServiceName().getNamespaceURI(), SESSION_HEADER);
    }

    /**
     * Computes a port name. If the web service type is custom, it's the port name on the service class. Otherwise its the API
     * version found in the security mgr.
     *
     * @param credentials    the credentials to use when calling to SFDC.
     * @param webServiceType the type of web service.
     * @param service        the web service itself.
     *
     *
     * @return either the service class' port name or the API version (if not a custom web service) or the API version found in the
     *         security mgr.
     */
    static String computePortName(final Credentials credentials, final WebServiceTypeEnum webServiceType, final Service service) {
        final String retVal = (webServiceType == WebServiceTypeEnum.CUSTOM_SERVICE_TYPE ? ServiceUtils.getPortName(service.getClass()) : credentials.getApiVersion());

        LoggerUtils.log(getLogger(), Level.FINEST, "Computed port name [{0}] for web service type [{1}] and service [{2}]", retVal, webServiceType, service);

        return retVal;
    }

    /**
     * Computes a port name. If the web service type is custom, it's the port name on the service class. Otherwise its the API
     * version found in the security mgr.
     *
     * @param securityMgr    the security manager to use when calling to SFDC.
     * @param webServiceType the type of web service.
     * @param service        the web service itself.
     *
     * @return either the service class' port name or the API version (if not a custom web service) or the API version found in the
     *         security mgr.
     */
    static String computePortName(final SecurityMgr securityMgr, final WebServiceTypeEnum webServiceType, final Service service) {
        return computePortName(securityMgr.getCredentials(), webServiceType, service);
    }

    /**
     * Will compute a URL string from baseServerUrl, partialUrl and name. The baseServerUrl may have extraneous data. For example:
     * http://na7.salesforce.com/alpha/beta. We want to compute http://na7.salesforce.com/ plus the name.
     *
     * @param baseServerUrl a full SFDC web service URL.
     * @param partialUrl    the sub portion of the URL.
     * @param name          the web service name.
     *
     * @return a URL string for the web service.
     */
    static String computeUrlString(final String baseServerUrl, final String partialUrl, final String name) {
        final String retVal = StringUtils.concat(baseServerUrl, '/', partialUrl, '/', name);

        LoggerUtils.log(getLogger(), Level.FINEST, "Computed URL [{1}]", retVal);

        return retVal;
    }

    /**
     * Will compute a URL string from securityMgr (contains the session's URL), the partial url from webServiceType and the
     * port of the service (for custom services it's contained in the service or non-custom the API version).
     *
     * @param securityMgr    contains the session's URL.
     * @param webServiceType contains the partial URL.
     * @param service        contains the port to compute the name.
     *
     * @return the String version of the url.
     */
    static String computeUrlString(final SecurityMgr securityMgr, final String baseUrl, final WebServiceTypeEnum webServiceType, final Service service) {
        return computeUrlString(baseUrl, webServiceType.getWebServiceSubUrl().getPartialUrl(), computePortName(securityMgr, webServiceType, service));
    }

    /**
     * Default constructor not allowed.
     */
    private SoapUtils() {
    }
}
