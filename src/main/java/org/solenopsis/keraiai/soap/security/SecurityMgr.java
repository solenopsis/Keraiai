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

import org.solenopsis.keraiai.soap.credentials.Credentials;

/**
 * Represents a way to login and out of SFDC for the enterprise, partner and tooling web services. Additionally, maintains a session
 * that can be shared (session id).
 *
 * @author Scot P. Floess
 */
public interface SecurityMgr {

    /**
     * Return the credentials.
     *
     * @return the credentials.
     */
    Credentials getCredentials();

    /**
     * Return a session based login context. A login will be called if one doesn't exist.
     *
     * @return the session based login context.
     */
    LoginContext getSession();

    /**
     * Reset a session.
     *
     * @param loginContext is the login that was being used as part of the session.
     *
     * @return the a new session based login context.
     */
    LoginContext resetSession(LoginContext loginContext);

    /**
     * Request a new login.
     *
     * @return a login result from SFDC.
     */
    LoginContext login();

    /**
     * Force a logout.
     */
    void logout();
}
