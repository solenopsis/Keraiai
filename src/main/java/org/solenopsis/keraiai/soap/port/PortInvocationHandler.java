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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import javax.xml.ws.Service;
import org.flossware.jcore.AbstractCommonBase;
import org.flossware.jcore.utils.ObjectUtils;
import org.solenopsis.keraiai.LoginContext;
import org.solenopsis.keraiai.SecurityMgr;

/**
 * Acts as a proxy to call methods on ports. This is the real place that auto logins, retries, etc. happen. We leverage the
 * <code>Service.getPort(Class)</code> method to retrieve the port at will to make method calls on the web service.
 *
 * @author Scot P. Floess
 */
final class PortInvocationHandler extends AbstractCommonBase implements InvocationHandler {

    /**
     * The security manager for logins, relogins, etc.
     */
    private final SecurityMgr securityMgr;

    /**
     * The url of the web service to call.
     */
    private final String url;

    /**
     * The web service itself.
     */
    private final Service service;

    /**
     * The port on the web service.
     */
    private final Class portType;

    /**
     * Return the security manager.
     *
     * @return the security manager.
     */
    private SecurityMgr getSecurityMgr() {
        return securityMgr;
    }

    /**
     * Return the web service url to call.
     *
     * @return the web service url to call.
     */
    private String getUrl() {
        return url;
    }

    /**
     * Return the web service being used.
     *
     * @return the web service being used.
     */
    private Service getService() {
        return service;
    }

    /**
     * Return the port type on the web service.
     *
     * @return the port type on the web service.
     */
    private Class getPortType() {
        return portType;
    }

    /**
     * This constructor all one needs to provide proxy calls for autologins and retries.
     *
     * @param securityMgr    used for login, re-login, etc.
     * @param webServiceType the type of web service being used.
     * @param service        the web service to call.
     * @param portType       used to retrieve a port from the service.
     *
     * @throws IllegalArgumentException if any of the params are null.
     */
    <P> PortInvocationHandler(final SecurityMgr securityMgr, final WebServiceTypeEnum webServiceType, final Service service, final Class portType) {
        this.securityMgr = ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");
        this.service = ObjectUtils.ensureObject(service, "Must provide a service!");
        this.portType = ObjectUtils.ensureObject(portType, "Must provide a port type!");
        this.url = PortUtils.computeSessionUrl(securityMgr, ObjectUtils.ensureObject(webServiceType, "Must provide a web service type!"), service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        ObjectUtils.ensureObject(proxy, "Must have a proxy object in which to call methods!");
        ObjectUtils.ensureObject(method, "Must provide a method to call!");

        log(Level.FINE, "Calling [{0}.{1}]", proxy.getClass(), method.getName());

        int totalCalls = 0;
        LoginContext loginContext = getSecurityMgr().getSession();
        Throwable toRaise = null;

        do {
            try {
                return method.invoke(PortUtils.createSessionPort(getSecurityMgr(), getService(), getPortType(), getUrl()), args);
            } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException callFailure) {
                getLogger().log(Level.WARNING, "Trouble calling " + proxy.getClass().getName() + "." + method.getName() + "()", toRaise);
                toRaise = callFailure;
                PortUtils.processException(callFailure, method);
            }

            loginContext = getSecurityMgr().resetSession(loginContext);
        } while (PortUtils.isCallRetriable(++totalCalls));

        throw toRaise;
    }
}
