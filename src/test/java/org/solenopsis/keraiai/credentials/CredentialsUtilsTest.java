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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.Credentials;

/**
 * Tests the CredentialsUtils utility class.
 *
 * @author sfloess
 */
@RunWith(MockitoJUnitRunner.class)
public class CredentialsUtilsTest {

    @Mock
    Credentials credentials;

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

    /**
     * Test credentials are null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_null() {
        CredentialsUtils.ensureCredentials(null, "Foo");
    }

    /**
     * Test credentials is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_api_null() {
        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test api is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_api_blank() {
        Mockito.when(credentials.getApiVersion()).thenReturn("  ");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test api is empty
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_api_empty() {
        Mockito.when(credentials.getApiVersion()).thenReturn("");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test password is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_password_null() {
        Mockito.when(credentials.getApiVersion()).thenReturn("31");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test password is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_password_blank() {
        Mockito.when(credentials.getApiVersion()).thenReturn("32");
        Mockito.when(credentials.getPassword()).thenReturn("  ");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test password is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_password_empty() {
        Mockito.when(credentials.getApiVersion()).thenReturn("33");
        Mockito.when(credentials.getPassword()).thenReturn("");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test token is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_token_null() {
        Mockito.when(credentials.getApiVersion()).thenReturn("31");
        Mockito.when(credentials.getPassword()).thenReturn("password");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test token is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_token_blank() {
        Mockito.when(credentials.getApiVersion()).thenReturn("32");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("  ");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test token is emoty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_token_empty() {
        Mockito.when(credentials.getApiVersion()).thenReturn("33");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test url is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_url_null() {
        Mockito.when(credentials.getApiVersion()).thenReturn("31");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test url is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_url_blank() {
        Mockito.when(credentials.getApiVersion()).thenReturn("32");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("  ");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test url is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_url_empty() {
        Mockito.when(credentials.getApiVersion()).thenReturn("33");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test username is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_username_null() {
        Mockito.when(credentials.getApiVersion()).thenReturn("31");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.com");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test username is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_username_blank() {
        Mockito.when(credentials.getApiVersion()).thenReturn("32");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.com");
        Mockito.when(credentials.getUserName()).thenReturn("  ");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test username is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_errorMsg_username_empty() {
        Mockito.when(credentials.getApiVersion()).thenReturn("33");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.com");
        Mockito.when(credentials.getUserName()).thenReturn("");

        CredentialsUtils.ensureCredentials(credentials, "Foo");
    }

    /**
     * Test good credentials
     */
    @Test
    public void test_ensureCredentials_credentials_errorMsg() {
        Mockito.when(credentials.getApiVersion()).thenReturn("33");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.com");
        Mockito.when(credentials.getUserName()).thenReturn("username");

        Assert.assertSame("Should be the same credentials", credentials, CredentialsUtils.ensureCredentials(credentials, "Foo"));
    }

    /**
     * Test credentials is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_null() {
        CredentialsUtils.ensureCredentials(null);
    }

    /**
     * Test api is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_api_null() {
        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test api is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_api_blank() {
        Mockito.when(credentials.getApiVersion()).thenReturn("  ");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test api is sempty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_api_empty() {
        Mockito.when(credentials.getApiVersion()).thenReturn("");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test password is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_password_null() {
        Mockito.when(credentials.getApiVersion()).thenReturn("31");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test password is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_password_blank() {
        Mockito.when(credentials.getApiVersion()).thenReturn("32");
        Mockito.when(credentials.getPassword()).thenReturn("  ");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test password is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_password_empty() {
        Mockito.when(credentials.getApiVersion()).thenReturn("33");
        Mockito.when(credentials.getPassword()).thenReturn("");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test token is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_token_null() {
        Mockito.when(credentials.getApiVersion()).thenReturn("31");
        Mockito.when(credentials.getPassword()).thenReturn("password");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test token is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_token_blank() {
        Mockito.when(credentials.getApiVersion()).thenReturn("32");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("  ");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test token is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_token_empty() {
        Mockito.when(credentials.getApiVersion()).thenReturn("33");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test url is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_url_null() {
        Mockito.when(credentials.getApiVersion()).thenReturn("31");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test url is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_url_blank() {
        Mockito.when(credentials.getApiVersion()).thenReturn("32");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("  ");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test url is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_url_empty() {
        Mockito.when(credentials.getApiVersion()).thenReturn("33");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test username is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_username_null() {
        Mockito.when(credentials.getApiVersion()).thenReturn("31");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.com");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test username is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_username_blank() {
        Mockito.when(credentials.getApiVersion()).thenReturn("32");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.com");
        Mockito.when(credentials.getUserName()).thenReturn("  ");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test username is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ensureCredentials_credentials_username_empty() {
        Mockito.when(credentials.getApiVersion()).thenReturn("33");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.com");
        Mockito.when(credentials.getUserName()).thenReturn("");

        CredentialsUtils.ensureCredentials(credentials);
    }

    /**
     * Test good credentials
     */
    @Test
    public void test_ensureCredentials_credentials() {
        Mockito.when(credentials.getApiVersion()).thenReturn("33");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.com");
        Mockito.when(credentials.getUserName()).thenReturn("username");

        Assert.assertSame("Should be the same credentials", credentials, CredentialsUtils.ensureCredentials(credentials, "Foo"));
    }
}
