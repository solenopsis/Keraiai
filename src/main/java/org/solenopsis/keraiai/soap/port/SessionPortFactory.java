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
package org.solenopsis.keraiai.soap.port;

import java.net.URL;
import javax.xml.ws.Service;
import org.solenopsis.keraiai.SecurityMgr;

/**
 * Interface defining the API to create session based ports.
 *
 * @author Scot P. Floess
 */
public interface SessionPortFactory {

    /**
     * Create a session based port. This port will be able to perform auto logins, re-logins, etc.
     *
     * @param <S>         the type of web service being used.
     * @param <P>         the type of port desired.
     *
     * @param securityMgr used for logins and session ids.
     * @param service     contains the port for web service calls.
     * @param portType    the type of port to perform web service calls.
     *
     * @return a session based port.
     */
    public <S extends Service, P> P createSessionPort(final SecurityMgr securityMgr, final Service service, Class<P> portType);

    /**
     * Create a session based port. This port will be able to perform auto logins, re-logins, etc.
     *
     * @param <S>         the type of web service being used.
     * @param <P>         the type of port desired.
     *
     * @param securityMgr used for logins and session ids.
     * @param service     contains the port for web service calls.
     *
     * @return a session based port.
     */
    public <S extends Service, P> P createSessionPort(final SecurityMgr securityMgr, final S service);

    /**
     * Create a session based port. This port will be able to perform auto logins, re-logins, etc.
     *
     * @param <S>          the type of web service being used.
     * @param <P>          the type of port desired.
     *
     * @param securityMgr  used for logins and session ids.
     * @param serviceClass the class of the service where instances contains the port for web service calls.
     * @param wsdlResource the type of port to perform web service calls.
     *
     * @return a session based port.
     */
    public <S extends Service, P> P createSessionPort(final SecurityMgr securityMgr, final Class<S> serviceClass, final URL wsdlResource);

    /**
     * Create a session based port. This port will be able to perform auto logins, re-logins, etc.
     *
     * @param <S>          the type of web service being used.
     * @param <P>          the type of port desired.
     *
     * @param securityMgr  used for logins and session ids.
     * @param serviceClass the class of the service where instances contains the port for web service calls.
     * @param wsdlResource the type of port to perform web service calls.
     *
     * @return a session based port.
     */
    public <S extends Service, P> P createSessionPort(final SecurityMgr securityMgr, final Class<S> serviceClass, final String wsdlResource);
}
