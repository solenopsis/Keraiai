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
package org.solenopsis.keraiai.soap.credentials;

import org.flossware.jcore.AbstractCommonBase;

/**
 * Abstract base class for credentials.
 *
 * @author sfloess
 */
public abstract class AbstractCredentials extends AbstractCommonBase implements Credentials {

    /**
     * Tests a credentials object for equality.
     *
     * @param credentials to compare self to.
     *
     * @return true if same reference as self or members are equal.
     */
    boolean equalsCredentials(final Credentials credentials) {
        return null == credentials ? false
                : this == credentials ? true
                        : getUserName().equals(credentials.getUserName())
                        && getPassword().equals(credentials.getPassword())
                        && getToken().equals(credentials.getToken())
                        && getUrl().equals(credentials.getUrl())
                        && getApiVersion().equals(credentials.getApiVersion());
    }

    /**
     * Default constructor.
     */
    protected AbstractCredentials() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        return object instanceof Credentials && hashCode() == object.hashCode() ? equalsCredentials((Credentials) object) : false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getUserName().hashCode()
                + getPassword().hashCode()
                + getToken().hashCode()
                + getUrl().hashCode()
                + getApiVersion().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSecurityPassword() {
        return CredentialsUtils.computeSecurityPassword(getPassword(), getToken());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("apiVersion       [").append(getApiVersion()).append("]\n");
        sb.append("password         [").append(getPassword()).append("]\n");
        sb.append("securityPassword [").append(getSecurityPassword()).append("]\n");
        sb.append("token            [").append(getToken()).append("]\n");
        sb.append("url              [").append(getUrl()).append("]\n");
        sb.append("userName         [").append(getUserName()).append("]");

        return sb.toString();
    }
}
