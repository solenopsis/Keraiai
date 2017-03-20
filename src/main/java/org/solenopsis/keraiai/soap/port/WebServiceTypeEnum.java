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
import org.flossware.jcore.utils.soap.ServiceUtils;
import org.flossware.jcore.utils.soap.SoapUtils;
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
     * {@inheritDoc}
     */
    @Override
    public <P> P createPort(final SecurityMgr securityMgr) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security mananger!");

        return SoapUtils.setUrl((P) getWebService().createPort(), SecurityUtils.computeLoginUrl(securityMgr, this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createSessionPort(final SecurityMgr securityMgr) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security mananger!");

        return createProxyPort(securityMgr, getWebService().getService());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final SecurityMgr securityMgr, final Service service, Class<P> portType) {
        ObjectUtils.ensureObject(service, "Must provide a service!");
        ObjectUtils.ensureObject(portType, "Must provide a port type!");

        // Retrieving the port in a threaded capacity is highly synchronized.
        // By prefetching here, all subsequent calls happen very fast.
        service.getPort(portType);

        // The type returned can also be cast to a SecurityMgr.  Useful if default
        // implementations are used for SecurityMgr - the user of the proxy can
        // then get the SecurityMgr used by casting and using...
        return (P) Proxy.newProxyInstance(WebServiceTypeEnum.class.getClassLoader(), new Class[]{portType, SecurityMgr.class}, new PortInvocationHandler(securityMgr, this, service, portType));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final SecurityMgr securityMgr, final S service) {
        ObjectUtils.ensureObject(service, "Must provide a service!");

        return (P) createProxyPort(securityMgr, service, ServiceUtils.getPortType(service.getClass()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final SecurityMgr securityMgr, final Class<S> serviceClass, final URL wsdlResource) {
        return createProxyPort(securityMgr, ServiceUtils.createService(serviceClass, wsdlResource));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final SecurityMgr securityMgr, final Class<S> serviceClass, final String wsdlResource) {
        return createProxyPort(securityMgr, ServiceUtils.createService(serviceClass, wsdlResource));
    }
}
