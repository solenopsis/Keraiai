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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
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
 * Tests the non specific portions of the Utils utility class.
 *
 * @author Scot P. Floess
 */
@RunWith( MockitoJUnitRunner.class )
public class PortUtilsTest {

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
    BindingProvider port;

    Map<String, Object> requestContext;

    Binding binding;

    String namespaceUri;
    String sessionId;

    @Before
    public void setup() throws SOAPException {
        requestContext = new HashMap<>();
        binding = new BindingStub();
        namespaceUri = TestUtils.generateUniqueStr("namespace", "uri");
        sessionId = TestUtils.generateUniqueStr("session", "id");

        Mockito.when(service.getServiceName()).thenReturn(serviceName);
        Mockito.when(serviceName.getNamespaceURI()).thenReturn(namespaceUri);

        Mockito.when(port.getBinding()).thenReturn(binding);
        Mockito.when(port.getRequestContext()).thenReturn(requestContext);
    }

    /**
     * Test computing the QName with a null service.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_computeSessionHeaderName_null() {
        PortUtils.computeSessionHeaderName(null);
    }

    /**
     * Test computing the QName.
     */
    @Test
    public void test_computeSessionHeaderName() {
        final QName qname = PortUtils.computeSessionHeaderName(service);

        Assert.assertNotNull("Should have compute a QName", qname);
        Assert.assertEquals("Should have correct namespace UIR", namespaceUri, qname.getNamespaceURI());
        Assert.assertEquals("Should have correct local part", PortUtils.SESSION_HEADER, qname.getLocalPart());
    }

    /**
     * Test setting a session id on service - null service.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_setSessionId_Service_nullService() {
        PortUtils.setSessionId((Service) null, port, sessionId);
    }

    /**
     * Test setting a session id on a service - null port.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_setSessionId_Service_nullPort() {
        PortUtils.setSessionId(service, null, sessionId);
    }

    /**
     * Test setting a session id on a service - null sessionId.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_setSessionId_Service_nullSessionId() {
        PortUtils.setSessionId(service, port, null);
    }

    /**
     * Test setting a session id on a service - blank sessionId.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_setSessionId_Service_blankSessionId() {
        PortUtils.setSessionId(service, port, "  ");
    }

    /**
     * Test setting a session id on a service - empty sessionId.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_setSessionId_Service_emptySessionId() {
        PortUtils.setSessionId(service, port, "");
    }

    /**
     * Test setting a session id on a service.
     */
    @Test
    public void test_setSessionId_Service() {
        PortUtils.setSessionId(service, port, sessionId);

        Assert.assertNotNull("Should have a handler chain!", binding.getHandlerChain());
        Assert.assertFalse("Should have a handler", binding.getHandlerChain().isEmpty());
        Assert.assertEquals("Should have a lone handler", 1, binding.getHandlerChain().size());
        Assert.assertSame("Should be a SessionIdSoapHeaderHandler", SessionIdSoapRequestHeaderHandler.class, binding.getHandlerChain().get(0).getClass());

        final SessionIdSoapRequestHeaderHandler handler = (SessionIdSoapRequestHeaderHandler) binding.getHandlerChain().get(0);
        Assert.assertEquals("Should be same session id", sessionId, handler.getValue());
        Assert.assertEquals("Should be correct local part", PortUtils.SESSION_HEADER, handler.getHeaderName().getLocalPart());
        Assert.assertEquals("Should be correct namespace URI", namespaceUri, handler.getHeaderName().getNamespaceURI());
    }

    /**
     * Test computing a URL string using strings.
     */
    @Test
    public void test_computeUrlString() {
        Assert.assertEquals("Should be the same string", "https://foo.com/bar/alpha", PortUtils.computeUrlString("https", "foo.com", "bar", "alpha"));
    }

    /**
     * Test computing a URL string using a url.
     */
    @Test
    public void test_computeUrlString_url() throws MalformedURLException {
        Assert.assertEquals("Should be the same string", "https://foo.com/bar/alpha", PortUtils.computeUrlString(new URL("https://foo.com/not/here/now"), "bar", "alpha"));
    }

    /**
     * Test computing a URL string using a null url where the serverUrl has more subpaths.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_computeUrlString_paths_null() throws MalformedURLException {
        PortUtils.computeUrlString((String) null, "bar", "alpha");
    }

    /**
     * Test computing a URL string using a blank url where the serverUrl has more subpaths.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_computeUrlString_paths_blank() throws MalformedURLException {
        PortUtils.computeUrlString("  ", "bar", "alpha");
    }

    /**
     * Test computing a URL string using an empty url where the serverUrl has more subpaths.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_computeUrlString_paths_empty() throws MalformedURLException {
        PortUtils.computeUrlString("", "bar", "alpha");
    }

    /**
     * Test computing a URL string using a maleformed url where the serverUrl has more subpaths.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_computeUrlString_paths_malformed() throws MalformedURLException {
        PortUtils.computeUrlString("h ttp:_fo", "bar", "alpha");
    }

    /**
     * Test computing a URL string using a proper url where the serverUrl has more subpaths.
     */
    @Test
    public void test_computeUrlString_paths() throws MalformedURLException {
        Assert.assertEquals("Should be the same string", "http://foo.com/bar/alpha", PortUtils.computeUrlString("http://foo.com/theta/papapapa", "bar", "alpha"));
    }

    /**
     * Test setting a URL using a null url.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_setUrl_null() throws MalformedURLException {
        PortUtils.setUrl(port, null, "bar", "alpha");
    }

    /**
     * Test setting a URL using a blank url.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_setUrl_blank() throws MalformedURLException {
        PortUtils.setUrl(port, "  ", "bar", "alpha");
    }

    /**
     * Test setting a URL using an empty url.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_setUrl_empty() throws MalformedURLException {
        PortUtils.setUrl(port, "", "bar", "alpha");
    }

    /**
     * Test setting a URL using a maleformed url.
     */
    @Test( expected = IllegalArgumentException.class )
    public void test_setUrl_malformed() throws MalformedURLException {
        PortUtils.setUrl(port, "h ttp:_fo", "bar", "alpha");
    }

    /**
     * Test setting a URL using a proper url.
     */
    @Test
    public void test_setUrl() throws MalformedURLException {
        PortUtils.setUrl(port, "http://foo.com/theta/apapapapa", "bar", "alpha");

        Assert.assertFalse("Should contain data", requestContext.isEmpty());
        Assert.assertEquals("Should only be one element", 1, requestContext.size());

        Assert.assertEquals("Should be correct url", "http://foo.com/bar/alpha", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Constructor constructor = PortUtils.class.getDeclaredConstructor(new Class[0]);
        constructor.setAccessible(true);
        constructor.newInstance(new Object[0]);
    }

}
