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

/**
 * For properties the property name for each credential. For XML, the name of an element.
 *
 * @author Scot P. Floess
 */
enum PropertiesCredentialsEnum {
    URL("url"),
    USER_NAME("username"),
    PASSWORD("password"),
    TOKEN("token"),
    API_VERSION("apiVersion");

    /**
     * The name for the property or XML element.
     */
    private final String name;

    /**
     * This constructor sets the name of the property or XML element.
     *
     * @param name the name of the property or XML element.
     */
    private PropertiesCredentialsEnum(final String name) {
        this.name = name;
    }

    /**
     * Return the name of the property or XML element.
     *
     * @return the name of the property or XML element.
     */
    public String getName() {
        return name;
    }
}
