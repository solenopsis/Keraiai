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
import org.junit.Before;
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

        private final String serverUrl;

        AbstractLoginContextStub(final Object loginResult, final Credentials creds, final String serverUrl) {
            super(loginResult, creds, serverUrl);
            this.serverUrl = serverUrl;
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
            return serverUrl;
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

    String serverUrl;

    @Before
    public void setup() {
        serverUrl = TestUtils.generateUniqueStr("https://foo.com/", "alpha");
    }

    /**
     * Tests the constructor with a null LoginResult.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_LoginResult_null() {
        new AbstractLoginContextStub(null, credentials, serverUrl);
    }

    /**
     * Tests the constructor with null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_Credentials_null() {
        new AbstractLoginContextStub(new Object(), null, serverUrl);
    }

    /**
     * Tests the constructor with null server url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_ServerUrl_null() {
        new AbstractLoginContextStub(new Object(), credentials, null);
    }

    /**
     * Tests the constructor with a blank server url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_ServerUrl_blank() {
        new AbstractLoginContextStub(new Object(), credentials, "");
    }

    /**
     * Tests the constructor with an empty server url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_ServerUrl_empty() {
        new AbstractLoginContextStub(new Object(), credentials, "    ");
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void test_constructor() {
        final Object loginResult = new Object();
        final AbstractLoginContextStub stub = new AbstractLoginContextStub(loginResult, credentials, serverUrl);

        Assert.assertSame("Should be the same credentials!", credentials, stub.getCredentials());
        Assert.assertSame("Should be the same login result!", loginResult, stub.getLoginResult());
        Assert.assertEquals("Should be the correct server URL", "https://foo.com", stub.getBaseServerUrl());
    }

}
