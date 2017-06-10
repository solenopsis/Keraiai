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
package org.solenopsis.keraiai.soap;

import javax.xml.ws.Service;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.LoginContext;

/**
 * Computes a session URL.
 *
 * @author Scot P. Floess
 */
public interface SessionUrlFactory {
    /**
     * Will compute a session URL.
     *
     * @param credentials contains the URL for whom we want a URL.
     * @param service     the service for whom we want a session URL.
     *
     * @return a session url.
     */
    String computeUrl(Credentials credentials, Service service);

    /**
     * Will compute a session URL.
     *
     * @param loginContext our session login data.
     * @param service      the service for whom we want a session URL.
     *
     * @return a session url.
     */
    String computeSessionUrl(LoginContext loginContext, Service service);
}
