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
package org.solenopsis.keraiai.soap.session;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.LoggerUtils;
import org.flossware.jcore.utils.soap.SoapUtils;
import org.solenopsis.keraiai.LoginContext;
import org.solenopsis.keraiai.soap.ApiWebService;
import org.solenopsis.keraiai.soap.WebServiceType;

/**
 * Utility class for port functionality.
 *
 * @author Scot P. Floess
 */
public final class SessionPortFactory {
    /**
     * Our logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SessionPortFactory.class.getName());

    /**
     * Return the LOGGER.
     */
    private static Logger getLogger() {
        return LOGGER;
    }

    /**
     * Create a usable session port including a URL and session id.
     *
     * @param <P>       the type of session port to create.
     *
     * @param url       the URL for the port.
     * @param sessionId the session id to use for the SOAP header.
     * @param service   the service to call.
     * @param portType  used to retrieve a port from the service.
     *
     * @return a usable port that has session id and URL set.
     */
    public static <P> P createSessionPort(final String url, final String sessionId, final Service service, final Class portType) {
        final P port = SoapUtils.createPort(service, portType, url, new SessionIdSoapRequestHeaderHandler(service, sessionId));

        LoggerUtils.log(getLogger(), Level.FINE, "Session Port = [{0}]", port);

        return port;
    }

    /**
     * Create a usable session port - the session id an url are pulled from <code>loginContext</code>.
     *
     * @param <P>            the type of session port to create.
     *
     * @param webServiceType the type of web service that will be called..
     * @param loginContext   a previously log in - contains the URL and session id to make the web service call.
     * @param service        the service to call.
     * @param portType       used to retrieve a port from the service.
     *
     * @return a usable port that has session id and URL set.
     */
    public static <P> P createSessionPort(final WebServiceType webServiceType, final LoginContext loginContext, final Service service, final Class portType) {
        return createSessionPort(webServiceType.getSessionUrlFactory().computeSessionUrl(loginContext, service), loginContext.getSessionId(), service, portType);
    }

    /**
     * Create a usable session port - the session id an url are pulled from <code>loginContext</code>.
     *
     * @param <P>           the type of session port to create.
     *
     * @param apiWebService one of the built in SFDC web service types.
     * @param loginContext  a previously log in - contains the URL and session id to make the web service call.
     *
     * @return a usable port that has session id and URL set.
     */
    public static <P> P createSessionPort(final ApiWebService apiWebService, final LoginContext loginContext) {
        return createSessionPort(apiWebService.getWebServiceType(), loginContext, apiWebService.getService(), apiWebService.getPortType());
    }

    /**
     * Default constructor not allowed.
     */
    private SessionPortFactory() {
    }
}
