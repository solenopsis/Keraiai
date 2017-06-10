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
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import javax.xml.ws.Service;
import org.flossware.jcore.AbstractCommonBase;
import org.flossware.jcore.utils.ObjectUtils;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.soap.LoginWebService;
import org.solenopsis.keraiai.soap.WebServiceType;
import org.solenopsis.keraiai.soap.exception.ExceptionContext;
import org.solenopsis.keraiai.soap.exception.SalesforceExceptionEnum;

/**
 * Acts as a proxy to call methods on ports. This is the real place that auto logins, retries, etc. happen. We leverage the
 * <code>Service.getPort(Class)</code> method to retrieve the port at will to make method calls on the web service.
 *
 * @author Scot P. Floess
 */
final class PortInvocationHandler extends AbstractCommonBase implements InvocationHandler {

    /**
     * Total calls for retry.
     */
    static final int MAX_RETRIES = 8;

    /**
     * The credentials.
     */
    private final Credentials credentials;

    /**
     * The login web service.
     */
    private final LoginWebService loginWebService;

    /**
     * The web service type.
     */
    private final WebServiceType webServiceType;

    /**
     * The web service itself.
     */
    private final Service service;

    /**
     * The port on the web service.
     */
    private final Class portType;

    /**
     * Our port.
     */
    private final AtomicReference port;

    /**
     * Return the credentials
     *
     * @return the credentials.
     */
    Credentials getCredentials() {
        return credentials;
    }

    /**
     * Return the login web service
     *
     * @return the login web service.
     */
    LoginWebService getLoginWebService() {
        return loginWebService;
    }

    /**
     * Return the web service type.
     *
     * @return the web service type.
     */
    WebServiceType getWebServiceType() {
        return webServiceType;
    }

    /**
     * Return the web service being used.
     *
     * @return the web service being used.
     */
    Service getService() {
        return service;
    }

    /**
     * Return the port type on the web service.
     *
     * @return the port type on the web service.
     */
    Class getPortType() {
        return portType;
    }

    /**
     * Return the port.
     */
    AtomicReference getPort() {
        return port;
    }

    /**
     * Return true if call retries allowed.
     *
     * @param callsThusFar is the number of calls made thus far.
     *
     * @return true if calls can be retried.
     */
    static boolean isCallRetriable(final int callsThusFar) {
        return callsThusFar <= MAX_RETRIES;
    }

    /**
     * This constructor all one needs to provide proxy calls for autologins and retries.
     *
     * @param securityMgr used for login, re-login, etc.
     * @param webServiceType the type of web service being used.
     * @param service the web service to call.
     * @param portType used to retrieve a port from the service.
     *
     * @throws IllegalArgumentException if any of the params are null.
     */
    <P> PortInvocationHandler(final Credentials credentials, final LoginWebService loginWebService, final WebServiceType webServiceType, final Service service, final Class portType) {
        this.credentials = ObjectUtils.ensureObject(credentials, "Must provide credentials!");
        this.loginWebService = ObjectUtils.ensureObject(loginWebService, "Must provide a login web service!");
        this.webServiceType = ObjectUtils.ensureObject(webServiceType, "Must provide a web service type!");
        this.service = ObjectUtils.ensureObject(service, "Must provide a service!");
        this.portType = ObjectUtils.ensureObject(portType, "Must provide a port type!");
        this.port = new AtomicReference(SalesforcePortUtils.createSessionPort(webServiceType, loginWebService.login(credentials), service, portType));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        ObjectUtils.ensureObject(proxy, "Must have a proxy object in which to call methods!");
        ObjectUtils.ensureObject(method, "Must provide a method to call!");

        log(Level.FINE, "Calling [{0}.{1}]", getPortType().getName(), method.getName());

        int totalCalls = 0;
        Throwable toRaise = null;

        final ExceptionContext exceptionContext = new ExceptionContext();

        do {
            try {
                return method.invoke(getPort().get(), args);
            } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException callFailure) {
                log(Level.WARNING, "Trouble calling [{0}.{1}()]", getPortType().getName(), method.getName());
                toRaise = callFailure;

                if (SalesforceExceptionEnum.isReloginException(exceptionContext.incrementFailureCount(toRaise))) {
                    getPort().set(SalesforcePortUtils.createSessionPort(getWebServiceType(), getLoginWebService().login(getCredentials()), getService(), getPortType()));
                }
            }
        } while (isCallRetriable(++totalCalls));

        log(Level.SEVERE, toRaise, "Unable to call [{0}].[{1}] after retry [{2}] attempts, raising exception.  Failures include [{3}]", port.get().getClass().getName(), method.getName(), totalCalls, exceptionContext.computeTotals());

        throw new IllegalStateException("Attempts to retry calls to Salesforce have failed after [" + totalCalls + "] times.  Failures are: " + exceptionContext.computeTotals(), toRaise);
    }
}
