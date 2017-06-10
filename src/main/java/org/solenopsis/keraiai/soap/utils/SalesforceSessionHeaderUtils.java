/*
 * Copyright (C) 2016 Scot P. Floess
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
package org.solenopsis.keraiai.soap.utils;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;

/**
 * Utility class for port functionality.
 *
 * @author Scot P. Floess
 */
public final class SalesforceSessionHeaderUtils {
    /**
     * When setting up the soap header, we need to set the session header using this attribute.
     */
    public static final String SESSION_HEADER = "SessionHeader";

    /**
     * Compuates the QName for the SOAP session header from the <code>namespaceUri</code>.
     *
     * @param namespaceUri this is the namespace for the URI of the session header.
     *
     * @return a QName for the session header.
     *
     * @throws IllegalArgumentException if namespaceUri is blank or null.
     */
    public static QName computeSessionHeaderNameForNamespace(final String namespaceUri) {
        StringUtils.ensureString(namespaceUri, "Must provide a namespace URI!");

        return new QName(namespaceUri, SESSION_HEADER);
    }

    /**
     * Using service, create a QName for the SOAP session header.
     *
     * @param serviceName the QName for the session header.
     *
     * @return a QName for the session header.
     *
     * @throws IllegalArgumentException if serviceName is null.
     */
    public static QName computeSessionHeaderNameForQname(final QName serviceName) {
        ObjectUtils.ensureObject(serviceName, "Must provide a service QName!");

        return computeSessionHeaderNameForNamespace(serviceName.getNamespaceURI());
    }

    /**
     * Using service, create a QName for the SOAP session header.
     *
     * @param service the service for whom we desire a QName for the session header.
     *
     * @return a QName for the session header.
     *
     * @throws IllegalArgumentException if service is null.
     */
    public static QName computeSessionHeaderNameForService(final Service service) {
        ObjectUtils.ensureObject(service, "Must provide a service!");

        return computeSessionHeaderNameForQname(service.getServiceName());
    }

    /**
     * Default constructor not allowed.
     */
    private SalesforceSessionHeaderUtils() {
    }
}
