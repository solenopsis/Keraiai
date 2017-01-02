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

import org.flossware.jcore.AbstractCommonBase;
import org.flossware.jcore.utils.ObjectUtils;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.LoginContext;

/**
 * Abstract base class for logins which include the Credentials used and the login result.
 *
 * @author Scot P. Floess
 *
 * @param <T> the type of login result.
 */
public abstract class AbstractLoginContext<T> extends AbstractCommonBase implements LoginContext {

    /**
     * The login result.
     */
    private final T loginResult;

    /**
     * Our base URL for the SFDC server to use.
     */
    private final String baseUrl;

    /**
     * Credentials for login.
     */
    private final Credentials credentials;

    /**
     * Constructor to set result of login, credentials used and the security manager who created self.
     *
     * @param loginResult the actual login result.
     * @param credentials credentials for login.
     *
     * @throws IllegalArgumentException if credentials is null.
     */
    protected AbstractLoginContext(final T loginResult, final Credentials credentials) {
        this.loginResult = ObjectUtils.ensureObject(loginResult, "Must provide a login result");
        this.credentials = ObjectUtils.ensureObject(credentials, "Credentials must not be null");
        this.baseUrl = SecurityUtils.computeUrlString(getServerUrl());
    }

    /**
     * The login result.
     *
     * @return the login result.
     */
    protected final T getLoginResult() {
        return loginResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getBaseServerUrl() {
        return baseUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Credentials getCredentials() {
        return credentials;
    }
}
