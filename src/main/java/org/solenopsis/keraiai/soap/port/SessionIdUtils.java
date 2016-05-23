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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import org.flossware.jcore.utils.LoggerUtils;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;

/**
 * Session ID utility class.
 *
 * @author sfloess
 */
class SessionIdUtils {

    /**
     * Used for logging.
     */
    private static final Logger logger = Logger.getLogger(SessionIdUtils.class.getName());

    /**
     * Return our logger.
     *
     * @return our logger.
     */
    private static Logger getLogger() {
        return logger;
    }

    /**
     * When setting up the soap header, we need to set the session header using this attribute.
     */
    static final String SESSION_HEADER = "SessionHeader";

    /**
     * This is the session id on the session header.
     */
    static final String SESSION_ID = "sessionId";

    /**
     * Using service, create a QName for the SOAP session header.
     *
     * @param service the service for whom we desire a QName for the session header.
     *
     * @return a QName for the session header.
     *
     * @throws IllegalArgumentException if service is null.
     */
    static QName computeSessionHeaderName(final Service service) {
        return new QName(ObjectUtils.ensureObject(service, "Must provide a service!").getServiceName().getNamespaceURI(), SESSION_HEADER);
    }

    /**
     * Set the session id on the soap header.
     *
     * @param soapHeader the header in which to set the session id.
     * @param sessionHeaderName the header name.
     * @param sessionId the session id.
     *
     * @throws SOAPException if any problems arise setting the session id.
     * @throws IllegalArgument exception if soapHeader/sessionHeaderName are null or if sessionId is null/empty.
     */
    static void setSessionId(final SOAPHeader soapHeader, final QName sessionHeaderName, final String sessionId) throws SOAPException {
        ObjectUtils.ensureObject(soapHeader, "Must provide a soap header!");
        ObjectUtils.ensureObject(sessionHeaderName, "Must provide a QName!");
        StringUtils.ensureString(sessionId, "Must provide a session id");

        LoggerUtils.log(getLogger(), Level.FINEST, "Setting session id [{0}] for QName [{1}] in SOAP header {3}", sessionId, sessionHeaderName, soapHeader);

        soapHeader.addChildElement(sessionHeaderName).addChildElement(SESSION_ID).addTextNode(sessionId);
    }

    /**
     * Set the session no the service using the service's namespace URI as a QName.
     *
     * @param <P> the type of port for the service.
     *
     * @param service the web service.
     * @param port the port for web service calls.
     * @param sessionId the session id to set.
     *
     * @return the port with the session id set.
     *
     * @throws IllegalArgumentException if service/port are null or if session id is null
     */
    static <P> P setSessionId(final Service service, final P port, final String sessionId) {
        ObjectUtils.ensureObject(service, "Must provide a service!");
        ObjectUtils.ensureObject(port, "Must provide a port!");
        StringUtils.ensureString(sessionId, "Must provide a session id");

        final List<Handler> handlerChain = new ArrayList<>();
        handlerChain.add(new SessionIdSoapHeaderHandler(computeSessionHeaderName(service), sessionId));

        ((BindingProvider) port).getBinding().setHandlerChain(handlerChain);

        LoggerUtils.log(getLogger(), Level.FINEST, "Setting session id [{0}] for Service [{1}] and Port [{2}]", sessionId, service, port);

        return port;
    }

    /**
     * Default constructor not allowed.
     */
    private SessionIdUtils() {
    }
}
