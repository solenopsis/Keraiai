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
package org.solenopsis.keraiai.credentials;

import org.flossware.jcore.utils.StringUtils;

/**
 *
 * Uses simple Strings to contain the Credentials.
 *
 * @author sfloess
 *
 */
public class StringCredentials extends AbstractCredentials {

    /**
     * The URL for login.
     */
    private final String url;

    /**
     * The username.
     */
    private final String userName;

    /**
     * The password.
     */
    private final String password;

    /**
     * The security token.
     */
    private final String token;

    /**
     * The API version.
     */
    private final String apiVersion;

    /**
     * Sets the credential parts.
     *
     * @param url        the server URL for login.
     * @param userName   the user name.
     * @param password   the password.
     * @param token      the security token.
     * @param apiVersion the API version.
     */
    public StringCredentials(final String url, final String userName, final String password, final String token, final String apiVersion) {
        this.url = StringUtils.ensureString(url, "Must have a URL!");
        this.userName = StringUtils.ensureString(userName, "Must provide a username!");
        this.password = StringUtils.ensureString(password, "Must provide a password!");
        this.token = StringUtils.ensureString(token, "Must provide a token!");
        this.apiVersion = StringUtils.ensureString(apiVersion, "Must provide an API version!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserName() {
        return userName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToken() {
        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApiVersion() {
        return apiVersion;
    }
}
