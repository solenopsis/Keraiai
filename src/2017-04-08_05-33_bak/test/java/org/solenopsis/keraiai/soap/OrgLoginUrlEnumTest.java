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
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the OrgLoginUrlEnum class. This is a silly test solely to improve code coverage.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class OrgLoginUrlEnumTest {

    @Test
    public void test_values() {
        for (final OrgLoginUrlEnum orgLoginUrlEnum : OrgLoginUrlEnum.values()) {
            Assert.assertNotNull("Should have a value", orgLoginUrlEnum.getLoginUrl());
            Assert.assertFalse("Should have a login url", orgLoginUrlEnum.getLoginUrl().trim().isEmpty());
        }
    }
}
