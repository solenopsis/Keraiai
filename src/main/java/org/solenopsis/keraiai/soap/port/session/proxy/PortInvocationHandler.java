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
package org.solenopsis.keraiai.soap.port.session.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import javax.xml.ws.Service;
import org.flossware.jcore.AbstractCommonBase;
import org.flossware.jcore.utils.ObjectUtils;
import org.solenopsis.keraiai.soap.WebServiceTypeEnum;
import org.solenopsis.keraiai.soap.security.LoginContext;

/**
 * Acts as a proxy to call methods on ports. This is the real place that autologins, retries, etc happen.
 *
 * @author Scot P. Floess
 */
class PortInvocationHandler extends AbstractCommonBase implements InvocationHandler {

    /**
     * Total number of retries until we fail a call.
     */
    public static final int MAX_RETRIES = 4;

    /**
     * The port factory for whom we make calls to ports.
     */
    private final ProxiedSessionPortFactory proxiedSessionPortFactory;

    /**
     * The type of web service.
     */
    private final WebServiceTypeEnum webServiceType;

    /**
     * The web service (can instantiate ports).
     */
    private final Service service;

    /**
     * The type of port to call on the web service.
     */
    private final Class portType;

    /**
     * The name for the web service. For "stock" SFDC web services (apex, enterprise, metatadata, partner and tooling) its the API
     * version). For custom web services is the name of the web service itself. Will be used as part of the URL when binding.
     */
    private final String name;

    private ProxiedSessionPortFactory getProxiedSessionPortFactory() {
        return proxiedSessionPortFactory;
    }

    /**
     * Return the web service type.
     *
     * @return the web service type.
     */
    private WebServiceTypeEnum getWebServiceType() {
        return webServiceType;
    }

    /**
     * Return the web service that can create ports to call.
     *
     * @return the web service that can create ports to call.
     */
    private Service getService() {
        return service;
    }

    /**
     * Return the type of port that will be called.
     *
     * @return the type of port that will be called.
     */
    private Class getPortType() {
        return portType;
    }

    /**
     * Return the name for the web service. For "stock" SFDC web services (apex, enterprise, metatadata, partner and tooling) its
     * the API version). For custom web services is the name of the web service itself. Will be used as part of the URL when
     * binding.
     *
     * @return the name for the web service.
     */
    private String getName() {
        return name;
    }

    /**
     * This constructor all one needs to provide proxy calls for autologins and retries.
     *
     * @param sessionPortFactory can create ports in which we can make actual web service calls.
     * @param webServiceType the type of web service being used.
     * @param service the web service that can create ports.
     * @param portType the type of port to create for web service calls.
     * @param name the name of the web service used in the URL.
     *
     * @throws IllegalArgumentException if any of the params are null or if name is blank.
     */
    <P> PortInvocationHandler(final ProxiedSessionPortFactory proxiedSessionPortFactory, final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name) {
        this.proxiedSessionPortFactory = ObjectUtils.ensureObject(proxiedSessionPortFactory, "Must have a proxied session port factory!");
        this.webServiceType = ObjectUtils.ensureObject(webServiceType, "WebServiceEnum cannot be null!");
        this.service = ObjectUtils.ensureObject(service, "Service cannot be null!");
        this.portType = ObjectUtils.ensureObject(portType, "Class for port type cannot be null!");

        this.name = name;
    }

    /**
     * Return the object for whom we will make a method call.
     *
     * @param loginContext contains our session id for port calls.
     *
     * @return the object to make calls.
     */
    Object createCaller(final LoginContext loginContext) {
        ObjectUtils.ensureObject(loginContext, "Should have a login context");

        return logAndReturn(Level.FINEST, "Created caller [{0]}", getProxiedSessionPortFactory().doCreateSessionPort(getWebServiceType(), getService(), getPortType(), getName(), loginContext));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        ObjectUtils.ensureObject(proxy, "Must have a proxy object in which to call methods!");

        int totalCalls = 0;

        log(Level.FINE, "Calling [{0}.{1}]", proxy.getClass(), method.getName());

        LoginContext loginContext = null;
        Throwable toRaise = null;

        do {
            try {
                loginContext = getProxiedSessionPortFactory().getSecurityMgr().getSession();

                return method.invoke(createCaller(loginContext), args);
            } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException callFailure) {
                getLogger().log(Level.WARNING, "Trouble calling " + proxy.getClass().getName() + "." + method.getName() + "()", toRaise);
                toRaise = callFailure;
                ProxyUtils.processException(callFailure, method);
            }

            getProxiedSessionPortFactory().getSecurityMgr().resetSession(loginContext);
        } while (ProxyUtils.isCallRetriable(++totalCalls));

        throw toRaise;
    }
}
