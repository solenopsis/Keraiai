/*
 * Copyright (C) 2017 Scot P. Floess
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
package org.solenopsis.keraiai.soap.session;

import javax.xml.ws.Service;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.LoginContext;

/**
 * Computes a port name.
 *
 * @author Scot P. Floess
 */
final class ApiSessionPortNameFactory implements SessionPortNameFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public String computePortName(final Credentials credentials, final Service service) {
        return credentials.getApiVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String computePortName(final LoginContext loginContext, final Service service) {
        return computePortName(loginContext.getCredentials(), service);
    }
}
