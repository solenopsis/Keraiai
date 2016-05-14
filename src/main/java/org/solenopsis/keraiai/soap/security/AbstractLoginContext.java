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
package org.solenopsis.keraiai.soap.security;

import java.util.logging.Logger;
import org.flossware.jcore.utils.ObjectUtils;
import org.solenopsis.keraiai.soap.credentials.Credentials;

/**
 * Abstract base class for logins which include the Credentials used.
 *
 * @author Scot P. Floess
 */
public abstract class AbstractLoginContext implements LoginContext {

    /**
     * Used for logging.
     */
    private final Logger logger;

    /**
     * Credentials for login.
     */
    private final Credentials credentials;

    /**
     * Constructor to set result of login, credentials used and the security manager who created self.
     *
     * @param credentials credentials for login.
     *
     * @throws IllegalArgumentException if credentials is null.
     */
    protected AbstractLoginContext(final Credentials credentials) {
        this.credentials = ObjectUtils.ensureObject(credentials, "Credentials must not be null");
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
     * {@inheritDoc}
     */
    @Override
    public Credentials getCredentials() {
        return credentials;
    }
}
