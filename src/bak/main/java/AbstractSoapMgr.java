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

import org.solenopsis.keraiai.soap.port.SessionIdSoapRequestHeaderHandler;
import org.solenopsis.keraiai.soap.port.WebServiceTypeEnum;
import javax.xml.ws.Service;
import org.flossware.jcore.AbstractCommonBase;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;
import org.flossware.jcore.utils.soap.ServiceUtils;
import org.solenopsis.keraiai.SecurityMgr;

/**
 * Abstract base class to deal with SOAP.
 *
 * @author Scot P. Floess
 */
public abstract class AbstractSoapMgr extends AbstractCommonBase {

    /**
     * Prepare a port for use.
     *
     * @param <P>            the type of port to prepare.
     *
     * @param securityMgr    contains credentials.
     * @param baseUrl        the URL to use on <code>port</code>.
     * @param webServiceType the type of web service.
     * @param service        the web service being called.
     * @param port           the port being called on the web service.
     *
     * @return the prepared port.
     */
    protected <P> P preparePort(final SecurityMgr securityMgr, final String baseUrl, final WebServiceTypeEnum webServiceType, final Service service, P port) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security manager");
        StringUtils.ensureString(baseUrl, "Must provide a base URL for the port");
        ObjectUtils.ensureObject(webServiceType, "Must provide a web service type");
        ObjectUtils.ensureObject(service, "Must provide a service");
        ObjectUtils.ensureObject(port, "Must provide a port");

        return org.flossware.jcore.utils.soap.SoapUtils.setUrl(port, SoapUtils.computeUrlString(securityMgr, baseUrl, webServiceType, service));
    }

    /**
     * Create a port for use, setting the URL to be that of <code>baseUrl</code>.
     *
     * @param <P>            the type of port.
     *
     * @param securityMgr    contains credentials.
     * @param baseUrl        the URL to use on <code>port</code>.
     * @param webServiceType the type of web service.
     * @param service        the web service being called.
     * @param portType       the type of port offered by <code>service</code>.
     *
     * @return the ready to use port.
     */
    protected <P> P createPort(final SecurityMgr securityMgr, final String baseUrl, final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType) {
        return preparePort(securityMgr, baseUrl, webServiceType, service, service.getPort(portType));
    }

    /**
     * Create a port for use, setting the URL to be that of <code>base</url>. The port will be extrapolated from the service.
     *
     * @param <P>            the type of port.
     *
     * @param securityMgr    contains credentials.
     * @param baseUrl        the URL to use on <code>port</code>.
     * @param webServiceType the type of web service.
     * @param service        the web service being called.
     *
     * @return the ready to use port.
     */
    protected <P> P createPort(final SecurityMgr securityMgr, final String baseUrl, final WebServiceTypeEnum webServiceType, final Service service) {
        return (P) createPort(securityMgr, baseUrl, webServiceType, service, ServiceUtils.getPortType(service.getClass()));
    }

    /**
     * Create a session based port for use, setting the URL to be that of the login context in <code>securityMgr</code> and session
     * session id into the SOAP header of the returned port.
     *
     * @param <P>            the type of port.
     *
     * @param securityMgr    contains credentials.
     * @param webServiceType the type of web service.
     * @param service        the web service being called.
     * @param portType       the type of port offered by <code>service</code>.
     *
     * @return the ready to use port.
     */
    protected <P> P createSessionPort(final SecurityMgr securityMgr, final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType) {
        final P retVal = createPort(securityMgr, securityMgr.getSession().getBaseServerUrl(), webServiceType, service, portType);

        org.flossware.jcore.utils.soap.SoapUtils.setHandler(retVal, new SessionIdSoapRequestHeaderHandler(service, securityMgr));

        return retVal;
    }

    /**
     * Create a session based port for use, setting the URL to be that of the login context in <code>securityMgr</code> and session
     * session id into the SOAP header of the returned port. The port will be extrapolated from <code>service</code>.
     *
     * @param <P>            the type of port.
     *
     * @param securityMgr    contains credentials.
     * @param webServiceType the type of web service.
     * @param service        the web service being called.
     *
     * @return the ready to use port.
     */
    protected <P> P createSessionPort(final SecurityMgr securityMgr, final WebServiceTypeEnum webServiceType, final Service service) {
        return (P) createSessionPort(securityMgr, webServiceType, service, ServiceUtils.getPortType(service.getClass()));
    }

    /**
     * Default constructor.
     */
    protected AbstractSoapMgr() {
    }
}
