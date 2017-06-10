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
 * Computes a port names. For an API port (enterprise, parter, tooling, or metadata) it is the version found in credentials. For
 * custom web services, it is the name of the web service class.
 *
 * @author Scot P. Floess
 */
interface SessionPortNameFactory {
    /**
     * Creates API port names.
     */
    SessionPortNameFactory API_PORT_NAME_FACTORY = new ApiSessionPortNameFactory();

    /**
     * Creates custom port names.
     */
    SessionPortNameFactory CUSTOM_PORT_NAME_FACTORY = new CustomSessionPortNameFactory();

    /**
     * Computes a port name from the <code>credentials</code> and <code>service</code>. If dealing with an API webservice, the value
     * of the version found in credentials will be used. However, for custom web services, it is the class name for the web service.
     *
     * @param credentials the login credentials (which contain an API version). Will be used in computing API port names.
     * @param service     the service for whom we want a port name. Will be used when computing custom web service port names.
     *
     * @return the port name.
     */
    String computePortName(Credentials credentials, Service service);

    /**
     * Computes the port name based upon <code>loginContext</code> and <code>service</code>. If dealing with an API webservice, the
     * value of the version found in the login context's credentials will be used. However, for custom web services, it is the class
     * name for the web service.
     *
     * @param loginContext contains the login credentials (which contain an PAI version). Will be used in compute API port names.
     * @param service      the service for whom we want a port name. Will be used when computing custom web service port names.
     *
     * @return the port name.
     */
    String computePortName(LoginContext loginContext, Service service);
}
