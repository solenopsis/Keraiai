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
package org.solenopsis.keraiai.soap.login;

import org.flossware.jcore.utils.StringUtils;
import org.flossware.jcore.utils.net.UrlUtils;
import org.solenopsis.keraiai.*;

/**
 * Non web service specific login result. Same information as one will receive from the enterprise, partner and tooling web
 * services.
 *
 * @author Scot P. Floess
 */
final class DefaultLoginContext implements LoginContext {
    private final String metadataServerUrl;

    private final boolean isPasswordExpired;

    private final boolean isSandbox;

    private final String serverUrl;

    private final String baseServerUrl;

    private final String sessionId;

    private final String userId;

    private final Credentials credentials;

    DefaultLoginContext(final String metadataServerUrl, final boolean isPasswordExpired, final boolean isSandbox, final String serverUrl, final String sessionId, final String userId, final Credentials credentials) {
        this.metadataServerUrl = metadataServerUrl;
        this.isPasswordExpired = isPasswordExpired;
        this.isSandbox = isSandbox;
        this.serverUrl = serverUrl;
        this.baseServerUrl = UrlUtils.computeProtocolAndHostString(StringUtils.ensureString(serverUrl, "Must provide a server url!"));
        this.sessionId = sessionId;
        this.userId = userId;
        this.credentials = credentials;
    }

    DefaultLoginContext(final org.solenopsis.keraiai.wsdl.enterprise.LoginResult loginResult, final Credentials credentials) {
        this(loginResult.getMetadataServerUrl(), loginResult.isPasswordExpired(), loginResult.isSandbox(), loginResult.getServerUrl(), loginResult.getSessionId(), loginResult.getUserId(), credentials);
    }

    DefaultLoginContext(final org.solenopsis.keraiai.wsdl.partner.LoginResult loginResult, final Credentials credentials) {
        this(loginResult.getMetadataServerUrl(), loginResult.isPasswordExpired(), loginResult.isSandbox(), loginResult.getServerUrl(), loginResult.getSessionId(), loginResult.getUserId(), credentials);
    }

    DefaultLoginContext(final org.solenopsis.keraiai.wsdl.tooling.LoginResult loginResult, final Credentials credentials) {
        this(loginResult.getMetadataServerUrl(), loginResult.isPasswordExpired(), loginResult.isSandbox(), loginResult.getServerUrl(), loginResult.getSessionId(), loginResult.getUserId(), credentials);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMetadataServerUrl() {
        return metadataServerUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPasswordExpired() {
        return isPasswordExpired;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSandbox() {
        return isSandbox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBaseServerUrl() {
        return baseServerUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials getCredentials() {
        return credentials;
    }
}
