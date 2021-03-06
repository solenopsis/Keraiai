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
import org.solenopsis.keraiai.Credentials;

/**
 * This interface denotes the built in API SFDC web services.
 *
 * @author Scot P. Floess
 */
public interface ApiWebService {
    /**
     * Return the web service type.
     *
     * @return the web service.
     */
    WebServiceType getWebServiceType();

    /**
     * Return the SFDC web service.
     *
     * @return the SFDC web service.
     */
    Service getService();

    /**
     * Return the port for the web service.
     *
     * @return the port for the web service.
     */
    Class getPortType();

    /**
     * Will create a proxy port using the API services included in Keraiai.
     *
     * @param <P>             the type of port to create.
     *
     * @param credentials     are the credentials to use when creating the API proxy port.
     * @param loginWebService used for logins and session ids.
     *
     * @return a proxy port
     */
    <P> P createProxyPort(Credentials credentials, LoginWebService loginWebService);

    /**
     * Will create a proxy port using the API services included in Keraiai.
     *
     * @param <P>         the type of port to create.
     *
     * @param credentials are the credentials to use when creating the API proxy port.
     *
     * @return a proxy port
     */
    <P> P createProxyPort(Credentials credentials);
}
