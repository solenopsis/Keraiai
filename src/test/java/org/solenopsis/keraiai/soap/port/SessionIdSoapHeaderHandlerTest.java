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
package org.solenopsis.keraiai.soap.port;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the SessionIdSoapHeaderHandler class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionIdSoapHeaderHandlerTest {

    @Mock
    SOAPMessageContext msgContext;

    @Mock
    SOAPMessage soapMessage;

    @Mock
    SOAPPart soapPart;

    @Mock
    SOAPEnvelope soapEnvelope;

    @Mock
    SOAPHeader soapHeader;

    @Mock
    SOAPElement headerSoapElement;

    @Mock
    SOAPElement sessionIdSoapElement;

    @Mock
    QName serviceName;

    String namespaceUri;
    String sessionId;

    @Before
    public void setup() throws SOAPException {
        namespaceUri = TestUtils.generateUniqueStr("namespace", "uri");
        sessionId = TestUtils.generateUniqueStr("session", "id");

        Mockito.when(msgContext.getMessage()).thenReturn(soapMessage);
        Mockito.when(soapMessage.getSOAPPart()).thenReturn(soapPart);
        Mockito.when(soapPart.getEnvelope()).thenReturn(soapEnvelope);
        Mockito.when(soapEnvelope.getHeader()).thenReturn(soapHeader);
        Mockito.when(soapHeader.addChildElement((QName) Mockito.anyObject())).thenReturn(headerSoapElement);
        Mockito.when(headerSoapElement.addChildElement(Mockito.anyString())).thenReturn(sessionIdSoapElement);

        Mockito.when(serviceName.getNamespaceURI()).thenReturn(namespaceUri);
    }

    /**
     * Test a null session header name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_sessionHeaderName_null() {
        new SessionIdSoapHeaderHandler(null, TestUtils.generateUniqueStr());
    }

    /**
     * Test a null sessionId.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_sessionId_null() {
        new SessionIdSoapHeaderHandler(new QName(TestUtils.generateUniqueStr(), TestUtils.generateUniqueStr()), null);
    }

    /**
     * Test a blank sessionId.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_sessionId_blank() {
        new SessionIdSoapHeaderHandler(new QName(TestUtils.generateUniqueStr(), TestUtils.generateUniqueStr()), "     ");
    }

    /**
     * Test an empty sessionId.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_sessionId_empty() {
        new SessionIdSoapHeaderHandler(new QName(TestUtils.generateUniqueStr(), TestUtils.generateUniqueStr()), "");
    }

    /**
     * Test constructing.
     */
    @Test
    public void test_constructor() {
        final SessionIdSoapHeaderHandler sessionIdSoapHeaderHandler = new SessionIdSoapHeaderHandler(serviceName, sessionId);

        Assert.assertSame("Should be the same session header name", serviceName, sessionIdSoapHeaderHandler.getSessionHeaderName());
        Assert.assertSame("Should be the same session id", sessionId, sessionIdSoapHeaderHandler.getSessionId());
    }

    /**
     * Test no request.
     */
    @Test
    public void test_handleMessage_noRequest() throws SOAPException {
        final SessionIdSoapHeaderHandler sessionIdSoapHeaderHandler = new SessionIdSoapHeaderHandler(serviceName, sessionId);

        Mockito.when(msgContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(false);

        Assert.assertTrue("Should return true", sessionIdSoapHeaderHandler.handleMessage(msgContext));

        Mockito.verify(msgContext, Mockito.never()).getMessage();
        Mockito.verify(msgContext.getMessage(), Mockito.never()).saveChanges();
        Mockito.verify(soapMessage, Mockito.never()).saveChanges();
    }

    /**
     * Test request where we get a SOAPException saving changes.
     */
    @Test
    public void test_handleMessage_SOAPException() throws SOAPException {
        final SessionIdSoapHeaderHandler sessionIdSoapHeaderHandler = new SessionIdSoapHeaderHandler(serviceName, sessionId);

        Mockito.when(msgContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(true);
        Mockito.doThrow(new SOAPException()).when(soapMessage).saveChanges();

        Assert.assertTrue("Should return true", sessionIdSoapHeaderHandler.handleMessage(msgContext));

        Mockito.verify(msgContext, Mockito.atLeastOnce()).getMessage();
        Mockito.verify(soapMessage, Mockito.times(1)).saveChanges();
        Mockito.verify(soapHeader).addChildElement(serviceName);
        Mockito.verify(headerSoapElement).addChildElement(SessionIdUtils.SESSION_ID);
        Mockito.verify(sessionIdSoapElement).addTextNode(sessionId);
    }

    /**
     * Test request.
     */
    @Test
    public void test_handleMessage() throws SOAPException {
        final SessionIdSoapHeaderHandler sessionIdSoapHeaderHandler = new SessionIdSoapHeaderHandler(serviceName, sessionId);

        Mockito.when(msgContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(true);

        Assert.assertTrue("Should return true", sessionIdSoapHeaderHandler.handleMessage(msgContext));

        Mockito.verify(msgContext, Mockito.atLeastOnce()).getMessage();
        Mockito.verify(soapMessage).saveChanges();
        Mockito.verify(soapHeader).addChildElement(serviceName);
        Mockito.verify(headerSoapElement).addChildElement(SessionIdUtils.SESSION_ID);
        Mockito.verify(sessionIdSoapElement).addTextNode(sessionId);
    }

    /**
     * Test other default methods for code coverage.
     */
    @Test
    public void test_defaultMethods() {
        final SessionIdSoapHeaderHandler sessionIdSoapHeaderHandler = new SessionIdSoapHeaderHandler(serviceName, sessionId);

        Assert.assertNull("Should be null", sessionIdSoapHeaderHandler.getHeaders());
        Assert.assertTrue(sessionIdSoapHeaderHandler.handleFault(msgContext));
        sessionIdSoapHeaderHandler.close(msgContext);
    }
}
