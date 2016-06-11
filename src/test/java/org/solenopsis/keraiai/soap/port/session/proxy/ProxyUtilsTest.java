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
package org.solenopsis.keraiai.soap.port.session.proxy;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Test;
import org.solenopsis.keraiai.soap.utils.ExceptionUtils;

/**
 * Tests the ProxyUtils utility class.
 *
 * @author Scot P. Floess
 */
public class ProxyUtilsTest {

    /**
     * A stub method for testing inside self - not to be used.
     */
    public void stubMethod() {

    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Constructor constructor = ProxyUtils.class.getDeclaredConstructor(new Class[0]);
        constructor.setAccessible(true);
        constructor.newInstance(new Object[0]);
    }

    /**
     * Test if we can perform retries.
     */
    @Test
    public void test_isCallRetriable() {
        for (int retryCount = 0; retryCount < ProxyUtils.MAX_RETRIES; retryCount++) {
            Assert.assertTrue("Should be retriable", ProxyUtils.isCallRetriable(retryCount));
        }

        Assert.assertFalse("Should not be retryable!", ProxyUtils.isCallRetriable(ProxyUtils.MAX_RETRIES));
        Assert.assertFalse("Should not be retryable!", ProxyUtils.isCallRetriable(ProxyUtils.MAX_RETRIES + 1));
    }

    /**
     * Test processing an exception that is not a relogin.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_processException_IllegalArgumentException() throws NoSuchMethodException, Throwable {
        final Method method = ProxyUtilsTest.class.getDeclaredMethod("stubMethod", new Class[0]);

        ProxyUtils.processException(new IllegalArgumentException(), method);
    }

    /**
     * Test processing an exception that is not a relogin.
     */
    @Test
    public void test_processException() {
        try {
            final Method method = ProxyUtilsTest.class.getDeclaredMethod("stubMethod", new Class[0]);

            ProxyUtils.processException(new IllegalArgumentException("Foo " + System.currentTimeMillis() + ExceptionUtils.INVALID_SESSION_ID), method);
            ProxyUtils.processException(new RuntimeException(new IllegalArgumentException("Foo " + System.currentTimeMillis() + ExceptionUtils.INVALID_SESSION_ID)), method);
            ProxyUtils.processException(new RuntimeException(ExceptionUtils.INVALID_SESSION_ID, new IllegalArgumentException("Foo " + System.currentTimeMillis() + ExceptionUtils.INVALID_SESSION_ID)), method);
            ProxyUtils.processException(new InvocationTargetException(new IllegalArgumentException("Foo " + System.currentTimeMillis() + ExceptionUtils.INVALID_SESSION_ID)), method);
            ProxyUtils.processException(new InvocationTargetException(new IllegalArgumentException("Foo " + System.currentTimeMillis() + ExceptionUtils.INVALID_SESSION_ID), ExceptionUtils.INVALID_SESSION_ID), method);

            ProxyUtils.processException(new IOException("Foo " + System.currentTimeMillis()), method);
            ProxyUtils.processException(new RuntimeException(new IOException("Foo " + System.currentTimeMillis())), method);
        } catch (final Throwable throwable) {
            Assert.fail("Should not have made it here - failed!");
        }
    }
}
