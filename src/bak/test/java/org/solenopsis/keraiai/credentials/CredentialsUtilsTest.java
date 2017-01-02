/*
 * Copyright (C) 2016 flossware
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
package org.solenopsis.keraiai.credentials;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the CredentialsUtils utility class.
 *
 * @author sfloess
 */
public class CredentialsUtilsTest {

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Constructor constructor = CredentialsUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance(new Object[0]);
    }

    /**
     * Test computing the security password with a null password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_nullPassword() {
        CredentialsUtils.computeSecurityPassword(null, "foo");
    }

    /**
     * Test computing the security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_emptyPassword() {
        CredentialsUtils.computeSecurityPassword("", "foo");
    }

    /**
     * Test computing the security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_blankPassword() {
        CredentialsUtils.computeSecurityPassword("  ", "foo");
    }

    /**
     * Test computing the security password with a null token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_nullToken() {
        CredentialsUtils.computeSecurityPassword("bar", null);
    }

    /**
     * Test computing the security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_emptyToken() {
        CredentialsUtils.computeSecurityPassword("bar", "");
    }

    /**
     * Test computing the security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_blankToken() {
        CredentialsUtils.computeSecurityPassword("bar", "  ");
    }

    /**
     * Test computing the security password.
     */
    @Test
    public void test_computeSecurityPassword_String_String() {
        final String password = "password" + System.currentTimeMillis();
        final String token = "token" + System.currentTimeMillis();

        Assert.assertEquals("Should be the same", password + token, CredentialsUtils.computeSecurityPassword(password, token));
    }

    /**
     * Test computing the security password with null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_Credentials_null() {
        CredentialsUtils.computeSecurityPassword(null);
    }

    /**
     * Test computing the security password with credentials.
     */
    @Test
    public void test_computeSecurityPassword_Credentials() {
        final String password = TestUtils.generateUniqueStr("password");
        final String token = TestUtils.generateUniqueStr("token");

        final StringCredentials creds = new StringCredentials("http://foo.bar", "myUser", password, token, "31.0");

        Assert.assertEquals("Should be correct security password", password + token, CredentialsUtils.computeSecurityPassword(creds));
    }
}
