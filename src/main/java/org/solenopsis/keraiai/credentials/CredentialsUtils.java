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

import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;
import org.solenopsis.keraiai.Credentials;

/**
 * Credential related utilities.
 *
 * @author sfloess
 *
 */
public final class CredentialsUtils {
    /**
     * If no error message is provided, use this value.
     */
    public static final String DEFAULT_ERROR_MESSAGE = "Invalid credentials";

    /**
     * Default constructor not allowed.
     */
    private CredentialsUtils() {
    }

    /**
     * Compute the web service password - this is a combination of <code>password</code> plus <code>token</code>.
     *
     * @param password the password.
     * @param token    the security token.
     *
     * @return the web service password (combination of password plus token).
     */
    static String computeSecurityPassword(final String password, final String token) {
        StringUtils.ensureString(password, "Missing password!");
        StringUtils.ensureString(token, "Missing token!");

        return StringUtils.concat(password, token);
    }

    /**
     * Compute the web service password - this is a combination of credential's <code>password</code> plus <code>token</code>.
     *
     * @param credentials contains the password and token for computation..
     *
     * @return the web service password (combination of credential's password plus token).
     */
    static String computeSecurityPassword(final Credentials credentials) {
        ObjectUtils.ensureObject(credentials, "Must provide credentials!");

        return computeSecurityPassword(credentials.getPassword(), credentials.getToken());
    }

    /**
     * Ensure good credentials - must have all the pieces to be good.
     *
     * @param credentials the credentials to examine.
     * @param errorMsg    the error message to emit if any parts are invalid.
     *
     * @return <code>credentials</code> if all parts of credentials are not missing.
     *
     * @throws IllegalArgumentException if credentials is null or any part of it are missing.
     */
    public static Credentials ensureCredentials(final Credentials credentials, final String errorMsg) {
        ObjectUtils.ensureObject(credentials, errorMsg);

        StringUtils.ensureString(credentials.getApiVersion(), "Must provide an API version!");
        StringUtils.ensureString(credentials.getPassword(), "Must provide a password!");
        StringUtils.ensureString(credentials.getToken(), "Must provide a token!");
        StringUtils.ensureString(credentials.getUrl(), "Must provide a URL!");
        StringUtils.ensureString(credentials.getUserName(), "Must provide a user name!");

        return credentials;
    }

    /**
     * Ensure good credentials - must have all the pieces to be good.
     *
     * @param credentials the credentials to examine.
     *
     * @return <code>credentials</code> if all parts of credentials are not missing.
     *
     * @throws IllegalArgumentException if credentials is null or any part of it are missing.
     */
    public static Credentials ensureCredentials(final Credentials credentials) {
        return ensureCredentials(credentials, DEFAULT_ERROR_MESSAGE);
    }
}
