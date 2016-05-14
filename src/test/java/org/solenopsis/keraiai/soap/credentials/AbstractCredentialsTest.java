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

        String password;
        String token;

        AbstractCredentialsStub(final String password, final String token) {
            this.password = password;
            this.token = token;
            this.myId = (id++);
        }

        AbstractCredentialsStub() {
            this.myId = (id++);
            this.password = "password" + myId;
            this.token = "token" + myId;
        }

        @Override
        public String getUrl() {
            return "url" + myId;
        }

        @Override
        public String getUserName() {
            return "user" + myId;
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
        public String getApiVersion() {
            return "api" + myId;
        }
    }

    /**
     * Test equality.
     */
    @Test
    public void test_equals() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub();
        AbstractCredentialsStub stub2 = new AbstractCredentialsStub();

        Assert.assertTrue("Should be equal", stub1.equals(stub1));
        Assert.assertTrue("Should be equal", stub2.equals(stub2));

        Assert.assertFalse("Should not be equal", stub1.equals(stub2));
        Assert.assertFalse("Should not be equal", stub2.equals(stub1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void test_hashCode() {
        AbstractCredentialsStub stub1 = new AbstractCredentialsStub();

        Assert.assertEquals("Should be equal", stub1.getUserName().hashCode(), stub1.hashCode());
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
