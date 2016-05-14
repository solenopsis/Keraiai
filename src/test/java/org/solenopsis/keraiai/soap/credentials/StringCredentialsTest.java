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
package org.solenopsis.keraiai.soap.credentials;

import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the StringCredentials class.
 *
 * @author Scot P. Floess
 */
public class StringCredentialsTest {

    volatile static int id = 0;

    StringCredentials createCredentials() {
        String suffix = "" + (id++);
        return new StringCredentials(TestUtils.generateUniqueStr("url", suffix), TestUtils.generateUniqueStr("user", suffix), TestUtils.generateUniqueStr("password", suffix), TestUtils.generateUniqueStr("token", suffix), TestUtils.generateUniqueStr("api", suffix));
    }

    /**
     * Test the url for null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null() {
        final StringCredentials creds1 = new StringCredentials(null, "user", "password", "token", "api");
    }

    /**
     * Test the url for blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_blank() {
        final StringCredentials creds1 = new StringCredentials("  ", "user", "password", "token", "api");
    }

    /**
     * Test the url for blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_empty() {
        final StringCredentials creds1 = new StringCredentials("", "user", "password", "token", "api");
    }

    /**
     * Test the url in the constructor.
     */
    @Test
    public void test_constructor() {
        final StringCredentials creds1 = new StringCredentials("http://foo/bar/", "user", "password", "token", "api");
        Assert.assertEquals("Should be correct url", "http://foo/bar/", creds1.getUrl());

        final StringCredentials creds2 = new StringCredentials("http://alpha/beta", "user", "password", "token", "api");
        Assert.assertEquals("Should be correct url", "http://alpha/beta/", creds2.getUrl());
    }

    /**
     * Test equality.
     */
    @Test
    public void test_equals() {
        StringCredentials creds1 = createCredentials();
        StringCredentials creds2 = createCredentials();

        Assert.assertTrue("Should be equal", creds1.equals(creds1));
        Assert.assertTrue("Should be equal", creds2.equals(creds2));

        Assert.assertFalse("Should not be equal", creds1.equals(creds2));
        Assert.assertFalse("Should not be equal", creds2.equals(creds1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void test_hashCode() {
        StringCredentials creds1 = createCredentials();

        Assert.assertEquals("Should be equal", creds1.getUserName().hashCode(), creds1.hashCode());
    }

    /**
     * Test retrieving the security password with a null password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_nullPassword() {
        final StringCredentials creds1 = new StringCredentials("http://foo/bar/", "user", null, "token", "api");
        creds1.getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_blankPassword() {
        final StringCredentials creds1 = new StringCredentials("http://foo/bar/", "user", "  ", "token", "api");
        creds1.getSecurityPassword();
    }

    /**
     * Test retrieving the security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_emptyPassword() {
        final StringCredentials creds1 = new StringCredentials("http://foo/bar/", "user", "", "token", "api");
        creds1.getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a null token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_nullToken() {
        final StringCredentials creds1 = new StringCredentials("http://foo/bar/", "user", "password", null, "api");
        creds1.getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_blankToken() {
        final StringCredentials creds1 = new StringCredentials("http://foo/bar/", "user", "password", "  ", "api");
        creds1.getSecurityPassword();
    }

    /**
     * Test retrieving the security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_emptyToken() {
        final StringCredentials creds1 = new StringCredentials("http://foo/bar/", "user", "password", "", "api");
        creds1.getSecurityPassword();
    }

    /**
     * Test computing the security password.
     */
    @Test
    public void test_getSecurityPassword() {
        final StringCredentials creds1 = createCredentials();

        Assert.assertFalse("Should not be blank", creds1.getSecurityPassword().trim().isEmpty());
        Assert.assertEquals("Should be equal", creds1.getPassword() + creds1.getToken(), creds1.getSecurityPassword());
    }

    /**
     * Test computing the toString().
     */
    @Test
    public void test_toString() {
        Assert.assertFalse("Should not be blank", createCredentials().toString().isEmpty());
    }

    /**
     * Test setting and retrieving values.
     */
    @Test
    public void test_values() {
        final String suffix = "" + System.currentTimeMillis();

        final String url = TestUtils.generateUniqueStr("url", suffix);
        final String user = TestUtils.generateUniqueStr("user", suffix);
        final String password = TestUtils.generateUniqueStr("password", suffix);
        final String token = TestUtils.generateUniqueStr("token", suffix);
        final String api = TestUtils.generateUniqueStr("api", suffix);

        final StringCredentials creds1 = new StringCredentials(url, user, password, token, api);

        Assert.assertEquals("Should be the same url", url + "/", creds1.getUrl());
        Assert.assertEquals("Should be the same user name", user, creds1.getUserName());
        Assert.assertEquals("Should be the same password", password, creds1.getPassword());
        Assert.assertEquals("Should be the same token", token, creds1.getToken());
        Assert.assertEquals("Should be the same api", api, creds1.getApiVersion());
    }
}
