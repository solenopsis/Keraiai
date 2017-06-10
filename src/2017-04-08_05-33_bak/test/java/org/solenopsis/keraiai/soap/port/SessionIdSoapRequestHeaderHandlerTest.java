package org.solenopsis.keraiai.soap.port;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.LoginContext;
import org.solenopsis.keraiai.SecurityMgr;

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
/**
 * Tests the SessionIdSoapRequestHeaderHandler class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionIdSoapRequestHeaderHandlerTest {

    QName qname;

    @Mock
    LoginContext loginContext;

    @Mock
    SecurityMgr securityMgr;

    @Before
    public void init() {
        qname = new QName(TestUtils.generateUniqueStr("str1"));
    }

    /**
     * Test constructor with null session id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_String_QName_sessionId_null() {
        new SessionIdSoapRequestHeaderHandler((String) null, qname);
    }

    /**
     * Test constructor with blank session id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_String_QName_sessionId_blank() {
        new SessionIdSoapRequestHeaderHandler("  ", qname);
    }

    /**
     * Test constructor with an empty session id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_String_QName_sessionId_empty() {
        new SessionIdSoapRequestHeaderHandler("", qname);
    }

    /**
     * Test constructor with a null QName.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_String_QName_sessionHeaderName_null() {
        new SessionIdSoapRequestHeaderHandler(TestUtils.generateUniqueStr(), null);
    }

    /**
     * Test good constructor.
     */
    public void test_SessionIdSoapRequestHeaderHandler_String_QName() {
        final String sessionId = TestUtils.generateUniqueStr("Session ID");
        final SessionIdSoapRequestHeaderHandler handler = new SessionIdSoapRequestHeaderHandler(sessionId, qname);

        Assert.assertEquals("Should be the correct name", SessionIdSoapRequestHeaderHandler.SESSION_ID, handler.getName());
        Assert.assertEquals("Should be the same session id", sessionId, handler.getValue());
        Assert.assertEquals("Should be the same session header name", qname, handler.getHeaderName());
    }

    /**
     * Test constructor with a null LoginContext.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_LoginContext_QName_session_null() {
        new SessionIdSoapRequestHeaderHandler((LoginContext) null, qname);
    }

    /**
     * Test constructor with a null session header name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_LoginContext_QName_sessionHeaderName_null() {
        new SessionIdSoapRequestHeaderHandler(loginContext, null);
    }

    /**
     * Test constructor with a null session id in the LoginContext.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_LoginContext_QName_sessionId_null() {
        new SessionIdSoapRequestHeaderHandler(loginContext, qname);
    }

    /**
     * Test constructor with a blank session id in the LoginContext.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_LoginContext_QName_sessionId_blank() {
        Mockito.when(loginContext.getSessionId()).thenReturn("  ");

        new SessionIdSoapRequestHeaderHandler(loginContext, qname);
    }

    /**
     * Test constructor with an empty session id in the LoginContext.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_LoginContext_QName_sessionId_empty() {
        Mockito.when(loginContext.getSessionId()).thenReturn("");

        new SessionIdSoapRequestHeaderHandler(loginContext, qname);
    }

    /**
     * Test good constructor.
     */
    public void test_SessionIdSoapRequestHeaderHandler_LoginContext_QName() {
        final String sessionId = TestUtils.generateUniqueStr("Session ID");
        Mockito.when(loginContext.getSessionId()).thenReturn(sessionId);

        final SessionIdSoapRequestHeaderHandler handler = new SessionIdSoapRequestHeaderHandler(loginContext, qname);

        Assert.assertEquals("Should be the correct name", SessionIdSoapRequestHeaderHandler.SESSION_ID, handler.getName());
        Assert.assertEquals("Should be the same session id", sessionId, handler.getValue());
        Assert.assertEquals("Should be the same session header name", qname, handler.getHeaderName());
    }

    /**
     * Test constructor with a null SecurityMgr.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_SecurityMgr_QName_securityMgr_null() {
        new SessionIdSoapRequestHeaderHandler((SecurityMgr) null, qname);
    }

    /**
     * Test constructor with a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_SecurityMgr_QName_service_null() {
        new SessionIdSoapRequestHeaderHandler(securityMgr, (QName) null);
    }

    /**
     * Test constructor with a null LoginContext in the securityMgr.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_SecurityMgr_QName_session_null() {
        new SessionIdSoapRequestHeaderHandler(securityMgr, qname);
    }

    /**
     * Test good constructor.
     */
    public void test_SessionIdSoapRequestHeaderHandler_SecurityMgr_QName() {
        final String sessionId = TestUtils.generateUniqueStr("Session ID");
        Mockito.when(loginContext.getSessionId()).thenReturn(sessionId);
        Mockito.when(securityMgr.getSession()).thenReturn(loginContext);

        final SessionIdSoapRequestHeaderHandler handler = new SessionIdSoapRequestHeaderHandler(securityMgr, qname);

        Assert.assertEquals("Should be the correct name", SessionIdSoapRequestHeaderHandler.SESSION_ID, handler.getName());
        Assert.assertEquals("Should be the same session id", sessionId, handler.getValue());
        Assert.assertEquals("Should be the same session header name", qname);
    }

    /**
     * Test constructor with a null SecurityMgr.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_SecurityMgr_Service_securityMgr_null() {
        new SessionIdSoapRequestHeaderHandler((SecurityMgr) null, WebServiceTypeEnum.METADATA_SERVICE_TYPE.getWebService().getService());
    }

    /**
     * Test constructor with a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_SessionIdSoapRequestHeaderHandler_SecurityMgr_Service_service_null() {
        new SessionIdSoapRequestHeaderHandler(securityMgr, (Service) null);
    }

    /**
     * Test good constructor.
     */
    @Test
    public void test_SessionIdSoapRequestHeaderHandler_SecurityMgr_Service() {
        final String sessionId = TestUtils.generateUniqueStr("Session ID");
        Mockito.when(loginContext.getSessionId()).thenReturn(sessionId);
        Mockito.when(securityMgr.getSession()).thenReturn(loginContext);

        final SessionIdSoapRequestHeaderHandler handler = new SessionIdSoapRequestHeaderHandler(securityMgr, qname);

        Assert.assertEquals("Should be the correct name", SessionIdSoapRequestHeaderHandler.SESSION_ID, handler.getName());
        Assert.assertEquals("Should be the same session id", sessionId, handler.getValue());
        Assert.assertEquals("Should be the same session header name", qname, handler.getHeaderName());
    }
}
