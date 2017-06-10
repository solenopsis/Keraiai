package org.solenopsis.keraiai.credentials;

import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.Credentials;

/*
 * Copyright (C) 2017 Scot P. Floess
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
/**
 * Tests the CredentialsUtils utility class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class CredentialsUtilsTest {
    @Mock
    Credentials credentials;

    @Before
    public void setup() {
        Mockito.when(credentials.getApiVersion()).thenReturn(TestUtils.generateUniqueStr("api"));
        Mockito.when(credentials.getPassword()).thenReturn(TestUtils.generateUniqueStr("password"));
        Mockito.when(credentials.getUrl()).thenReturn(TestUtils.generateUniqueStr("url"));
        Mockito.when(credentials.getUserName()).thenReturn(TestUtils.generateUniqueStr("username"));
    }

    /**
     * Test computing a security password with a null password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_null_password() {
        CredentialsUtils.computeSecurityPassword(null, "foo");
    }

    /**
     * Test computing a security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_blank_password() {
        CredentialsUtils.computeSecurityPassword("    ", "foo");
    }

    /**
     * Test computing a security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_empty_password() {
        CredentialsUtils.computeSecurityPassword("", "foo");
    }

    /**
     * Test computing a security password with a null token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_null_token() {
        CredentialsUtils.computeSecurityPassword("bar", null);
    }

    /**
     * Test computing a security password with a blank token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_blank_token() {
        CredentialsUtils.computeSecurityPassword("bar", "    ");
    }

    /**
     * Test computing a security password with an empty token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_String_String_empty_token() {
        CredentialsUtils.computeSecurityPassword("bar", "");
    }

    /**
     * Test computing a security password.
     */
    @Test
    public void test_computeSecurityPassword_String_String() {
        final String password = TestUtils.generateUniqueStr("password");
        final String token = TestUtils.generateUniqueStr("token");

        Assert.assertEquals("Should be same security token", password + token, CredentialsUtils.computeSecurityPassword(password, token));
    }

    /**
     * Tests computing a security password with null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_Credentials_null() {
        CredentialsUtils.computeSecurityPassword(null);
    }

    /**
     * Test computing a security password with a null password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_Credentials_null_password() {
        Mockito.when(credentials.getPassword()).thenReturn(null);

        CredentialsUtils.computeSecurityPassword(credentials);
    }

    /**
     * Test computing a security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_Credentials_blank_password() {
        Mockito.when(credentials.getToken()).thenReturn("foo");
        Mockito.when(credentials.getPassword()).thenReturn("    ");

        CredentialsUtils.computeSecurityPassword(credentials);
    }

    /**
     * Test computing a security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_Credentials_empty_password() {
        Mockito.when(credentials.getToken()).thenReturn("foo");
        Mockito.when(credentials.getPassword()).thenReturn("");

        CredentialsUtils.computeSecurityPassword(credentials);
    }

    /**
     * Test computing a security password with a null token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_Credentials_null_token() {
        Mockito.when(credentials.getPassword()).thenReturn("bar");

        CredentialsUtils.computeSecurityPassword(credentials);
    }

    /**
     * Test computing a security password with a blank token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_Credentials_blank_token() {
        Mockito.when(credentials.getToken()).thenReturn("    ");
        Mockito.when(credentials.getPassword()).thenReturn("bar");

        CredentialsUtils.computeSecurityPassword(credentials);
    }

    /**
     * Test computing a security password with an empty token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSecurityPassword_Credentials_empty_token() {
        Mockito.when(credentials.getToken()).thenReturn("");
        Mockito.when(credentials.getPassword()).thenReturn("bar");

        CredentialsUtils.computeSecurityPassword(credentials);
    }

    /**
     * Test computing a security password.
     */
    @Test
    public void test_computeSecurityPassword_Credentials() {
        final String password = TestUtils.generateUniqueStr("password");
        final String token = TestUtils.generateUniqueStr("token");

        Mockito.when(credentials.getToken()).thenReturn(token);
        Mockito.when(credentials.getPassword()).thenReturn(password);

        Assert.assertEquals("Should be same security token", password + token, CredentialsUtils.computeSecurityPassword(credentials));
    }
}
