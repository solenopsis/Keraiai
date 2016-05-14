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

import java.util.logging.Logger;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.ObjectUtils;
import org.solenopsis.keraiai.soap.WebServiceTypeEnum;

/**
 * Abstract base class for create ports. We will use two type of ports: one that is simply a straight on port and one that is for
 * sessions.
 *
 * Ports consist of a server URL plus a partial URL as denoted in WebServiceTypeEnum and a service name. For API based web services
 * like the Enterprise or Metadata it is an API version whereas Custom web services use the Apex class name (which can be
 * extrapolated from the Port getter method on a wsimport generated web service).
 *
 * @author Scot P. Floess
 */
public abstract class AbstractPortFactory {

    /**
     * Used for logging.
     */
    private final Logger logger;

    /**
     * Default constructor.
     */
    protected AbstractPortFactory() {
        this.logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    protected final Logger getLogger() {
        return logger;
    }

    /**
     * Create a usable web service port.
     *
     * @param <P> the port type desired.
     *
     * @param webServiceType the type of web service.
     * @param serverUrl the SFDC URL to communicate with on web service calls.
     * @param service the actual Service subclass.
     * @param portType the port type for the service.
     * @param name the name of the web service. For API based web services it will represent that number whereas for Custom web
     * services it is the name of the Apex class.
     *
     * @return a usable port to call on a web service.
     */
    protected <P> P createPort(WebServiceTypeEnum webServiceType, final String serverUrl, Service service, Class<P> portType, String name) {
        return UrlUtils.setUrl(ObjectUtils.ensureObject(service, "Must present a service").getPort(portType), serverUrl, webServiceType.getPartialUrl(), name);
    }

    /**
     * Create a port that has the session id in the SOAP header and is ready to use.
     *
     * @param <P> the port type desired.
     *
     * @param webServiceType the type of web service.
     * @param serverUrl the SFDC URL to communicate with on web service calls.
     * @param service the actual Service subclass.
     * @param portType the port type for the service.
     * @param name the name of the web service. For API based web services it will represent that number whereas for Custom web
     * services it is the name of the Apex class.
     * @param sessionId the session id needed to call the Salesforce web service.
     *
     * @return a usable port to call on a web service that has <code>sessionId</code> in the SOAP header.
     */
    protected <P> P createSessionPort(WebServiceTypeEnum webServiceType, final String serverUrl, final Service service, final Class<P> portType, final String name, final String sessionId) {
        return (P) SessionIdUtils.setSessionId(service, createPort(webServiceType, serverUrl, service, portType, name), sessionId);
    }
}
