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
package org.solenopsis.keraiai.soap.credentials;

import org.flossware.jcore.utils.StringUtils;

/**
 * Credential related utilities.
 *
 * @author sfloess
 *
 */
class CredentialsUtils {

    /**
     * Compute the web service password - this is a combination of <code>password</code> plus <code>token</code>.
     *
     * @param password the password.
     * @param token the security token.
     *
     * @return the web service password (combination of password plus token).
     */
    static String computeSecurityPassword(final String password, final String token) {
        return StringUtils.ensureString(password, "Missing password!") + StringUtils.ensureString(token, "Missing token!");
    }

    /**
     * Compute the web service password - this is a combination of credential's <code>password</code> plus <code>token</code>.
     *
     * @param credentials contains the password and token for computation..
     *
     * @return the web service password (combination of credential's password plus token).
     */
    static String computeSecurityPassword(final Credentials credentials) {
        return computeSecurityPassword(credentials.getPassword(), credentials.getToken());
    }

    /**
     * Default constructor not allowed.
     */
    private CredentialsUtils() {
    }
}
