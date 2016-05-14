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

import java.util.Properties;

/**
 *
 * Uses properties to populate the credentials.
 *
 * @author sfloess
 *
 */
public class PropertiesCredentials extends StringCredentials {

    /**
     * Uses properties to get the credentials.
     *
     * @param properties contains the credentials.
     * @param urlProperty the name of the property containing URL.
     * @param userNameProperty the name of the property containing user name.
     * @param passwordProperty the name of the property containing password.
     * @param tokenProperty the name of the property containing token.
     * @param apiVersionProperty the name of the property containing API.
     */
    public PropertiesCredentials(final Properties properties, final String urlProperty, final String userNameProperty, final String passwordProperty, final String tokenProperty, final String apiVersionProperty) {
        super(
                properties.getProperty(urlProperty),
                properties.getProperty(userNameProperty),
                properties.getProperty(passwordProperty),
                properties.getProperty(tokenProperty),
                properties.getProperty(apiVersionProperty)
        );
    }

    /**
     * Constructs credentials from properties.
     *
     * @param properties contains properties from which our credentials will be retrieved.
     */
    public PropertiesCredentials(final Properties properties) {
        this(
                properties,
                CredentialsEnum.URL.getName(),
                CredentialsEnum.USER_NAME.getName(),
                CredentialsEnum.PASSWORD.getName(),
                CredentialsEnum.TOKEN.getName(),
                CredentialsEnum.API_VERSION.getName()
        );
    }
}
