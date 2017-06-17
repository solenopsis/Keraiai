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
package org.solenopsis.keraiai.soap.session;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.flossware.jcore.soap.AbstractSoapRequestHeaderHandler;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;
import org.solenopsis.keraiai.soap.utils.SalesforceSessionHeaderUtils;

/**
 * Manages the SOAP header for the session id.
 *
 * @author sfloess
 */
final class SessionIdSoapRequestHeaderHandler extends AbstractSoapRequestHeaderHandler {
    /**
     * This is the session id on the session header.
     */
    static final String SESSION_ID = "sessionId";

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
     * @param sessionId         the actual session id.
     * @param sessionHeaderName the name that will contain the session id element.
     *
     * @throws IllegalArgumentException if sessionHeaderName is null or if sessionId is null/empty/blank.
     */
    SessionIdSoapRequestHeaderHandler(final QName sessionHeaderName, final String sessionId) {
        this.sessionHeaderName = ObjectUtils.ensureObject(sessionHeaderName, "Must provide a session header QName!");
        this.sessionId = StringUtils.ensureString(sessionId, "Must provide a session id!");
    }

    /**
     * Return the name that will contain the session id element.
     *
     * @return the name that will contain the session id element.
     */
    @Override
    protected QName getHeaderName() {
        return sessionHeaderName;
    }

    /**
     * Our name is SESSION_ID.
     *
     * @return SESSION_ID;
     */
    @Override
    protected String getName() {
        return SESSION_ID;
    }

    /**
     * Our value is the session id.
     *
     * @return the actual session id.
     */
    @Override
    protected String getValue() {
        return sessionId;
    }

    /**
     * This constructor gets the session id from <code>securityMgr</code> and the QName from the <code>service</code> needed to make
     * web service calls.
     *
     * @param service   contains the QName needed for the SOAP header.
     * @param sessionId the session id for the SFDC SOAP call.
     */
    public SessionIdSoapRequestHeaderHandler(final Service service, final String sessionId) {
        this(SalesforceSessionHeaderUtils.computeSessionHeaderNameForService(service), sessionId);
    }
}
