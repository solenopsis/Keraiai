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
package org.solenopsis.keraiai.soap.security;

import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.soap.credentials.Credentials;
import org.solenopsis.keraiai.soap.credentials.StringCredentials;

/**
 * Tests the AbstractSecurityMgr base class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractSecurityMgrTest {

    class BogustException extends Exception {

    }

    class AbstractSecurityMgrStub extends AbstractSecurityMgr {

        final LoginWebServiceTypeEnum loginWebServiceType;
        LoginContext myLoginContext;
        boolean isLoginCalled;
        boolean isLogoutCalled;

        RuntimeException toThrowRuntimeException;
        Exception toThrowException;

        AbstractSecurityMgrStub(final Credentials credentials, final LoginWebServiceTypeEnum loginWebServiceType) {
            super(credentials);

            this.loginWebServiceType = loginWebServiceType;
            this.isLoginCalled = false;
            this.isLogoutCalled = false;
        }

        @Override
        protected LoginWebServiceTypeEnum getLoginWebServiceType() {
            return loginWebServiceType;
        }

        @Override
        protected LoginContext doLogin(Object port) throws Exception {
            if (null != toThrowRuntimeException) {
                throw toThrowRuntimeException;
            } else if (null != toThrowException) {
                throw toThrowException;
            }

            isLoginCalled = true;
            return myLoginContext;
        }

        @Override
        protected void doLogout(Object port) throws Exception {
            if (null != toThrowRuntimeException) {
                throw toThrowRuntimeException;
            } else if (null != toThrowException) {
                throw toThrowException;
            }

            isLogoutCalled = true;
        }
    }

    @Mock
    Credentials credentials;

    @Mock
    LoginContext loginContext;

    @Mock
    LoginContext newLoginContext;

    AbstractSecurityMgrStub abstractSecurityMgrStub;

    @Before
    public void setup() {
        abstractSecurityMgrStub = new AbstractSecurityMgrStub(credentials, LoginWebServiceTypeEnum.TOOLING_LOGIN_SERVICE);
    }

    /**
     * Tests the constructor with null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null() {
        new AbstractSecurityMgrStub(null, LoginWebServiceTypeEnum.ENTERPRISE_LOGIN_SERVICE);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void test_constructor() {
        final Credentials creds = new StringCredentials(TestUtils.generateUniqueStr(), TestUtils.generateUniqueStr(), TestUtils.generateUniqueStr(), TestUtils.generateUniqueStr(), TestUtils.generateUniqueStr());

        Assert.assertSame("Should be the same credentials", creds, new AbstractSecurityMgrStub(creds, LoginWebServiceTypeEnum.PARTNER_LOGIN_SERVICE).getCredentials());
    }

    /**
     * Tests setting the login context.
     */
    @Test
    public void test_setLoginContext() {
        Assert.assertSame("Should be the same login context", loginContext, abstractSecurityMgrStub.setLoginContext(loginContext));
    }

    /**
     * Tests retrieving the session when the login context is set.
     */
    @Test
    public void test_getSession_loginContext() {
        abstractSecurityMgrStub.setLoginContext(loginContext);

        Assert.assertSame("Should be the same login context", loginContext, abstractSecurityMgrStub.getSession());
    }

    /**
     * Tests retrieving the session.
     */
    @Test
    public void test_getSession() {
        abstractSecurityMgrStub.myLoginContext = loginContext;

        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        Assert.assertSame("Should be the same login context", loginContext, abstractSecurityMgrStub.getSession());
        Assert.assertTrue("Should have called login", abstractSecurityMgrStub.isLoginCalled);
        Assert.assertFalse("Should not have called logout", abstractSecurityMgrStub.isLogoutCalled);
    }

    /**
     * Tests retrieving the session where a runtime exception is raised.
     */
    @Test(expected = NumberFormatException.class)
    public void test_getSession_runtimeException() {
        abstractSecurityMgrStub.myLoginContext = loginContext;

        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        abstractSecurityMgrStub.toThrowRuntimeException = new NumberFormatException("This was intentional");

        abstractSecurityMgrStub.getSession();
    }

    /**
     * Tests retrieving the session where an exception is raised.
     */
    @Test(expected = LoginException.class)
    public void test_getSession_exception() {
        abstractSecurityMgrStub.myLoginContext = loginContext;

        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        abstractSecurityMgrStub.toThrowException = new BogustException();

        abstractSecurityMgrStub.getSession();
    }

    /**
     * Tests resetting a session using different login contexts.
     */
    @Test
    public void test_resetSession_differentLoginContext() {
        abstractSecurityMgrStub.setLoginContext(loginContext);

        Assert.assertSame("Should be the correct login context", loginContext, abstractSecurityMgrStub.resetSession(newLoginContext));
    }

    /**
     * Tests resetting a session with the same login context.
     */
    @Test
    public void test_resetSession_sameLoginContext() {
        abstractSecurityMgrStub.setLoginContext(loginContext);
        abstractSecurityMgrStub.myLoginContext = newLoginContext;

        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        Assert.assertSame("Should be the same login context", newLoginContext, abstractSecurityMgrStub.resetSession(loginContext));
        Assert.assertTrue("Should have called login", abstractSecurityMgrStub.isLoginCalled);
        Assert.assertFalse("Should not have called logout", abstractSecurityMgrStub.isLogoutCalled);
    }

    /**
     * Tests retrieving the session where a runtime exception is raised.
     */
    @Test(expected = NumberFormatException.class)
    public void test_resetSession_runtimeException() {
        abstractSecurityMgrStub.setLoginContext(loginContext);

        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        abstractSecurityMgrStub.toThrowRuntimeException = new NumberFormatException("This was intentional");

        abstractSecurityMgrStub.resetSession(loginContext);
    }

    /**
     * Tests retrieving the session where an exception is raised.
     */
    @Test(expected = LoginException.class)
    public void test_resetSession_exception() {
        abstractSecurityMgrStub.setLoginContext(loginContext);

        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        abstractSecurityMgrStub.toThrowException = new BogustException();

        abstractSecurityMgrStub.resetSession(loginContext);
    }

    /**
     * Tests login.
     */
    @Test
    public void test_login() {
        abstractSecurityMgrStub.setLoginContext(loginContext);
        abstractSecurityMgrStub.myLoginContext = newLoginContext;

        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        Assert.assertSame("Should be the same login context", newLoginContext, abstractSecurityMgrStub.login());
        Assert.assertTrue("Should have called login", abstractSecurityMgrStub.isLoginCalled);
        Assert.assertFalse("Should not have called logout", abstractSecurityMgrStub.isLogoutCalled);
    }

    /**
     * Tests login where a runtime exception is raised.
     */
    @Test(expected = NumberFormatException.class)
    public void test_login_runtimeException() {
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        abstractSecurityMgrStub.toThrowRuntimeException = new NumberFormatException("This was intentional");

        abstractSecurityMgrStub.login();
    }

    /**
     * Tests login where an exception is raised.
     */
    @Test(expected = LoginException.class)
    public void test_login_exception() {
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        abstractSecurityMgrStub.toThrowException = new BogustException();

        abstractSecurityMgrStub.login();
    }

    /**
     * Tests logout.
     */
    @Test
    public void test_logout() {
        abstractSecurityMgrStub.setLoginContext(loginContext);

        Mockito.when(loginContext.getServerUrl()).thenReturn("http://foo.bar");
        Mockito.when(loginContext.getSessionId()).thenReturn("" + System.currentTimeMillis());

        abstractSecurityMgrStub.logout();

        Assert.assertNull("Should be the same login context", abstractSecurityMgrStub.getLoginContext());
        Assert.assertFalse("Should not have called login", abstractSecurityMgrStub.isLoginCalled);
        Assert.assertTrue("Should have called logout", abstractSecurityMgrStub.isLogoutCalled);
    }

    /**
     * Tests logout where a runtime exception is raised.
     */
    @Test(expected = NumberFormatException.class)
    public void test_logout_runtimeException() {
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        abstractSecurityMgrStub.toThrowRuntimeException = new NumberFormatException("This was intentional");

        abstractSecurityMgrStub.logout();
    }

    /**
     * Tests logout where an exception is raised.
     */
    @Test(expected = LoginException.class)
    public void test_logout_exception() {
        Mockito.when(credentials.getUrl()).thenReturn("http://foo.bar");

        abstractSecurityMgrStub.toThrowException = new BogustException();

        abstractSecurityMgrStub.logout();
    }
}
