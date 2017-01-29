/*
 * Copyright (C) 2015 Scot P. Floess
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

import java.lang.reflect.Proxy;
import java.net.URL;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;
import org.flossware.jcore.utils.soap.ServiceUtils;
import org.solenopsis.keraiai.LoginContext;
import org.solenopsis.keraiai.SecurityMgr;
import org.solenopsis.keraiai.soap.WebServiceEnum;
import org.solenopsis.keraiai.soap.WebServiceSubUrlEnum;

/**
 * Denotes an SFDC API web service and the sub URL one needs when calling an SFDC web service and the ability to create session ports
 * for SFDC web services.
 *
 * The fully qualified SFDC URLs for a web service always use partial URLs and a host plus either an API version
 * or a custom web service name. Here are some examples:
 * <ul>
 * <li>Apex = https://cs9.salesforce.com/services/Soap/s/31.0</li>
 * <li>Custom = https://cs9.salesforce.com/services/Soap/class/Foo</li>
 * <li>Enterprise = https://cs9.salesforce.com/services/Soap/m/32.0</li>
 * <li>Metadata = https://cs9.salesforce.com/services/Soap/m/33.0</li>
 * <li>Partner = https://cs9.salesforce.com/services/Soap/u/34.0</li>
 * <li>Tooling = https://cs9.salesforce.com/services/Soap/T/35.0</li>
 * </ul>
 *
 * When logging in, you have to use the typical production (https://login.salesforce.com) or sandbox (https://test.salesforce.com)
 * URLS with either the enterprise, partner or tooling partial URLs plus API version. Test login URL examples:
 * <ul>
 * <li>Enterprise = https://test.salesforce.com/services/Soap/c/32.0</li>
 * <li>Partner = https://test.salesforce.com/services/Soap/u/33.0</li>
 * <li>Tooling = https://test.salesforce.com/services/Soap/T/34.0</li>
 * </ul>
 *
 * Production login URL examples:
 * <ul>
 * <li>Enterprise = https://login.salesforce.com/services/Soap/c/32.0</li>
 * <li>Partner = https://login.salesforce.com/services/Soap/u/33.0</li>
 * <li>Tooling = https://login.salesforce.com/services/Soap/T/34.0</li>
 * </ul>
 *
 * An instance of Keraia's web services are included, along with the class of the port those web services use. The
 * exception here is for user defined custom web services as those will always be defined by consumers of this library.
 *
 * @author Scot P. Floess
 */
public enum WebServiceTypeEnum implements SessionPortFactory {
    APEX_SERVICE_TYPE(WebServiceEnum.APEX_SERVICE, WebServiceSubUrlEnum.APEX_TYPE),
    CUSTOM_SERVICE_TYPE(null, WebServiceSubUrlEnum.CUSTOM_TYPE),
    ENTERPRISE_SERVICE_TYPE(WebServiceEnum.ENTERPRISE_SERVICE, WebServiceSubUrlEnum.ENTERPRISE_TYPE),
    METADATA_SERVICE_TYPE(WebServiceEnum.METADATA_SERVICE, WebServiceSubUrlEnum.METADATA_TYPE),
    PARTNER_SERVICE_TYPE(WebServiceEnum.PARTNER_SERVICE, WebServiceSubUrlEnum.PARTNER_TYPE),
    TOOLING_SERVICE_TYPE(WebServiceEnum.TOOLING_SERVICE, WebServiceSubUrlEnum.TOOLING_TYPE);

    private final WebServiceEnum webService;
    private final WebServiceSubUrlEnum webServiceSubUrl;

    /**
     * This constructor sets the SFDC web service, port type and partial URL (as defined in the Java doc header).
     *
     * @param webServiceType   the SFDC web service.
     * @param webServiceSubUrl the port for the web service.
     */
    private WebServiceTypeEnum(final WebServiceEnum webService, final WebServiceSubUrlEnum webServiceSubUrl) {
        this.webService = webService;
        this.webServiceSubUrl = webServiceSubUrl;
    }

    /**
     * The sub URL when calling out to web services.
     *
     * @return the sub URL.
     */
    public WebServiceSubUrlEnum getWebServiceSubUrl() {
        return webServiceSubUrl;
    }

    /**
     * Return the web service or null for a custom service.
     *
     * @return the web service or null if none exists.
     */
    public WebServiceEnum getWebService() {
        return webService;
    }

    /**
     * Compute the session URL using <code<>baseURL</code>, the partial URL for a web service and the name of the port. By port
     * name, for custom services it is the name of the QName of the port for service, otherwise it is the API version for from the
     * credentials as found in the <code>securityMgr</code>'s credentials.
     *
     * @param baseUrl     is the server URL from which we will make a web service call.
     * @param securityMgr contains session id and credentials.
     * @param service     if using a custom web service, will use the name of the QName of the port name of the service. Otherwise
     *                    it is the API version found in <code>securityMgr</code>'s session.
     *
     * @return the computed session URL.
     */
    String computeSessionUrl(final String baseUrl, final SecurityMgr securityMgr, final Service service) {
        StringUtils.ensureString(baseUrl, "Must provide a base URL!");
        ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");
        ObjectUtils.ensureObject(service, "Must provide a service!");

        return StringUtils.concatWithSeparator(false, "/", baseUrl, getWebServiceSubUrl().getPartialUrl(), PortUtils.computePortName(this, securityMgr, service));
    }

    /**
     * Compute the session URL using the base URL from the <code>loginContext</code>'s session, the partial URL for a web service
     * and the name of the port. By port name, for custom services it is the name of the QName of the port for service, otherwise it
     * is the API version for from the credentials as found in the <code>securityMgr</code>'s credentials.
     *
     * @param loginContext contains the base URL of the server for whom we will make a web service call.
     * @param securityMgr  contains session id and credentials.
     * @param service      if using a custom web service, will use the name of the QName of the port name of the service. Otherwise
     *                     it is the API version found in <code>securityMgr</code>'s session.
     *
     * @return the computed session URL.
     */
    String computeSessionUrl(final LoginContext loginContext, final SecurityMgr securityMgr, final Service service) {
        ObjectUtils.ensureObject(loginContext, "Must provide a login context!");

        return computeSessionUrl(loginContext.getBaseServerUrl(), securityMgr, service);
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
    String computeSessionUrl(final SecurityMgr securityMgr, final Service service) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");

        return computeSessionUrl(securityMgr.getSession(), securityMgr, service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createSessionPort(final SecurityMgr securityMgr, final Service service, Class<P> portType) {
        return (P) Proxy.newProxyInstance(WebServiceTypeEnum.class.getClassLoader(), new Class[]{ portType }, new PortInvocationHandler(securityMgr, this, service, portType));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createSessionPort(final SecurityMgr securityMgr, final S service) {
        ObjectUtils.ensureObject(service, "Must provide a service!");

        return (P) createSessionPort(securityMgr, service, ServiceUtils.getPortType(service.getClass()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createSessionPort(final SecurityMgr securityMgr, final Class<S> serviceClass, final URL wsdlResource) {
        return createSessionPort(securityMgr, ServiceUtils.createService(serviceClass, wsdlResource));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createSessionPort(final SecurityMgr securityMgr, final Class<S> serviceClass, final String wsdlResource) {
        return createSessionPort(securityMgr, ServiceUtils.createService(serviceClass, wsdlResource));
    }
}
