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
package org.solenopsis.keraiai.soap.security.enterprise;

import javax.xml.ws.Service;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.soap.WebServiceTypeEnum;
import org.solenopsis.keraiai.soap.credentials.Credentials;
import org.solenopsis.keraiai.soap.security.LoginContext;
import org.solenopsis.keraiai.soap.security.LoginWebServiceTypeEnum;
import org.solenopsis.keraiai.wsdl.enterprise.LoginResult;
import org.solenopsis.keraiai.wsdl.enterprise.Soap;

/**
 * Tests the EnterpriseSecurityMgr class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class EnterpriseSecurityMgrTest {

    class BogustException extends Exception {

    }

    class EnterpriseSecurityMgrStub extends EnterpriseSecurityMgr {

        EnterpriseSecurityMgrStub(final Credentials creds) {
            super(creds);
        }

        @Override
        protected Soap createPort(WebServiceTypeEnum webServiceType, final String serverUrl, Service service, Class portType, String name) {
            return soap;
        }

        @Override
        protected Soap createSessionPort(WebServiceTypeEnum webServiceType, final String serverUrl, final Service service, final Class portType, final String name, final String sessionId) {
            return soap;
        }
    }

    @Mock
    Credentials credentials;

    @Mock
    Soap soap;

    @Mock
    LoginResult loginResult;

    String url;
    String userName;
    String password;
    String token;
    String securityPassword;
    String apiVersion;

    String metadataServerUrl;
    boolean isPasswordExpired;
    boolean isSandbox;
    String sessionId;
    String userId;
    String serverUrl;

    EnterpriseSecurityMgr securityMgr;

    @Before
    public void setup() throws Exception {

        url = TestUtils.generateUniqueStr("http://foo.com/");
        Mockito.when(credentials.getUrl()).thenReturn(url);

        userName = TestUtils.generateUniqueStr("user");
        Mockito.when(credentials.getUserName()).thenReturn(userName);

        password = TestUtils.generateUniqueStr("password");
        Mockito.when(credentials.getPassword()).thenReturn(password);

        token = TestUtils.generateUniqueStr("token");
        Mockito.when(credentials.getToken()).thenReturn(token);

        securityPassword = password + token;
        Mockito.when(credentials.getSecurityPassword()).thenReturn(securityPassword);

        apiVersion = TestUtils.generateUniqueStr("apiVersion");
        Mockito.when(credentials.getApiVersion()).thenReturn(apiVersion);

        metadataServerUrl = TestUtils.generateUniqueStr("http://metadata.com/");
        Mockito.when(loginResult.getMetadataServerUrl()).thenReturn(metadataServerUrl);

        isPasswordExpired = (System.currentTimeMillis() % 2 == 0);
        Mockito.when(loginResult.isPasswordExpired()).thenReturn(isPasswordExpired);

        isSandbox = (System.currentTimeMillis() % 2 == 0);
        Mockito.when(loginResult.isSandbox()).thenReturn(isSandbox);

        sessionId = TestUtils.generateUniqueStr("session id");
        Mockito.when(loginResult.getSessionId()).thenReturn(sessionId);

        userId = TestUtils.generateUniqueStr("user id");
        Mockito.when(loginResult.getUserId()).thenReturn(userId);

        serverUrl = TestUtils.generateUniqueStr("http://server.com/");
        Mockito.when(loginResult.getServerUrl()).thenReturn(serverUrl);

        Mockito.when(soap.login(userName, securityPassword)).thenReturn(loginResult);

        securityMgr = new EnterpriseSecurityMgrStub(credentials);
    }

    /**
     * Tests the constructor with null Credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null_Credentials() {
        new EnterpriseSecurityMgr(null);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void test_constructor() {
        Assert.assertSame("Should be the same credentials", credentials, securityMgr.getCredentials());
    }

    /**
     * Tests the web service type.
     */
    @Test
    public void test_getLoginWebServiceType() {
        Assert.assertSame("Should be the correct web service type", LoginWebServiceTypeEnum.ENTERPRISE_LOGIN_SERVICE, securityMgr.getLoginWebServiceType());
    }

    /**
     * Tests login.
     */
    @Test
    public void test_doLogin() throws Exception {
        final EnterpriseLoginContext loginContext = (EnterpriseLoginContext) securityMgr.doLogin(soap);

        Mockito.verify(soap, Mockito.times(1)).login(userName, securityPassword);

        Assert.assertSame("Should be the same login result", loginResult, loginContext.getLoginResult());

        Assert.assertSame("Should be the same metadata url", metadataServerUrl, loginContext.getMetadataServerUrl());
        Assert.assertSame("Should be the same password expired", isPasswordExpired, loginContext.isPasswordExpired());
        Assert.assertSame("Should be the same sandbox", isSandbox, loginContext.isSandbox());
        Assert.assertSame("Should be the same session id", sessionId, loginContext.getSessionId());
        Assert.assertSame("Should be the same user id", userId, loginContext.getUserId());
        Assert.assertSame("Should be the same server url", serverUrl, loginContext.getServerUrl());
    }

    /**
     * Tests logout.
     */
    @Test
    public void test_doLogout() throws Exception {
        securityMgr.doLogout(soap);

        Mockito.verify(soap, Mockito.times(1)).logout();
    }

    /**
     * Test getting the credentials.
     */
    @Test
    public void test_getCredentials() {
        Assert.assertSame("Should be the same credentials", credentials, securityMgr.getCredentials());
    }

    /**
     * Tests retrieving a session.
     */
    @Test
    public void test_getSession() throws Exception {
        final EnterpriseLoginContext loginContext1 = (EnterpriseLoginContext) securityMgr.getSession();

        Mockito.verify(soap, Mockito.times(1)).login(userName, securityPassword);

        Assert.assertSame("Should be the correct login result", loginResult, loginContext1.getLoginResult());

        final EnterpriseLoginContext loginContext2 = (EnterpriseLoginContext) securityMgr.getSession();

        Mockito.verify(soap, Mockito.times(1)).login(userName, securityPassword);

        Assert.assertSame("Should be the same login context", loginContext1, loginContext2);

        Assert.assertSame("Should be the same login result", loginContext1.getLoginResult(), loginContext2.getLoginResult());

    }

    /**
     * Tests resetting a session.
     */
    @Test
    public void test_resetSession() throws Exception {
        final LoginContext bogustContext = Mockito.mock(LoginContext.class);

        final LoginContext loginContext = securityMgr.login();

        Assert.assertSame("Should be the same login context", loginContext, securityMgr.resetSession(bogustContext));

        Mockito.verify(soap, Mockito.times(1)).login(userName, securityPassword);

        Assert.assertNotSame("Should be different login context", loginContext, securityMgr.resetSession(loginContext));

        Mockito.verify(soap, Mockito.times(2)).login(userName, securityPassword);
    }

    /**
     * Tests login raising a RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void test_login_RuntimeException() throws Exception {
        Mockito.when(soap.login(userName, securityPassword)).thenThrow(new RuntimeException());

        securityMgr.login();
    }

    /**
     * Tests login.
     */
    @Test
    public void test_login() throws Exception {
        final EnterpriseLoginContext loginContext = (EnterpriseLoginContext) securityMgr.login();

        Mockito.verify(soap, Mockito.times(1)).login(userName, securityPassword);

        Assert.assertSame("Should be the same login result", loginResult, loginContext.getLoginResult());

        Assert.assertSame("Should be the same metadata url", metadataServerUrl, loginContext.getMetadataServerUrl());
        Assert.assertSame("Should be the same password expired", isPasswordExpired, loginContext.isPasswordExpired());
        Assert.assertSame("Should be the same sandbox", isSandbox, loginContext.isSandbox());
        Assert.assertSame("Should be the same session id", sessionId, loginContext.getSessionId());
        Assert.assertSame("Should be the same user id", userId, loginContext.getUserId());
        Assert.assertSame("Should be the same server url", serverUrl, loginContext.getServerUrl());
    }

    /**
     * Tests logout when a runtime exception is raised.
     */
    @Test(expected = RuntimeException.class)
    public void test_logout_RuntimeException() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(soap).logout();

        securityMgr.logout();
    }

    /**
     * Test logout.
     */
    @Test
    public void test_logout() throws Exception {
        securityMgr.logout();

        Mockito.verify(soap, Mockito.times(1)).logout();
    }
}
