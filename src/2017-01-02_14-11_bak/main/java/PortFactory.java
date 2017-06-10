/*
 * Copyright (C) 2016 Scot P. Floess
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
package bak;

import java.net.URL;
import javax.xml.ws.Service;
import org.solenopsis.keraiai.SecurityMgr;
import org.solenopsis.keraiai.soap.port.WebServiceTypeEnum;

/**
 * Defines the API to create web service ports.
 *
 * @author Scot P. Floess
 */
public interface PortFactory {

    /**
     * Return the security manager being used.
     *
     * @return the security manager being used.
     */
    SecurityMgr getSecurityMgr();

    /**
     * Create a session based port (one that honors session ids, auto logins, and retries).
     *
     * @param <P>            the type of port to create/return.
     *
     * @param webServiceType the type of web service desired.
     * @param service        the actual web service for whom a port is exposed.
     *
     * @return a fully functional port to call.
     */
    <P> P createSessionPort(WebServiceTypeEnum webServiceType, Service service);

    /**
     * Create a session based port (one that honors session ids, auto logins, and retries).
     *
     * @param <S>            the type of web service containing a desired port.
     * @param <P>            the type of port to call upon the web service.
     *
     * @param webServiceType the type of web service.
     * @param serviceClass   the class of the web service.
     * @param wsdlResource   the resource containing the WSDL for the web service.
     *
     * @return a fully functional port to call.
     */
    <S extends Service, P> P createSessionPort(WebServiceTypeEnum webServiceType, Class<S> serviceClass, URL wsdlResource);

    /**
     * Create a session based port (one that honors session ids, auto logins, and retries).
     *
     * @param <S>            the type of web service containing a desired port.
     * @param <P>            the type of port to call upon the web service.
     *
     * @param webServiceType the type of web service.
     * @param serviceClass   the class of the web service.
     * @param wsdlResource   the resource containing the WSDL for the web service.
     *
     * @return a fully functional port to call.
     */
    <S extends Service, P> P createSessionPort(WebServiceTypeEnum webServiceType, Class<S> serviceClass, String wsdlResource);
}
