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
package org.solenopsis.keraiai.soap.security.enterprise;

import org.flossware.jcore.utils.ObjectUtils;
import org.solenopsis.keraiai.soap.credentials.Credentials;
import org.solenopsis.keraiai.soap.security.AbstractLoginContext;
import org.solenopsis.keraiai.wsdl.enterprise.LoginResult;

/**
 * Enterprise web service login result.
 *
 * @author Scot P. Floess
 */
class EnterpriseLoginContext extends AbstractLoginContext {

    /**
     * Our login result.
     */
    private final LoginResult loginResult;

    /**
     * Return our login result.
     *
     * @return our login result.
     */
    LoginResult getEnterpriseLoginResult() {
        return loginResult;
    }

    /**
     * Constructor sets the login result and security manager who constructed self.
     *
     * @param loginContext result of login.
     * @param credentials the credentials used for login.
     *
     * @throws IllegalArgumentException if loginContext or credentials are null.
     */
    EnterpriseLoginContext(final LoginResult loginResult, final Credentials credentials) {
        super(credentials);

        this.loginResult = ObjectUtils.ensureObject(loginResult, "LoginResult must not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMetadataServerUrl() {
        return getEnterpriseLoginResult().getMetadataServerUrl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPasswordExpired() {
        return getEnterpriseLoginResult().isPasswordExpired();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSandbox() {
        return getEnterpriseLoginResult().isSandbox();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSessionId() {
        return getEnterpriseLoginResult().getSessionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return getEnterpriseLoginResult().getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServerUrl() {
        return getEnterpriseLoginResult().getServerUrl();
    }
}
