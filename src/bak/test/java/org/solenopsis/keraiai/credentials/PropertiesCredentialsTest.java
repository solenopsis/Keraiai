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
package org.solenopsis.keraiai.credentials;

import java.util.Properties;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the StringCredentials class.
 *
 * @author Scot P. Floess
 */
public class PropertiesCredentialsTest {

    volatile static int id = 0;

    Properties createProperties(final String url, final String user, final String password, final String token, final String api) {
        String suffix = "" + (id++);

        final Properties props = new Properties();

        props.put(url, TestUtils.generateUniqueStr("url", suffix));
        props.put(user, TestUtils.generateUniqueStr("user", suffix));
        props.put(password, TestUtils.generateUniqueStr("password", suffix));
        props.put(token, TestUtils.generateUniqueStr("token", suffix));
        props.put(api, TestUtils.generateUniqueStr("api", suffix));

        return props;
    }

    Properties createProperties() {
        return createProperties("URL", "USER", "PASSWORD", "TOKEN", "API");
    }

    Properties createDefaultProperties() {
        return createProperties(
                CredentialsEnum.URL.getName(),
                CredentialsEnum.USER_NAME.getName(),
                CredentialsEnum.PASSWORD.getName(),
                CredentialsEnum.TOKEN.getName(),
                CredentialsEnum.API_VERSION.getName()
        );
    }

    PropertiesCredentials createCredentials(Properties properties) {
        return new PropertiesCredentials(properties, "URL", "USER", "PASSWORD", "TOKEN", "API");
    }

    PropertiesCredentials createDefaultCredentials(Properties properties) {
        return new PropertiesCredentials(properties);
    }

    PropertiesCredentials createCredentials() {
        return new PropertiesCredentials(createProperties(), "URL", "USER", "PASSWORD", "TOKEN", "API");
    }

    PropertiesCredentials createDefaultCredentials() {
        return new PropertiesCredentials(createDefaultProperties());
    }

    /**
     * Test the url in the constructor.
     */
    @Test
    public void test_constructor_userDefinedPropertyNames() {
        final Properties props1 = createProperties();
        props1.setProperty("URL", "http://foo/bar/");

        final PropertiesCredentials creds1 = createCredentials(props1);
        Assert.assertEquals("Should be correct url", "http://foo/bar/", creds1.getUrl());

        final Properties props2 = createProperties();
        props2.setProperty("URL", "http://alpha/beta");

        final PropertiesCredentials creds2 = createCredentials(props2);
        Assert.assertEquals("Should be correct url", "http://alpha/beta/", creds2.getUrl());
    }

    /**
     * Test the url in the constructor.
     */
    @Test
    public void test_constructor_defaultPropertyNames() {
        final Properties props1 = createDefaultProperties();
        props1.setProperty(CredentialsEnum.URL.getName(), "http://foo/bar/");

        final PropertiesCredentials creds1 = createDefaultCredentials(props1);
        Assert.assertEquals("Should be correct url", "http://foo/bar/", creds1.getUrl());

        final Properties props2 = createDefaultProperties();
        props2.setProperty(CredentialsEnum.URL.getName(), "http://alpha/beta");

        final PropertiesCredentials creds2 = createDefaultCredentials(props2);
        Assert.assertEquals("Should be correct url", "http://alpha/beta/", creds2.getUrl());
    }

    /**
     * Test equality.
     */
    @Test
    public void test_equals_userDefinedPropertyNames() {
        PropertiesCredentials creds1 = createCredentials();
        PropertiesCredentials creds2 = createCredentials();

        Assert.assertTrue("Should be equal", creds1.equals(creds1));
        Assert.assertTrue("Should be equal", creds2.equals(creds2));

        Assert.assertFalse("Should not be equal", creds1.equals(creds2));
        Assert.assertFalse("Should not be equal", creds2.equals(creds1));
    }

