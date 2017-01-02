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
package org.solenopsis.keraiai.soap.security;

import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.Credentials;

/**
 * Tests the AbstractLoginContext base class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractLoginContextTest {

    class AbstractLoginContextStub extends AbstractLoginContext {

        AbstractLoginContextStub(final Credentials creds) {
            super(creds);
        }

        @Override
        public String getMetadataServerUrl() {
            return TestUtils.generateUniqueStr();
        }

        @Override
        public boolean isPasswordExpired() {
            return true;
        }

        @Override
        public boolean isSandbox() {
            return true;
        }

        @Override
        public String getServerUrl() {
            return TestUtils.generateUniqueStr();
        }

        @Override
        public String getSessionId() {
            return TestUtils.generateUniqueStr();
        }

        @Override
        public String getUserId() {
            return TestUtils.generateUniqueStr();
        }

    }

    @Mock
    Credentials credentials;

    /**
     * Tests the constructor with null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null() {
        new AbstractLoginContextStub(null);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void test_constructor() {
        final AbstractLoginContextStub stub = new AbstractLoginContextStub(credentials);

        Assert.assertSame("Should be the same credentials!", credentials, stub.getCredentials());
    }

}
