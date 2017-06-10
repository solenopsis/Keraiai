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
package org.solenopsis.keraiai.soap.security.partner;

import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.wsdl.partner.LoginResult;

/**
 * Tests the PartnerLoginContext class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class PartnerLoginContextTest {

    @Mock
    LoginResult loginResult;

    @Mock
    Credentials credentials;

    /**
     * Tests the constructor with null LoginResult.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null_LoginResult() {
        new PartnerLoginContext(null, credentials);
    }

    /**
     * Tests the constructor with null Credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null_Credentials() {
        new PartnerLoginContext(loginResult, null);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void test_constructor() {
        final PartnerLoginContext context = new PartnerLoginContext(loginResult, credentials);

        Assert.assertSame("Should be the same login result", loginResult, context.getPartnerLoginResult());
        Assert.assertSame("Should be the same credentials", credentials, context.getCredentials());
    }

    /**
     * Tests getters.
     */
    @Test
    public void test_getters() {
        final String metadataUrl = TestUtils.generateUniqueStr("metadataUrl");
        final boolean isExpired = true;
        final boolean isSandbox = false;
        final String sessionId = TestUtils.generateUniqueStr("sessionId");
        final String userId = TestUtils.generateUniqueStr("userId");
        final String serverUrl = TestUtils.generateUniqueStr("serverUrl");

        Mockito.when(loginResult.getMetadataServerUrl()).thenReturn(metadataUrl);
        Mockito.when(loginResult.isPasswordExpired()).thenReturn(isExpired);
        Mockito.when(loginResult.isSandbox()).thenReturn(isSandbox);
        Mockito.when(loginResult.getSessionId()).thenReturn(sessionId);
        Mockito.when(loginResult.getUserId()).thenReturn(userId);
        Mockito.when(loginResult.getServerUrl()).thenReturn(serverUrl);

        final PartnerLoginContext context = new PartnerLoginContext(loginResult, credentials);

        Assert.assertEquals("Should be the correct metadata url", metadataUrl, context.getMetadataServerUrl());
        Assert.assertEquals("Should be the correct is password expired", isExpired, context.isPasswordExpired());
        Assert.assertEquals("Should be the correct is sandbox", isSandbox, context.isSandbox());
        Assert.assertEquals("Should be the correct session id", sessionId, context.getSessionId());
        Assert.assertEquals("Should be the correct user id", userId, context.getUserId());
        Assert.assertEquals("Should be the correct server url", serverUrl, context.getServerUrl());
    }
}
