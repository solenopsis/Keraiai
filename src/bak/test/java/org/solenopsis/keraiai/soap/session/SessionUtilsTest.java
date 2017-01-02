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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.solenopsis.keraiai.soap.session;

import org.solenopsis.keraiai.bak.soap.session.SessionUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.SecurityMgr;
import org.solenopsis.keraiai.soap.utils.ExceptionUtils;

/**
 * Tests the SessionUtils utility class.
 *
 * @author Scot P. Floess
 */
@RunWith( MockitoJUnitRunner.class )
public class SessionUtilsTest {

    static class TestException extends Exception {

    }

    static void testMethod() {

    }

    @Mock
    SecurityMgr securityMgr;

    @Mock
    Credentials credentials;

    @Before
    public void startTest() {
        Mockito.when(securityMgr.getCredentials()).thenReturn(credentials);
    }

    /**
     * Test the isCallRetriable.
     */
    @Test
    public void test_isCallRetriable() {
        for (int retry = 0; retry < SessionUtils.MAX_RETRIES; retry++) {
            Assert.assertTrue("Should be retriable", SessionUtils.isCallRetriable(retry));
        }

        for (int retry = SessionUtils.MAX_RETRIES; retry < SessionUtils.MAX_RETRIES * 2; retry++) {
            Assert.assertFalse("Should be retriable", SessionUtils.isCallRetriable(retry));
        }
    }

    /**
     * Test processing exceptions.
     */
    @Test( expected = TestException.class )
    public void test_processException_NotReloginException() throws Throwable {
        SessionUtils.processException(new TestException(), SessionUtilsTest.class.getDeclaredMethod("testMethod", new Class[0]));
    }

    /**
     * Test processing exceptions. No exception should be raised from here.
     */
    @Test
    public void test_processException() throws Throwable {
        final Method method = SessionUtilsTest.class.getDeclaredMethod("testMethod", new Class[0]);

        SessionUtils.processException(new IllegalArgumentException(ExceptionUtils.INVALID_SESSION_ID), method);

        SessionUtils.processException(new IllegalArgumentException(new IllegalArgumentException(ExceptionUtils.INVALID_SESSION_ID)), method);

        SessionUtils.processException(new IOException(), method);
        SessionUtils.processException(new IllegalArgumentException(new IOException()), method);
    }

    /**
     * Test computing the port name.
     */
    @Test
    public void test_computePortName() {
        final String apiVersion = TestUtils.generateUniqueStr();
        Mockito.when(credentials.getApiVersion()).thenReturn(apiVersion);

        Assert.assertEquals("Should be the API version as name".SessionUtils.computePortName(WebServiceType.));
    }
}
