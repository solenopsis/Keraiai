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
 * Test the AbstractCredentials base class.
 *
 * @author Scot P. Floess
 */
public class AbstractCredentialsTest {

    volatile static int id = 0;

    /**
     * Stub class for testing.
     */
    class AbstractCredentialsStub extends AbstractCredentials {

        int myId;

        String user;
        String password;
        String token;
        String url;
        String api;

        AbstractCredentialsStub(int myId) {
            this.myId = myId;

            this.user = "user" + myId;
            this.password = "password" + myId;
            this.token = "token" + myId;
            this.url = "url" + myId;
            this.api = "api" + myId;
        }

        AbstractCredentialsStub() {
            this(id++);
        }

        AbstractCredentialsStub(final String password, final String token) {
            this();

            this.password = password;
            this.token = token;
        }

        @Override
        public String getUserName() {
            return user;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getToken() {
            return token;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public String getApiVersion() {
            return api;
        }
    }

    /**
     * Test equality.
     */
    @Test
    public void test_equalsCredentials() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub();

        Assert.assertFalse("Should not be equal", stub1.equalsCredentials(null));
        Assert.assertTrue("Should be equals", stub1.equalsCredentials(stub1));

        final int myId = (int) System.currentTimeMillis();

        AbstractCredentialsStub stub3 = new AbstractCredentialsStub(myId);
        AbstractCredentialsStub stub4 = new AbstractCredentialsStub(myId);

        Assert.assertTrue("Should be equal", stub3.equalsCredentials(stub4));
        Assert.assertTrue("Should be equal", stub4.equalsCredentials(stub3));

        stub3.api = "api";
        Assert.assertFalse("Should not be equal", stub3.equalsCredentials(stub4));
        Assert.assertFalse("Should not be equal", stub4.equalsCredentials(stub3));
        stub3.api = stub4.api;

        stub3.url = "url";
        Assert.assertFalse("Should not be equal", stub3.equalsCredentials(stub4));
        Assert.assertFalse("Should not be equal", stub4.equalsCredentials(stub3));
        stub3.url = stub4.url;

        stub3.token = "token";
        Assert.assertFalse("Should not be equal", stub3.equalsCredentials(stub4));
        Assert.assertFalse("Should not be equal", stub4.equalsCredentials(stub3));
        stub3.token = stub4.token;

        stub3.password = "password";
        Assert.assertFalse("Should not be equal", stub3.equalsCredentials(stub4));
        Assert.assertFalse("Should not be equal", stub4.equalsCredentials(stub3));
        stub3.password = stub4.password;

        stub3.user = "user";
        Assert.assertFalse("Should not be equal", stub3.equalsCredentials(stub4));
        Assert.assertFalse("Should not be equal", stub4.equalsCredentials(stub3));
    }

    /**
     * Test equality.
     */
    @Test
    public void test_equals() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub();

        Assert.assertFalse("Should not be equal", stub1.equals(TestUtils.generateUniqueStr()));
        Assert.assertFalse("Should not be equal", stub1.equals((String) null));
        Assert.assertFalse("Should not be equal", stub1.equals((Credentials) null));

        Assert.assertTrue("Should not be equal", stub1.equals(stub1));

        final int myId = (int) System.currentTimeMillis();

        AbstractCredentialsStub stub3 = new AbstractCredentialsStub(myId);
        AbstractCredentialsStub stub4 = new AbstractCredentialsStub(myId);

        Assert.assertTrue("Should be equal", stub3.equals(stub4));
        Assert.assertTrue("Should be equal", stub4.equals(stub3));

        stub3.password = "password";
        Assert.assertFalse("Should not be equal", stub3.equals(stub4));
        stub3.password = stub4.password;

        stub3.token = "token";
        Assert.assertFalse("Should not be equal", stub3.equals(stub4));
        stub3.token = stub4.token;

        stub3.url = "url";
        Assert.assertFalse("Should not be equal", stub3.equals(stub4));
        stub3.url = stub4.url;

        stub3.api = "api";
        Assert.assertFalse("Should not be equal", stub3.equals(stub4));
    }

    /**
     * Test hash code.
     */
    @Test
    public void test_hashCode() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub();

        final int hashCode = stub1.getUserName().hashCode()
                + stub1.getPassword().hashCode()
                + stub1.getToken().hashCode()
                + stub1.getUrl().hashCode()
                + stub1.getApiVersion().hashCode();

        Assert.assertEquals("Should be equal", hashCode, stub1.hashCode());
    }

    /**
     * Test retrieving the security password with a null password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_nullPassword() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub(null, "foo");
        stub1.getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_blankPassword() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub("  ", "foo");
        stub1.getSecurityPassword();
    }

    /**
     * Test retrieving the security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_emptyPassword() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub("", "foo");
        stub1.getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a null token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_nullToken() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub("bar", null);
        stub1.getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_blankToken() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub("bar", "  ");
        stub1.getSecurityPassword();
    }

    /**
     * Test retrieving the security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_emptyToken() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub("bar", "");
        stub1.getSecurityPassword();
    }

    /**
     * Test computing the security password.
     */
    @Test
    public void test_getSecurityPassword() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub();

        Assert.assertFalse("Should not be blank", stub1.getSecurityPassword().trim().isEmpty());
        Assert.assertEquals("Should be equal", stub1.getPassword() + stub1.getToken(), stub1.getSecurityPassword());
    }

    /**
     * Test computing the toString().
     */
    @Test
    public void test_toString() {
        Assert.assertFalse("Should not be blank", new AbstractCredentialsStub().toString().isEmpty());
    }
}
