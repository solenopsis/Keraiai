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
package org.solenopsis.keraiai.soap.login;

import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.LoginContext;

/**
 * Represents a way to login and out of SFDC for the enterprise, partner and tooling web services. Additionally, maintains a session
 * that can be shared (session id).
 *
 * @author Scot P. Floess
 */
interface LoginMgr {
    /**
     * The enterprise login manager.
     */
    LoginMgr ENTERPRISE_LOGIN_MGR = new EnterpriseLoginMgr();

    /**
     * The partner login manager.
     */
    LoginMgr PARTNER_LOGIN_MGR = new PartnerLoginMgr();

    /**
     * The tooling login manager.
     */
    LoginMgr TOOLING_LOGIN_MGR = new ToolingLoginMgr();

    /**
     * Request a new login.
     *
     * @param port        the port we will call for login.
     * @param credentials the credentials to use for login - this includes the url to call, the API version, username and
     *                    password/token.
     *
     * @return a login result from SFDC.
     */
    LoginContext login(Object port, Credentials credentials);

    /**
     * Force a logout.
     */
    void logout(Object port);
}
