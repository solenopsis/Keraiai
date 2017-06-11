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
package org.solenopsis.keraiai.soap;

import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.LoginContext;

/**
 * Represents all login SOAP web service: enterprise, partner and tooling. Additionally provides the ability to create a usable
 * login port for logins.
 *
 * @author Scot P. Floess
 */
public interface LoginWebService {
    /**
     * Return the login web service type.
     *
     * @return the login web service type.
     */
    ApiWebService getApiWebService();

    LoginContext login(Credentials credentials);

    void logout(LoginContext loginContext);
}