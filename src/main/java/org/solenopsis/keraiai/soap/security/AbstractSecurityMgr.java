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
package org.solenopsis.keraiai.soap.security;

import java.util.logging.Level;
import org.flossware.jcore.utils.ObjectUtils;
import org.solenopsis.keraiai.soap.credentials.Credentials;
import org.solenopsis.keraiai.soap.port.AbstractPortFactory;

/**
 * Represents a way to login and out of SFDC. Can be used to abstract out the enterprise, partner and tooling web services.
 *
 * @author Scot P. Floess
 *
 * @param <P> The type of port for login/logout.
 */
public abstract class AbstractSecurityMgr<P> extends AbstractPortFactory implements SecurityMgr {

    /**
     * Used for session based logins.
     */
    private final Credentials credentials;

    /**
     * Login context used in sessions.
     */
    private LoginContext loginContext;

    /**
     * Return the login context.
     *
     * @return the new login context.
     */
    synchronized LoginContext getLoginContext() {
        return loginContext;
    }

    /**
     * Set and return the newly defined login context.
     *
     * @param loginContext the new login context.
     *
     * @return the new login context.
     */
    synchronized LoginContext setLoginContext(final LoginContext loginContext) {
        this.loginContext = loginContext;

        log(Level.FINEST, "Setting loging context to [{0}]", loginContext);

        return loginContext;
    }

    /**
     * Return the login web service type.
     *
     * @return the login web service type.
     */
    protected abstract LoginWebServiceTypeEnum getLoginWebServiceType();

    /**
     * Using port, perform a login with the supplied credentials.
     *
     * @param port the port to call a login on.
     *
     * @return the login context from a login.
     *
     * @throws Exception if any problems arise performing a login.
     */
    protected abstract LoginContext doLogin(P port) throws Exception;

    /**
     * Using port, perform a logout.
     *
     * @param port the port to use to perform a logout.
     *
     * @throws Exception if any problems arise performing a logout.
     */
    protected abstract void doLogout(P port) throws Exception;

    /**
     * This constructor sets the session credentials.
     *
     * @param credentials the session credentials.
     *
     * @throws IllegalArgumentException if credentials is null.
     */
    protected AbstractSecurityMgr(final Credentials credentials) {
        this.credentials = ObjectUtils.ensureObject(credentials, "Must have credentials");
    }

    /**
     * Default constructor.
     */
    protected AbstractSecurityMgr() {
        this(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized LoginContext getSession() {
        if (null != loginContext) {
            return loginContext;
        }

        return login();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized LoginContext resetSession(final LoginContext loginContext) {
        if (this.loginContext != loginContext) {
            log(Level.FINEST, "Not resetting as old context [{0}] != new context [{1}]", this.loginContext, loginContext);
            return this.loginContext;
        }

        return login();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized LoginContext login() {
        log(Level.FINEST, "Requesting login");

        try {
            return setLoginContext(doLogin(
                    (P) createPort(
                            getLoginWebServiceType().getApiWebServiceType().getWebServiceType(),
                            getCredentials().getUrl(),
                            getLoginWebServiceType().getApiWebServiceType().getWebService(),
                            getLoginWebServiceType().getApiWebServiceType().getPortType(),
                            getCredentials().getApiVersion())
            ));
        } catch (final RuntimeException runtimeException) {
            throw runtimeException;
        } catch (final Throwable throwable) {
            throw new LoginException(throwable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void logout() {
        log(Level.FINEST, "Requesting logout");

        try {
            doLogout((P) createSessionPort(
                    getLoginWebServiceType().getApiWebServiceType().getWebServiceType(),
                    getSession().getServerUrl(),
                    getLoginWebServiceType().getApiWebServiceType().getWebService(),
                    getLoginWebServiceType().getApiWebServiceType().getPortType(),
                    getCredentials().getApiVersion(),
                    getSession().getSessionId()
            ));
            setLoginContext(null);
        } catch (final RuntimeException runtimeException) {
            throw runtimeException;
        } catch (final Throwable throwable) {
            throw new LogoutException(throwable);
        }
    }
}
