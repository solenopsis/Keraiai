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
import org.solenopsis.keraiai.wsdl.tooling.SforceServicePortType;

/**
 * Implementation using the tooling web service.
 *
 * @author Scot P. Floess
 */
public class ToolingLoginMgr implements LoginMgr {
    @Override
    public LoginContext login(Object port, Credentials credentials) {
        try {
            return new DefaultLoginContext(((SforceServicePortType) port).login(credentials.getUserName(), credentials.getSecurityPassword()), credentials);
        } catch (final Throwable t) {
            throw new LoginException(t);
        }
    }

    @Override
    public void logout(Object port) {
        try {
            ((SforceServicePortType) port).logout();
        } catch (final Throwable t) {
            throw new LogoutException(t);
        }
    }
}
