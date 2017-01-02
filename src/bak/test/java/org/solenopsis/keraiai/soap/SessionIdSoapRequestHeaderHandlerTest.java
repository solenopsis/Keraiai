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
package org.solenopsis.keraiai.soap;

import org.solenopsis.keraiai.soap.port.PortUtils;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the SessionIdSoapRequestHeaderHandler class.
 *
 * @author Scot P. Floess
 */
@RunWith( MockitoJUnitRunner.class )
public class SessionIdSoapRequestHeaderHandlerTest {

    @Mock
    QName serviceName;

    String namespaceUri;
    String sessionId;

    @Before
    public void setup() throws SOAPException {
        namespaceUri = TestUtils.generateUniqueStr("namespace", "uri");
        sessionId = TestUtils.generateUniqueStr("session", "id");

        Mockito.when(serviceName.getNamespaceURI()).thenReturn(namespaceUri);
    }

    /**
     * Test a null session header name.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_constructor_sessionHeaderName_null() {
        new SessionIdSoapRequestHeaderHandler(null, TestUtils.generateUniqueStr());
    }

    /**
     * Test a null sessionId.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_constructor_sessionId_null() {
        new SessionIdSoapRequestHeaderHandler(new QName(TestUtils.generateUniqueStr(), TestUtils.generateUniqueStr()), null);
    }

    /**
     * Test a blank sessionId.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_constructor_sessionId_blank() {
        new SessionIdSoapRequestHeaderHandler(new QName(TestUtils.generateUniqueStr(), TestUtils.generateUniqueStr()), "     ");
    }

    /**
     * Test an empty sessionId.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_constructor_sessionId_empty() {
        new SessionIdSoapRequestHeaderHandler(new QName(TestUtils.generateUniqueStr(), TestUtils.generateUniqueStr()), "");
    }

    /**
     * Test constructing.
     */
    @Test
    public void test_constructor() {
        final SessionIdSoapRequestHeaderHandler sessionIdSoapHeaderHandler = new SessionIdSoapRequestHeaderHandler(serviceName, sessionId);

        Assert.assertSame("Should be the same session header name", serviceName, sessionIdSoapHeaderHandler.getHeaderName());
        Assert.assertSame("Should be the same name", PortUtils.SESSION_ID, sessionIdSoapHeaderHandler.getName());
        Assert.assertSame("Should be the same session id", sessionId, sessionIdSoapHeaderHandler.getValue());
    }
}
