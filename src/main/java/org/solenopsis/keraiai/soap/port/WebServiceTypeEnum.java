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
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.LoginContext;
import org.solenopsis.keraiai.soap.LoginWebService;
import org.solenopsis.keraiai.soap.SessionUrlFactory;
import org.solenopsis.keraiai.soap.WebServiceType;
import org.solenopsis.keraiai.soap.session.SessionUrlFactoryEnum;

/**
 * Denotes an SFDC API web service and the sub URL one needs when calling an SFDC web service and the ability to create session
 * ports for SFDC web services.
 *
 * The fully qualified SFDC URLs for a web service always use partial URLs and a host plus either an API version or a custom web
 * service name. Here are some examples:
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
 * An instance of Keraia's web services are included, along with the class of the port those web services use. The exception here is
 * for user defined custom web services as those will always be defined by consumers of this library.
 *
 * @author Scot P. Floess
 */
public enum WebServiceTypeEnum implements WebServiceType {
    APEX_SERVICE_TYPE(SessionUrlFactoryEnum.APEX_SESSION_URL_FACTORY),
    CUSTOM_SERVICE_TYPE(SessionUrlFactoryEnum.CUSTOM_SESSION_URL_FACTORY),
    ENTERPRISE_SERVICE_TYPE(SessionUrlFactoryEnum.ENTERPRISE_SESSION_URL_FACTORY),
    METADATA_SERVICE_TYPE(SessionUrlFactoryEnum.METADATA_SESSION_URL_FACTORY),
    PARTNER_SERVICE_TYPE(SessionUrlFactoryEnum.PARTNER_SESSION_URL_FACTORY),
    TOOLING_SERVICE_TYPE(SessionUrlFactoryEnum.TOOLING_SESSION_URL_FACTORY);

    /**
     * Can compute a session URL.
     */
    private final SessionUrlFactory sessionUrlFactory;

    /**
     * This constructor sets the SFDC web service, port type and partial URL (as defined in the Java doc header).
     *
     * @param webServiceType   the SFDC web service.
     * @param webServiceSubUrl the port for the web service.
     */
    private WebServiceTypeEnum(final SessionUrlFactory sessionUrlFactory) {
        this.sessionUrlFactory = sessionUrlFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionUrlFactory getSessionUrlFactory() {
        return sessionUrlFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final Credentials credentials, final LoginWebService loginWebService, final Service service, Class<P> portType) {
        ObjectUtils.ensureObject(service, "Must provide a service!");
        ObjectUtils.ensureObject(portType, "Must provide a port type!");

        // Retrieving the port in a threaded capacity is highly synchronized.
        // By prefetching here, all subsequent calls happen very fast.
        service.getPort(portType);

        // The type returned can also be cast to a LoginMgr.  Useful if default
        // implementations are used for LoginMgr - the user of the proxy can
        // then get the LoginMgr used by casting and using...
        return (P) Proxy.newProxyInstance(WebServiceTypeEnum.class.getClassLoader(), new Class[]{portType, LoginContext.class}, new PortInvocationHandler(credentials, loginWebService, this, service, portType));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final Credentials credentials, final Service service, Class<P> portType) {
        return createProxyPort(credentials, LoginWebService.DEFAULT_LOGIN_WEB_SERVICE, service, portType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final Credentials credentials, final LoginWebService loginWebService, final S service) {
        ObjectUtils.ensureObject(service, "Must provide a service!");

        return (P) createProxyPort(credentials, loginWebService, service, ServiceUtils.getPortType(service.getClass()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final Credentials credentials, final S service) {
        return createProxyPort(credentials, LoginWebService.DEFAULT_LOGIN_WEB_SERVICE, service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final Credentials credentials, final LoginWebService loginWebService, final Class<S> serviceClass, final URL wsdlResource) {
        return createProxyPort(credentials, loginWebService, ServiceUtils.createService(serviceClass, wsdlResource));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final Credentials credentials, final Class<S> serviceClass, final URL wsdlResource) {
        return createProxyPort(credentials, LoginWebService.DEFAULT_LOGIN_WEB_SERVICE, serviceClass, wsdlResource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final Credentials credentials, final LoginWebService loginWebService, final Class<S> serviceClass, final String wsdlResource) {
        return createProxyPort(credentials, loginWebService, ServiceUtils.createService(serviceClass, wsdlResource));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createProxyPort(final Credentials credentials, final Class<S> serviceClass, final String wsdlResource) {
        return createProxyPort(credentials, LoginWebService.DEFAULT_LOGIN_WEB_SERVICE, serviceClass, wsdlResource);
    }
}
