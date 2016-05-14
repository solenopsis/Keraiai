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
package org.solenopsis.keraiai.soap.port.session;

import java.net.URL;
import javax.xml.ws.Service;
import org.solenopsis.keraiai.soap.WebServiceTypeEnum;
import org.solenopsis.keraiai.soap.security.SecurityMgr;

/**
 * Defines the interface to create ports on web services. Proxy ports can also be created that manage auto logins, auto relogins,
 * and call retries.
 *
 * A note about the name params below: stock SFDC web service like the apex, enterprise, metadata, partner and tooling use the API
 * version in their name as the binding URL. As an example: https://cs9.salesforce.com/services/Soap/s/31.0. Custom web services use
 * the name of the Apex class exposed as a web service. This can be found in the wsimport generated Service extended class (from the
 * exported web service) via the getter methods that return the port (annotated with @WebEndpoint and specifically the name
 * attribute).
 *
 * @author sfloess
 */
public interface SessionPortFactory {

    /**
     * Return the security manager being used.
     *
     * @return the security manager being used.
     */
    SecurityMgr getSecurityMgr();

    /**
     * Creates a fully functional port that will have a session id placed in the session header.
     *
     * @param <P> the type of port to return.
     *
     * @param webServiceType denotes the type of web service.
     * @param service the actual service who can create ports.
     *
     * @return a functioning port.
     */
    <P> P createSessionPort(WebServiceTypeEnum webServiceType, Service service);

    /**
     * Creates a fully functional port that will have a session id placed in the session header..
     *
     * @param <S> the service that can create ports.
     * @param <P> the type of port to return.
     *
     * @param webServiceType denotes the type of web service.
     * @param serviceClass the class of the service that can create ports.
     * @param wsdlResource the URL to the WSDL for the service.
     *
     * @return a functioning port.
     */
    <S extends Service, P> P createSessionPort(WebServiceTypeEnum webServiceType, Class<S> serviceClass, URL wsdlResource);

    /**
     * Creates a fully functional port.
     *
     * @param <S> the service that can create ports.
     * @param <P> the type of port to return.
     *
     * @param webServiceType denotes the type of web service.
     * @param serviceClass the class of the service that can create ports.
     * @param wsdlResource the string representation of a URL to the WSDL for the service.
     *
     * @return a functioning port.
     */
    <S extends Service, P> P createSessionPort(WebServiceTypeEnum webServiceType, Class<S> serviceClass, String wsdlResource);
}
