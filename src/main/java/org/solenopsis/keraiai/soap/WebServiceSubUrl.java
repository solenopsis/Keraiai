/*
 * Copyright (C) 2017 Scot P. Floess
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
 * Defines a web service sub url:
 * <ul>
 * <li>Apex = services/Soap/s/</li>
 * <li>Custom = services/Soap/class/</li>
 * <li>Enterprise = services/Soap/c/</li>
 * <li>Metadata = services/Soap/m/</li>
 * <li>Partner = services/Soap/u/</li>
 * <li>Tooling = services/Soap/T/</li>
 * </ul
 *
 * @author Scot P. Floess
 */
public interface WebServiceSubUrl {
    /**
     * Return the partial URL as defined in the Java doc header.
     *
     * @return the partial URL as defined in the Java doc header.
     */
    String getPartialUrl();
}
