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
package org.solenopsis.keraiai.soap.utils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the ExceptionUtils utility class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionUtilsTest {

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Constructor constructor = ExceptionUtils.class.getDeclaredConstructor(new Class[0]);
        constructor.setAccessible(true);
        constructor.newInstance(new Object[0]);
    }

    /**
     * Tests if an exception message is contained.
     */
    @Test
    public void test_isExceptionMsgContained_String() {
        Assert.assertFalse("Should not be contained", ExceptionUtils.isExceptionMsgContained(null, "bar"));
        Assert.assertFalse("Should not be contained", ExceptionUtils.isExceptionMsgContained("foo", (String) null));
        Assert.assertFalse("Should not be contained", ExceptionUtils.isExceptionMsgContained("foo", "bar"));

        Assert.assertTrue("Should be contained", ExceptionUtils.isExceptionMsgContained("Hello", TestUtils.generateUniqueStr("Hello", "Goodbye")));
        Assert.assertTrue("Should be contained", ExceptionUtils.isExceptionMsgContained("Goodbye", TestUtils.generateUniqueStr("Hello", "Goodbye")));
    }

    /**
     * Tests if an exception message is contained in a throwable.
     */
    @Test
    public void test_isExceptionMsgContained_Throwable() {
        Assert.assertFalse("Should not be contained", ExceptionUtils.isExceptionMsgContained(null, new Throwable("bar")));
        Assert.assertFalse("Should not be contained", ExceptionUtils.isExceptionMsgContained("foo", (Throwable) null));
        Assert.assertFalse("Should not be contained", ExceptionUtils.isExceptionMsgContained("foo", new Throwable("bar")));
        Assert.assertFalse("Should not be contained", ExceptionUtils.isExceptionMsgContained("Meh", new InvocationTargetException(new Throwable(TestUtils.generateUniqueStr("Hello", "Goodbye")))));

        Assert.assertTrue("Should be contained", ExceptionUtils.isExceptionMsgContained("Hello", new Throwable(TestUtils.generateUniqueStr("Hello", "Goodbye"))));
        Assert.assertTrue("Should be contained", ExceptionUtils.isExceptionMsgContained("Goodbye", new Throwable(TestUtils.generateUniqueStr("Hello", "Goodbye"))));

        Assert.assertTrue("Should be contained", ExceptionUtils.isExceptionMsgContained("Goodbye", new InvocationTargetException(new Throwable(TestUtils.generateUniqueStr("Hello", "Goodbye")))));
        Assert.assertTrue("Should be contained", ExceptionUtils.isExceptionMsgContained("Goodbye", new InvocationTargetException(new RuntimeException(new Throwable(TestUtils.generateUniqueStr("Hello", "Goodbye"))))));
    }

    /**
     * Tests if we have an invalid session id msg.
     */
    @Test
    public void test_isInvalidSessionId_String() {
        Assert.assertFalse("Should not be an invalid session id", ExceptionUtils.isInvalidSessionId((String) null));
        Assert.assertFalse("Should not be an invalid session id", ExceptionUtils.isInvalidSessionId(TestUtils.generateUniqueStr("Hello", "Goodbye")));

        Assert.assertTrue("Should be an invalid session id", ExceptionUtils.isInvalidSessionId(TestUtils.generateUniqueStr(ExceptionUtils.INVALID_SESSION_ID, "Goodbye")));
        Assert.assertTrue("Should be an invalid session id", ExceptionUtils.isInvalidSessionId(TestUtils.generateUniqueStr("Hello", ExceptionUtils.INVALID_SESSION_ID)));
        Assert.assertTrue("Should be an invalid session id", ExceptionUtils.isInvalidSessionId(ExceptionUtils.INVALID_SESSION_ID));
    }

    /**
     * Tests if we have a server unavailable msg.
     */
    @Test
    public void test_isServerUnavailable_String() {
        Assert.assertFalse("Should not be a server unavailable", ExceptionUtils.isServerUnavailable((String) null));
        Assert.assertFalse("Should not be a server unavailable", ExceptionUtils.isServerUnavailable(TestUtils.generateUniqueStr("Hello", "Goodbye")));

        Assert.assertTrue("Should be a server unavailable", ExceptionUtils.isServerUnavailable(TestUtils.generateUniqueStr(ExceptionUtils.SERVER_UNAVAILABLE, "Goodbye")));
        Assert.assertTrue("Should be a server unavailable", ExceptionUtils.isServerUnavailable(TestUtils.generateUniqueStr("Hello", ExceptionUtils.SERVER_UNAVAILABLE)));
        Assert.assertTrue("Should be a server unavailable", ExceptionUtils.isServerUnavailable(ExceptionUtils.SERVER_UNAVAILABLE));
    }

    /**
     * Tests if a throwable is an invalid session id.
     */
    @Test
    public void test_isInvalidSessionId() {
        Assert.assertFalse("Should not be an invalid session id", ExceptionUtils.isInvalidSessionId((Throwable) null));
        Assert.assertFalse("Should not be an invalid session id", ExceptionUtils.isInvalidSessionId(new Throwable("bar")));

        Assert.assertTrue("Should be an invalid session id", ExceptionUtils.isInvalidSessionId(new Throwable(TestUtils.generateUniqueStr(ExceptionUtils.INVALID_SESSION_ID, "Goodbye"))));
        Assert.assertTrue("Should be an invalid session id", ExceptionUtils.isInvalidSessionId(new Throwable(TestUtils.generateUniqueStr("Hello", ExceptionUtils.INVALID_SESSION_ID))));

        Assert.assertTrue("Should be an invalid session id", ExceptionUtils.isInvalidSessionId(new InvocationTargetException(new Throwable(TestUtils.generateUniqueStr(ExceptionUtils.INVALID_SESSION_ID, "Goodbye")))));
    }

    /**
     * Tests if a throwable is a server unavailable.
     */
    @Test
    public void test_isServerUnavailable() {
        Assert.assertFalse("Should not be an invalid session id", ExceptionUtils.isServerUnavailable((Throwable) null));
        Assert.assertFalse("Should not be an invalid session id", ExceptionUtils.isServerUnavailable(new Throwable("bar")));

        Assert.assertTrue("Should be an invalid session id", ExceptionUtils.isServerUnavailable(new Throwable(TestUtils.generateUniqueStr(ExceptionUtils.SERVER_UNAVAILABLE, "Goodbye"))));
        Assert.assertTrue("Should be an invalid session id", ExceptionUtils.isServerUnavailable(new Throwable(TestUtils.generateUniqueStr("Hello", ExceptionUtils.SERVER_UNAVAILABLE))));

        Assert.assertTrue("Should be an invalid session id", ExceptionUtils.isServerUnavailable(new InvocationTargetException(new Throwable(TestUtils.generateUniqueStr(ExceptionUtils.SERVER_UNAVAILABLE, "Goodbye")))));
    }

    /**
     * Tests if a throwable contains an IOException.
     */
    @Test
    public void test_containsIOException() {
        Assert.assertFalse("Should not contain an IOException", ExceptionUtils.containsIOException((Throwable) null));
        Assert.assertFalse("Should not contain an IOException", ExceptionUtils.containsIOException(new Throwable("bar")));

        Assert.assertTrue("Should contain an IOException", ExceptionUtils.containsIOException(new IOException(TestUtils.generateUniqueStr())));
        Assert.assertTrue("Should contain an IOException", ExceptionUtils.containsIOException(new RuntimeException(new IOException(TestUtils.generateUniqueStr()))));
        Assert.assertTrue("Should contain an IOException", ExceptionUtils.containsIOException(new RuntimeException(new RuntimeException(new IOException(TestUtils.generateUniqueStr())))));
    }

    /**
     * Tests if a throwable is a relogin exception.
     */
    @Test
    public void test_isReloginException() {
        Assert.assertFalse("Should not be a relogin exception", ExceptionUtils.isReloginException((Throwable) null));
        Assert.assertFalse("Should not be a relogin exception", ExceptionUtils.isReloginException(new Throwable("bar")));

        Assert.assertTrue("Should be a relogin exception", ExceptionUtils.isReloginException(new Throwable(TestUtils.generateUniqueStr(ExceptionUtils.INVALID_SESSION_ID, "Goodbye"))));
        Assert.assertTrue("Should be a relogin exception", ExceptionUtils.isReloginException(new Throwable(TestUtils.generateUniqueStr("Hello", ExceptionUtils.INVALID_SESSION_ID))));

        Assert.assertTrue("Should a relogin exception", ExceptionUtils.isReloginException(new InvocationTargetException(new Throwable(TestUtils.generateUniqueStr(ExceptionUtils.INVALID_SESSION_ID, "Goodbye")))));

        Assert.assertTrue("Should a relogin exception", ExceptionUtils.isReloginException(new IOException(TestUtils.generateUniqueStr())));
        Assert.assertTrue("Should a relogin exception", ExceptionUtils.isReloginException(new RuntimeException(new IOException(TestUtils.generateUniqueStr()))));
        Assert.assertTrue("Should a relogin exception", ExceptionUtils.isReloginException(new RuntimeException(new RuntimeException(new IOException(TestUtils.generateUniqueStr())))));
    }
}
