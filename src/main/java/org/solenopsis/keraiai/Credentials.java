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
package org.solenopsis.keraiai;

/**
 *
 * Defines SFDC credentials.
 *
 * @author sfloess
 *
 */
public interface Credentials {

    /**
     * Return the server url for login.
     *
     * @return the server url for login.
     */
    String getUrl();

    /**
     * Return the user name.
     *
     * @return the user name.
     */
    String getUserName();

    /**
     * Return the password.
     *
     * @return the password.
     */
    String getPassword();

    /**
     * Return the token.
     *
     * @return the token.
     */
    String getToken();

    /**
     * Return the computed security password (password plus token).
     *
     * @return the computed security password.
     */
    String getSecurityPassword();

    /**
     * Return the API version.
     *
     * @return the API version.
     */
    String getApiVersion();
}
