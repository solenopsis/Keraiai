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

import org.solenopsis.keraiai.SecurityMgr;

/**
 * Interface defining the API to create login based ports.
 *
 * @author Scot P. Floess
 */
public interface LoginPortFactory {

    /**
     * Creates a login port to an SFDC web service (either enterprise, partner or tooling).
     *
     * @param <P>         the type of port being created.
     *
     * @param securityMgr contains credentials whose "base" url and the API version used to construct the URL.
     *
     * @return a usable login port.
     */
    public <P> P createLoginPort(final SecurityMgr securityMgr);
}
