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
 * Denotes the sub URL one needs when calling an SFDC web service:
 * <ul>
 * <li>Apex = services/Soap/s/</li>
 * <li>Custom = services/Soap/class/</li>
 * <li>Enterprise = services/Soap/c/</li>
 * <li>Metadata = services/Soap/m/</li>
 * <li>Partner = services/Soap/u/</li>
 * <li>Tooling = services/Soap/T/</li>
 * </ul>
 *
 * @author Scot P. Floess
 */
public enum WebServiceSubUrlEnum {
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
    private WebServiceSubUrlEnum(final String partialUrl) {
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
