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
 * Non web service specific login result. Same information as one will receive from the enterprise, partner and tooling web
 * services.
 *
 * @author Scot P. Floess
 */
public interface LoginContext {

    /**
     * The metadata server's URL.
     *
     * @return the metadata server's URL.
     */
    String getMetadataServerUrl();

    /**
     * Return true if the password expired or false if not.
     *
     * @return true if the password expired or false if not.
     */
    boolean isPasswordExpired();

    /**
     * Return true if this is a sandbox or false if not.
     *
     * @return true if a sandbox or false if not.
     */
    boolean isSandbox();

    /**
     * The server's real URL.
     *
     * @return the server's real URL.
     */
    String getServerUrl();

    /**
     * The server's real URL just including host and protocol.
     *
     * @return the server's real URL just including host and protocol.
     */
    String getBaseServerUrl();

    /**
     * The session ID to use.
     *
     * @return the session id.
     */
    String getSessionId();

    /**
     * The user id.
     *
     * @return the user id.
     */
    String getUserId();

    /**
     * The credentials used for login.
     *
     * @return the credentials used for login.
     */
    Credentials getCredentials();
}
