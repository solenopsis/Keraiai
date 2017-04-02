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

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import org.flossware.jcore.AbstractCommonBase;
import org.flossware.jcore.utils.ObjectUtils;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.LoginContext;
import org.solenopsis.keraiai.SecurityMgr;

/**
 * Represents a way to login and out of SFDC. Can be used to abstract out the enterprise, partner and tooling web services.
 *
 * @author Scot P. Floess
 */
public abstract class AbstractSecurityMgr<P> extends AbstractCommonBase implements SecurityMgr {
    /**
     * The type of login web service.
     */
    private final LoginWebService loginWebService;

    /**
     * Used for session based logins.
     */
    private final Credentials credentials;

    /**
     * Login context used in sessions.
     */
    private AtomicReference<LoginContext> loginContext;

    /**
     * Return the login context.
     *
     * @return the new login context.
     */
    synchronized AtomicReference<LoginContext> getLoginContext() {
        return loginContext;
    }

    private LoginWebService getLoginWebService() {
        return loginWebService;
    }

    /**
     * Using port, perform a login with the supplied credentials.
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
     * @throws IllegalArgumentException if any params are null.
     */
    protected AbstractSecurityMgr(final LoginWebService loginWebService, final Credentials credentials) {
        this.loginWebService = ObjectUtils.ensureObject(loginWebService, "Must provide a LoginWebService");
        this.credentials = ObjectUtils.ensureObject(credentials, "Must have credentials");
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
    public LoginContext getSession() {
        if (null != getLoginContext().get()) {
            return getLoginContext().get();
        }

        return login();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginContext resetSession() {
        return login();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginContext login() {
        log(Level.FINEST, "Requesting login");

        try {
            getLoginContext().set(doLogin((P) getLoginWebService().getWebServiceType().createPort(this, getLoginWebService().getWebServiceType().getWebService().getService(), getLoginWebService().getWebServiceType().getWebService().getPortType())));
            return getLoginContext().get();
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
            doLogout((P) getLoginWebService().getWebServiceType().createSessionPort(this, getLoginWebService().getWebServiceType().getWebService().getService(), getLoginWebService().getWebServiceType().getWebService().getPortType()));
            getLoginContext().set(null);
        } catch (final RuntimeException runtimeException) {
            getLogger().log(Level.WARNING, "Trouble logging out", runtimeException);
            throw runtimeException;
        } catch (final Throwable throwable) {
            throw new LogoutException(throwable);
        }
    }
}