    /**
     * Test equality.
     */
    @Test
    public void test_equals_defaultPropertyNames() {
        PropertiesCredentials creds1 = createDefaultCredentials();
        PropertiesCredentials creds2 = createDefaultCredentials();

        Assert.assertTrue("Should be equal", creds1.equals(creds1));
        Assert.assertTrue("Should be equal", creds2.equals(creds2));

        Assert.assertFalse("Should not be equal", creds1.equals(creds2));
        Assert.assertFalse("Should not be equal", creds2.equals(creds1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void test_hashCode_userDefinedPropertyNames() {
        PropertiesCredentials creds1 = createCredentials();

        final int hashCode = creds1.getUserName().hashCode()
                             + creds1.getPassword().hashCode()
                             + creds1.getToken().hashCode()
                             + creds1.getUrl().hashCode()
                             + creds1.getApiVersion().hashCode();

        Assert.assertEquals("Should be equal", hashCode, creds1.hashCode());
    }

    /**
     * Test hash code.
     */
    @Test
    public void test_hashCode_defaultPropertyNames() {
        PropertiesCredentials creds1 = createDefaultCredentials();

        final int hashCode = creds1.getUserName().hashCode()
                             + creds1.getPassword().hashCode()
                             + creds1.getToken().hashCode()
                             + creds1.getUrl().hashCode()
                             + creds1.getApiVersion().hashCode();

        Assert.assertEquals("Should be equal", hashCode, creds1.hashCode());
    }

    /**
     * Test retrieving the security password with a null password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_userDefinedPropertNames_nullPassword() {
        final Properties props1 = createProperties();
        props1.remove("PASSWORD");

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a null password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_defaultPropertNames_nullPassword() {
        final Properties props1 = createDefaultProperties();
        props1.remove(CredentialsEnum.PASSWORD.getName());

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_userDefinedPropertyNames_blankPassword() {
        final Properties props1 = createProperties();
        props1.setProperty("PASSWORD", "  ");

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_defaultPropertyNames_blankPassword() {
        final Properties props1 = createDefaultProperties();
        props1.setProperty(CredentialsEnum.PASSWORD.getName(), "  ");

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_userDefiendPropertyNames_emptyPassword() {
        final Properties props1 = createProperties();
        props1.setProperty("PASSWORD", "");

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_defaultPropertyNames_emptyPassword() {
        final Properties props1 = createDefaultProperties();
        props1.setProperty(CredentialsEnum.PASSWORD.getName(), "");

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a null token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_userDefinedPropertyNames_nullToken() {
        final Properties props1 = createProperties();
        props1.remove("TOKEN");

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a null token.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_defaultPropertyNames_nullToken() {
        final Properties props1 = createDefaultProperties();
        props1.remove(CredentialsEnum.TOKEN.getName());

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_userDefinedPropertyNames_blankToken() {
        final Properties props1 = createProperties();
        props1.setProperty("TOKEN", "  ");

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with a blank password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_defaultPropertyNames_blankToken() {
        final Properties props1 = createDefaultProperties();
        props1.setProperty(CredentialsEnum.TOKEN.getName(), "  ");

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_userDefinedPropertyNames_emptyToken() {
        final Properties props1 = createProperties();
        props1.setProperty("TOKEN", "");

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test retrieving the security password with an empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getSecurityPassword_defaultPropertyNames_emptyToken() {
        final Properties props1 = createDefaultProperties();
        props1.setProperty(CredentialsEnum.TOKEN.getName(), "");

        createCredentials(props1).getSecurityPassword();
    }

    /**
     * Test computing the security password.
     */
    @Test
    public void test_getSecurityPassword_userDefinedPropertyNames() {
        PropertiesCredentials creds1 = createCredentials();

        Assert.assertFalse("Should not be blank", creds1.getSecurityPassword().trim().isEmpty());
        Assert.assertEquals("Should be equal", creds1.getPassword() + creds1.getToken(), creds1.getSecurityPassword());
    }

    /**
     * Test computing the security password.
     */
    @Test
    public void test_getSecurityPassword_defaultPropertyNames() {
        PropertiesCredentials creds1 = createDefaultCredentials();

        Assert.assertFalse("Should not be blank", creds1.getSecurityPassword().trim().isEmpty());
        Assert.assertEquals("Should be equal", creds1.getPassword() + creds1.getToken(), creds1.getSecurityPassword());
    }

    /**
     * Test computing the toString().
     */
    @Test
    public void test_toString_userDefinedPropertyNames() {
        Assert.assertFalse("Should not be blank", createCredentials().toString().isEmpty());
    }

    /**
     * Test computing the toString().
     */
    @Test
    public void test_toString_defaultPropertyNames() {
        Assert.assertFalse("Should not be blank", createDefaultCredentials().toString().isEmpty());
    }
}
