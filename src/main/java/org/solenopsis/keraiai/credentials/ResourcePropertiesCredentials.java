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
package org.solenopsis.keraiai.credentials;

import org.flossware.jcore.utils.PropertiesUtils;

/**
 * Uses a resource to populate the credentials.
 *
 * @author sfloess
 */
public class ResourcePropertiesCredentials extends PropertiesCredentials {
    /**
     * Uses properties to get the credentials.
     *
     * @param resource           the resource containing properties.
     * @param urlProperty        the name of the property containing URL.
     * @param userNameProperty   the name of the property containing user name.
     * @param passwordProperty   the name of the property containing password.
     * @param tokenProperty      the name of the property containing token.
     * @param apiVersionProperty the name of the property containing API.
     */
    public ResourcePropertiesCredentials(final String resource, final String urlProperty, final String userNameProperty, final String passwordProperty, final String tokenProperty, final String apiVersionProperty) {
        super(PropertiesUtils.createProperties(resource), urlProperty, userNameProperty, passwordProperty, tokenProperty, apiVersionProperty);
    }

    /**
     * Constructs credentials from properties.
     *
     * @param resource the resource containing properties.
     */
    public ResourcePropertiesCredentials(final String resource) {
        super(PropertiesUtils.createProperties(resource));
    }
}
