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

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the WebServiceSubUrlEnum class.
 *
 * @author Scot P. Floess
 */
public class WebServiceSubUrlEnumTest {

    @Test
    public void test_values() {
        for (final WebServiceSubUrlEnum webServiceSubUrlEnum : WebServiceSubUrlEnum.values()) {
            Assert.assertNotNull("Should have a partial url", webServiceSubUrlEnum.getPartialUrl());
            Assert.assertNotEquals("Should have a partial url", "", webServiceSubUrlEnum.getPartialUrl().trim());
        }
    }
}
