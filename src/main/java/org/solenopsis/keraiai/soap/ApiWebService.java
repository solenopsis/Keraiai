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

import javax.xml.ws.Service;

/**
 * This interface denotes the built in API SFDC web services.
 *
 * @author Scot P. Floess
 */
public interface ApiWebService {
    /**
     * Return the SFDC web service.
     *
     * @return the SFDC web service.
     */
    public Service getService();

    /**
     * Return the port for the web service.
     *
     * @return the port for the web service.
     */
    public Class getPortType();
}
