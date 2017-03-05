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
package org.solenopsis.keraiai.soap.port;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;
import org.flossware.jcore.utils.soap.ServiceUtils;
import org.flossware.jcore.utils.soap.SoapUtils;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.SecurityMgr;
import static org.solenopsis.keraiai.soap.port.WebServiceTypeEnum.CUSTOM_SERVICE_TYPE;
import org.solenopsis.keraiai.soap.utils.ExceptionUtils;

/**
 * Utility class for port functionality.
 *
 * @author Scot P. Floess
 */
final class PortUtils {

    /**
     * When setting up the soap header, we need to set the session header using this attribute.
     */
    static final String SESSION_HEADER = "SessionHeader";

    /**
     * Our LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(PortUtils.class.getName());

    /**
     * Return the LOGGER.
     */
    private static Logger getLogger() {
        return LOGGER;
    }

    /**
     * Maximum retries.
     */
    static final int MAX_RETRIES = 4;

    /**
     * Using service, create a QName for the SOAP session header.
     *
     * @param service the service for whom we desire a QName for the session header.
     *
     * @return a QName for the session header.
     *
     * @throws IllegalArgumentException if service is null.
     */
    static QName computeSessionHeaderName(final String namespaceUri) {
        StringUtils.ensureString(namespaceUri, "Must provide a namespace URI!");

        return new QName(namespaceUri, SESSION_HEADER);
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
    static QName computeSessionHeaderName(final QName serviceName) {
        ObjectUtils.ensureObject(serviceName, "Must provide a service QName!");

        return computeSessionHeaderName(serviceName.getNamespaceURI());
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

        return computeSessionHeaderName(service.getServiceName());
    }

    /**
     * Can we retry a call?
     *
     * @param totalCalls the total calls made thus far.
     *
     * @return true if a call can be retried or false if we've reached our limit.
     */
    static boolean isCallRetriable(int totalCalls) {
        return totalCalls < MAX_RETRIES;
    }

    /**
     * When an exception happens on call, this method will handle the exception.
     *
     * @param callFailure the exception that arose when calling SFDC.
     * @param method      the method being called when the failure arose.
     *
     * @throws Throwable if the exception cannot be handled.
     */
    static void processException(final Throwable callFailure, final Method method) throws Throwable {
        if (!ExceptionUtils.isReloginException(callFailure)) {
            getLogger().log(Level.SEVERE, "Trouble calling [{0}] - [{1}]...raising exception", new Object[]{method.getName(), callFailure.getLocalizedMessage()});

            throw callFailure;
        }

        getLogger().log(Level.WARNING, "Received a relogin exception when calling [{0}] - initiating a new login", method.getName());
    }

    /**
     * Determine if <code>webServiceType</code> is a CUSTOM_SERVICE_TYPE, returning true if so, or false otherwise.
     *
     * @param webServiceType to examine if a CUSTOM_SERVICE_TYPE.
     *
     * @return true if <code>webServiceType</code> is a CUSTOM_SERVICE_TYPE or false if not.
     */
    static boolean isWebServiceCustomService(final WebServiceTypeEnum webServiceType) {
        return webServiceType == CUSTOM_SERVICE_TYPE;
    }

    /**
     * Compute the port name for the <code>service</code> - if self is a custom web service, it's the name of the QName of the port
     * on the service, otherwise it's the API versio.
     *
     * @param webServiceType the type of web service.
     * @param apiVersion     the API version.
     * @param service        contains the QName of the port and is used if self is a custom web service.
     *
     * @return the port name.
     *
     * @throws IllegalArgumentException if any of the params are null.
     */
    static String computePortName(final WebServiceTypeEnum webServiceType, final String apiVersion, final Service service) {
        ObjectUtils.ensureObject(webServiceType, "Must provide a web service type!");
        StringUtils.ensureString(apiVersion, "Must provide an API version!");
        ObjectUtils.ensureObject(service, "Must provide a service!");

        return !isWebServiceCustomService(webServiceType) ? apiVersion : ServiceUtils.getPortName(service.getClass());
    }

    /**
     * Compute the port name for the <code>service</code> - if self is a custom web service, it's the name of the QName of the port
     * on the service, otherwise it's the API version as found in the <code>credentials</code>.
     *
     * @param webServiceType the type of web service.
     * @param credentials    contains the API version from it's credentials.
     * @param service        contains the QName of the port and is used if self is a custom web service.
     *
     * @return the port name.
     *
     * @throws IllegalArgumentException if any of the params are null.
     */
    static String computePortName(final WebServiceTypeEnum webServiceType, final Credentials credentials, final Service service) {
        ObjectUtils.ensureObject(credentials, "Must provide credentials!");

        return computePortName(webServiceType, credentials.getApiVersion(), service);
    }

    /**
     * Compute the port name for the <code>service</code> - if self is a custom web service, it's the name of the QName of the port
     * on the service, otherwise it's the API version as found in the <code>securityMgr</code>'s credentials.
     *
     * @param webServiceType the type of web service.
     * @param securityMgr    contains the API version from it's credentials.
     * @param service        contains the QName of the port and is used if self is a custom web service.
     *
     * @return the port name.
     *
     * @throws IllegalArgumentException if any of the params are null.
     */
    static String computePortName(final WebServiceTypeEnum webServiceType, final SecurityMgr securityMgr, final Service service) {
        ObjectUtils.ensureObject(webServiceType, "Must provide a web service type!");
        ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");
        ObjectUtils.ensureObject(securityMgr, "Must provide a service!");

        return computePortName(webServiceType, securityMgr.getCredentials(), service);
    }

    /**
     * Create a usable session port including a URL and session id .
     *
     * @param securityMgr contains the session id.
     * @param service     the service to call.
     * @param portType    used to retrieve a port from the service.
     * @param url         the URL for the port.
     *
     * @return a usable port that has session id and URL set.
     */
    static Object createSessionPort(final SecurityMgr securityMgr, final Service service, final Class portType, final String url) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");
        ObjectUtils.ensureObject(service, "Must probide a service!");
        ObjectUtils.ensureObject(portType, "Must provide a port type!");
        StringUtils.ensureString(url, "Must provide a URL!");

        final Object port = service.getPort(portType);
        SoapUtils.setHandler(port, new SessionIdSoapRequestHeaderHandler(securityMgr, service));
        SoapUtils.setUrl(port, url);

        getLogger().log(Level.FINE, "Port = [{0}]", port);

        return port;
    }

    /**
     * Compute the session URL using the base URL from the <code>securityMgr</code>'s session, the partial URL for a web service
     * and the name of the port. By port name, for custom services it is the name of the QName of the port for service, otherwise it
     * is the API version for from the credentials as found in the <code>securityMgr</code>'s credentials.
     *
     * @param securityMgr contains session id and credentials.
     * @param service     if using a custom web service, will use the name of the QName of the port name of the service. Otherwise
     *                    it is the API version found in <code>securityMgr</code>'s session.
     *
     * @return the computed session URL.
     */
    static String computeSessionUrl(final WebServiceTypeEnum webServiceType, final SecurityMgr securityMgr, final Service service) {
        ObjectUtils.ensureObject(webServiceType, "Must provide a web service type!");
        ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");
        ObjectUtils.ensureObject(service, "Must provide a service!");

        return StringUtils.concatWithSeparator(
                false, "/", securityMgr.getSession().getBaseServerUrl(),
                webServiceType.getWebServiceSubUrl().getPartialUrl(),
                PortUtils.computePortName(webServiceType, securityMgr, service)
        );
    }

    /**
     * Default constructor not allowed.
     */
    private PortUtils() {
    }
}
