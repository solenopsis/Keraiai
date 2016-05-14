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

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;
import org.flossware.jcore.utils.soap.SoapUtils;

/**
 * Manages the SOAP header for the session id.
 *
 * @author sfloess
 */
class SessionIdSoapHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    /**
     * Our logger.
     */
    private final Logger logger;

    /**
     * Name where we will place the session id element.
     */
    private final QName sessionHeaderName;

    /**
     * The actual session id.
     */
    private final String sessionId;

    /**
     * This constructor sets the name in the soap header for the session id and the session id itself.
     *
     * @param sessionHeaderName the name that will contain the session id element.
     * @param sessionId the actual session id.
     *
     * @throws IllegalArgumentException if sessionHeaderName is null or if sessionId is null/empty/blank.
     */
    SessionIdSoapHeaderHandler(final QName sessionHeaderName, final String sessionId) {
        this.sessionHeaderName = ObjectUtils.ensureObject(sessionHeaderName, "Must provide a session header QName!");
        this.sessionId = StringUtils.ensureString(sessionId, "Must provide a session Id!");

        this.logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Return the logger.
     */
    private Logger getLogger() {
        return logger;
    }

    /**
     * Return the name that will contain the session id element.
     *
     * @return the name that will contain the session id element.
     */
    QName getSessionHeaderName() {
        return sessionHeaderName;
    }

    /**
     * Return the actual session id.
     *
     * @return the actual session id.
     */
    String getSessionId() {
        return sessionId;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Set getHeaders() {
        return null;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean handleMessage(SOAPMessageContext msgContext) {
        //if this is a request, true for outbound messages, false for inbound
        if (!SoapUtils.isRequest(msgContext)) {
            return true;
        }

        try {
            SessionIdUtils.setSessionId(SoapUtils.getSoapHeader(msgContext), getSessionHeaderName(), getSessionId());
            msgContext.getMessage().saveChanges();
        } catch (final SOAPException soapException) {
            getLogger().log(Level.SEVERE, "Problem handling msg", soapException);
        }

        return true;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean handleFault(SOAPMessageContext msgContext) {
        return true;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void close(final MessageContext msgContext) {
    }
}
