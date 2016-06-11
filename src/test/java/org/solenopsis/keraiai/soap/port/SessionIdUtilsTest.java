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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the SessionIdUtils utility class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionIdUtilsTest {

    class BindingStub implements Binding {

        List<Handler> handlerChain = new ArrayList<>();

        @Override
        public List<Handler> getHandlerChain() {
            return handlerChain;
        }

        @Override
        public void setHandlerChain(List<Handler> list) {
            handlerChain = list;
        }

        @Override
        public String getBindingID() {
            return TestUtils.generateUniqueStr();
        }
    }

    @Mock
    Service service;

    @Mock
    QName serviceName;

    @Mock
    SOAPHeader soapHeader;

    @Mock
    SOAPElement headerSoapElement;

    @Mock
    SOAPElement sessionIdSoapElement;

    @Mock
    BindingProvider port;

    Binding binding;

    String namespaceUri;
    String sessionId;

    @Before
    public void setup() throws SOAPException {
        namespaceUri = TestUtils.generateUniqueStr("namespace", "uri");
        sessionId = TestUtils.generateUniqueStr("session", "id");
        binding = new BindingStub();

        Mockito.when(service.getServiceName()).thenReturn(serviceName);
        Mockito.when(serviceName.getNamespaceURI()).thenReturn(namespaceUri);

        Mockito.when(soapHeader.addChildElement((QName) Mockito.anyObject())).thenReturn(headerSoapElement);
        Mockito.when(headerSoapElement.addChildElement(Mockito.anyString())).thenReturn(sessionIdSoapElement);

        Mockito.when(port.getBinding()).thenReturn(binding);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Constructor constructor = SessionIdUtils.class.getDeclaredConstructor(new Class[0]);
        constructor.setAccessible(true);
        constructor.newInstance(new Object[0]);

        System.out.println("C -> " + constructor);
    }

    /**
     * Test computing the QName with a null service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_null() {
        SessionIdUtils.computeSessionHeaderName(null);
    }

    /**
     * Test computing the QName.
     */
    @Test
    public void test_computeSessionHeaderName() {
        final QName qname = SessionIdUtils.computeSessionHeaderName(service);

        Assert.assertNotNull("Should have compute a QName", qname);
        Assert.assertEquals("Should have correct namespace UIR", namespaceUri, qname.getNamespaceURI());
        Assert.assertEquals("Should have correct local part", SessionIdUtils.SESSION_HEADER, qname.getLocalPart());
    }

    /**
     * Test setting the session id on a SOAP header with a null soap header.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setSessionId_SOAPHeader_nullSoapHeader() throws SOAPException {
        SessionIdUtils.setSessionId((SOAPHeader) null, serviceName, TestUtils.generateUniqueStr());
    }

    /**
     * Test setting the session id on a SOAP header with a null session header name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setSessionId_SOAPHeader_nullSessionHeaderName() throws SOAPException {
        SessionIdUtils.setSessionId(soapHeader, null, TestUtils.generateUniqueStr());
    }

    /**
     * Test setting the session id on a SOAP header with a null session id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setSessionId_SOAPHeader_nullSessionId() throws SOAPException {
        SessionIdUtils.setSessionId(soapHeader, serviceName, null);
    }

    /**
     * Test setting the session id on a SOAP header with a blank session id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setSessionId_SOAPHeader_blankSessionId() throws SOAPException {
        SessionIdUtils.setSessionId(soapHeader, serviceName, "    ");
    }

    /**
     * Test setting the session id on a SOAP header with an empty session id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setSessionId_SOAPHeader_emptySessionId() throws SOAPException {
        SessionIdUtils.setSessionId(soapHeader, serviceName, "");
    }

    /**
     * Test setting a session id on a SOAP header.
     */
    @Test
    public void test_setSessionId_SOAPHeader() throws SOAPException {
        SessionIdUtils.setSessionId(soapHeader, serviceName, sessionId);

        Mockito.verify(sessionIdSoapElement).addTextNode(sessionId);
    }

    /**
     * Test setting a session id on service - null service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setSessionId_Service_nullService() {
        SessionIdUtils.setSessionId((Service) null, port, sessionId);
    }

    /**
     * Test setting a session id on a service - null port.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setSessionId_Service_nullPort() {
        SessionIdUtils.setSessionId(service, null, sessionId);
    }

    /**
     * Test setting a session id on a service - null sessionId.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setSessionId_Service_nullSessionId() {
        SessionIdUtils.setSessionId(service, port, null);
    }

    /**
     * Test setting a session id on a service - blank sessionId.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setSessionId_Service_blankSessionId() {
        SessionIdUtils.setSessionId(service, port, "  ");
    }

    /**
     * Test setting a session id on a service - empty sessionId.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setSessionId_Service_emptySessionId() {
        SessionIdUtils.setSessionId(service, port, "");
    }

    /**
     * Test setting a session id on a service.
     */
    @Test
    public void test_setSessionId_Service() {
        SessionIdUtils.setSessionId(service, port, sessionId);

        Assert.assertNotNull("Should have a handler chain!", binding.getHandlerChain());
        Assert.assertFalse("Should have a handler", binding.getHandlerChain().isEmpty());
        Assert.assertEquals("Should have a lone handler", 1, binding.getHandlerChain().size());
        Assert.assertSame("Should be a SessionIdSoapHeaderHandler", SessionIdSoapHeaderHandler.class, binding.getHandlerChain().get(0).getClass());

        final SessionIdSoapHeaderHandler handler = (SessionIdSoapHeaderHandler) binding.getHandlerChain().get(0);
        Assert.assertEquals("Should be same session id", sessionId, handler.getSessionId());
        Assert.assertEquals("Should be correct namespace URI", namespaceUri, handler.getSessionHeaderName().getNamespaceURI());
        Assert.assertEquals("Should be correct local part", SessionIdUtils.SESSION_HEADER, handler.getSessionHeaderName().getLocalPart());
    }
}
