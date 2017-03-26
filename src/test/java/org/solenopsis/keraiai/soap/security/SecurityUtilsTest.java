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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.solenopsis.keraiai.soap.security;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.SecurityMgr;
import org.solenopsis.keraiai.soap.port.WebServiceTypeEnum;

/**
 * Tests the SecurityUtils class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityUtilsTest {
    @Mock
    Credentials credentials;

    @Mock
    SecurityMgr securityMgr;

    @Before
    public void setup() {
        Mockito.when(credentials.getApiVersion()).thenReturn("31.0");
        Mockito.when(credentials.getPassword()).thenReturn("password");
        Mockito.when(credentials.getToken()).thenReturn("token");
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.com");
        Mockito.when(credentials.getUserName()).thenReturn("username");

        Mockito.when(securityMgr.getCredentials()).thenReturn(credentials);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Constructor constructor = SecurityUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance(new Object[0]);
    }

    /**
     * Test computing a login url with null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeLoginUrl_credentials_webServiceType_null_credentials() {
        SecurityUtils.computeLoginUrl((Credentials) null, WebServiceTypeEnum.APEX_SERVICE_TYPE);
    }

    /**
     * Test computing a login url with null web service type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeLoginUrl_credentials_webServiceType_null_webServiceType() {
        SecurityUtils.computeLoginUrl(credentials, null);
    }

    /**
     * Test computing a login url.
     */
    @Test
    public void test_computeLoginUrl_credentials_webServiceType() {
        Assert.assertEquals("Should be the correct login url", "http://foo.com/services/Soap/c/31.0", SecurityUtils.computeLoginUrl(credentials, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE));
    }

    /**
     * Test computing a login url with null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeLoginUrl_securityMgr_webServiceType_null_credentials() {
        SecurityUtils.computeLoginUrl((SecurityMgr) null, WebServiceTypeEnum.APEX_SERVICE_TYPE);
    }

    /**
     * Test computing a login url with null web service type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeLoginUrl_securityMgr_webServiceType_null_webServiceType() {
        SecurityUtils.computeLoginUrl(securityMgr, null);
    }

    /**
     * Test computing a login url.
     */
    @Test
    public void test_computeLoginUrl_securityMgr_webServiceType() {
        Assert.assertEquals("Should be the correct login url", "http://foo.com/services/Soap/c/31.0", SecurityUtils.computeLoginUrl(securityMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE));
    }
}
