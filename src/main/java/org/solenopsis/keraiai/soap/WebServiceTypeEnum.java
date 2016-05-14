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
package org.solenopsis.keraiai.soap;

/**
 * Denotes an SFDC web service type, and the partial URL for one needs when calling an SFDC web service:
 * <ul>
 * <li>Apex = services/Soap/s/</li>
 * <li>Custom = services/Soap/class/</li>
 * <li>Enterprise = services/Soap/c/</li>
 * <li>Metadata = services/Soap/m/</li>
 * <li>Partner = services/Soap/u/</li>
 * <li>Tooling = services/Soap/T/</li>
 * </ul>
 *
 * The fully qualified SFDC URLs for a web service always use the aforementioned partial URLs and a host plus either an API version
 * or a custom web service name. Here are some examples:
 * <ul>
 * <li>Apex = https://cs9.salesforce.com/services/Soap/s/31.0</li>
 * <li>Custom = https://cs9.salesforce.com/services/Soap/class/Foo</li>
 * <li>Enterprise = https://cs9.salesforce.com/services/Soap/m/32.0</li>
 * <li>Metadata = https://cs9.salesforce.com/services/Soap/m/33.0</li>
 * <li>Partner = https://cs9.salesforce.com/services/Soap/u/34.0</li>
 * <li>Tooling = https://cs9.salesforce.com/services/Soap/T/35.0</li>
 * </ul>
 *
 * When logging in, you have to use the typical production (https://login.salesforce.com) or sandbox (https://test.salesforce.com)
 * URLS with either the enterprise, partner or tooling partial URLs plus API version. Test login URL examples:
 * <ul>
 * <li>Enterprise = https://test.salesforce.com/services/Soap/m/32.0</li>
 * <li>Partner = https://test.salesforce.com/services/Soap/u/33.0</li>
 * <li>Tooling = https://test.salesforce.com/services/Soap/T/34.0</li>
 * </ul>
 *
 * Production login URL examples:
 * <ul>
 * <li>Enterprise = https://login.salesforce.com/services/Soap/m/32.0</li>
 * <li>Partner = https://login.salesforce.com/services/Soap/u/33.0</li>
 * <li>Tooling = https://login.salesforce.com/services/Soap/T/34.0</li>
 * </ul>
 *
 * Additionally, an instance of Keraia's web services are included, along with the class of the port those web services use. The
 * exception here is for user defined custom web services as those will always be defined by consumers of this library.
 *
 * @author Scot P. Floess
 */
public enum WebServiceTypeEnum {
    APEX_TYPE("services/Soap/s"),
    CUSTOM_TYPE("services/Soap/class"),
    ENTERPRISE_TYPE("services/Soap/c"),
    METADATA_TYPE("services/Soap/m"),
    PARTNER_TYPE("services/Soap/u"),
    TOOLING_TYPE("services/Soap/T");

    /**
     * The partial URL as defined in the Java doc header.
     */
    private final String partialUrl;

    /**
     * This constructor sets the SFDC partial URL (as defined in the Java doc header).
     *
     * @param partialUrl the partial URL as defined in the Java doc header.
     */
    private WebServiceTypeEnum(final String partialUrl) {
        this.partialUrl = partialUrl;
    }

    /**
     * Return the partial URL as defined in the Java doc header.
     *
     * @return the partial URL as defined in the Java doc header.
     */
    public String getPartialUrl() {
        return partialUrl;
    }
}
