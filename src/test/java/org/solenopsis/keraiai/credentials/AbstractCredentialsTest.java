/*
 * Copyright (C) 2017 Scot P. Floess
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

import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the AbstractCredentials class.
 *
 * @author Scot P. Floess
 */
public class AbstractCredentialsTest {
    static class CredentialsStub extends AbstractCredentials {
        static int ID = 0;

        String url;

        String userName;

        String password;

        String token;

        String apiVersion;

        CredentialsStub() {
            String id = "" + (ID++);

            url = TestUtils.generateUniqueStr("url", id);
            userName = TestUtils.generateUniqueStr("userName", id);
            password = TestUtils.generateUniqueStr("password", id);
            token = TestUtils.generateUniqueStr("token", id);
            apiVersion = TestUtils.generateUniqueStr("apiVersion", id);
        }

        CredentialsStub(final CredentialsStub toCopy) {
            url = toCopy.url;
            userName = toCopy.userName;
            password = toCopy.password;
            token = toCopy.token;
            apiVersion = toCopy.apiVersion;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public String getUserName() {
            return userName;
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
            return apiVersion;
        }
    }

    /**
     * Test the equalsCredentials() method.
     */
    @Test
    public void test_equalsCredentials() {
        Assert.assertFalse("Should not be equal", new CredentialsStub().equalsCredentials(null));

        final CredentialsStub credentials1 = new CredentialsStub();

        Assert.assertTrue("Should be equal - same instance", credentials1.equalsCredentials(credentials1));

        final CredentialsStub credentials2 = new CredentialsStub(credentials1);

        Assert.assertTrue("Should be equal - copied instance", credentials1.equalsCredentials(credentials2));
        Assert.assertTrue("Should be equal - copied instance", credentials2.equalsCredentials(credentials1));

        credentials2.apiVersion = "foo";

        Assert.assertFalse("Should not be equal", credentials1.equalsCredentials(credentials2));
        Assert.assertFalse("Should not be equal", credentials2.equalsCredentials(credentials1));
    }

    /**
     * Test the equals() method.
     */
    @Test
    public void test_equals() {
        Assert.assertFalse("Should not be equal", new CredentialsStub().equals(null));

        final CredentialsStub credentials1 = new CredentialsStub();

        Assert.assertTrue("Should be equal - same instance", credentials1.equals(credentials1));

        final CredentialsStub credentials2 = new CredentialsStub(credentials1);

        Assert.assertTrue("Should be equal - copied instance", credentials1.equals(credentials2));
        Assert.assertTrue("Should be equal - copied instance", credentials2.equals(credentials1));

        credentials2.apiVersion = "foo";

        Assert.assertFalse("Should not be equal", credentials1.equals(credentials2));
        Assert.assertFalse("Should not be equal", credentials2.equals(credentials1));
    }

    /**
     * Test the hashCode() method.
     */
    @Test
    public void test_hashCode() {
        Assert.assertTrue("Should have computed a hash code", new CredentialsStub().hashCode() != 0);
    }

    /**
     * Test converting to a StringBuilder.
     */
    @Test
    public void test_toStringBuilder() {
        final StringBuilder sb = new StringBuilder();
        final StringBuilder toCompare = new CredentialsStub().toStringBuilder(sb, "");

        Assert.assertNotNull("Should not be null", toCompare);
        Assert.assertSame("Should be the same object", toCompare, sb);

        Assert.assertTrue("Should contain a string", toCompare.length() > 0);
    }
}
