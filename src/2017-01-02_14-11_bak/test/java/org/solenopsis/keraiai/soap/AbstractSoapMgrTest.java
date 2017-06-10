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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.handler.Handler;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.SecurityMgr;

/**
 * Tests the AbstractPortFactory base class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractSoapMgrTest {

    /**
     * Stub implementation to test against.
     */
    class AbstractPortFactoryStub extends AbstractSoapMgr {

    }

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

    /**
     * Simple stub class for examining web endpoint methods.
     */
    @WebServiceClient(targetNamespace = "Target_Namespace", name = "Name")
    static class StubService extends Service {

        @WebEndpoint(name = "Web_Endpoint_Method")
        public Integer webEndpointMethod() {
            return 0;
        }

        public void nonWebEndpointMethod() {
        }

        public StubService() {
            super(AbstractSoapMgrTest.class.getResource("/TestService.wsdl"), new QName("urn:enterprise.soap.sforce.com", "SforceService"));
        }
    }

    @Mock
    QName serviceName;

    @Mock
    SOAPHeader soapHeader;

    @Mock
    SOAPElement headerSoapElement;

    @Mock
    SOAPElement sessionIdSoapElement;

    Service service;

    @Mock
    BindingProvider port;

    Binding binding;

    String namespaceUri;
    String sessionId;

    Map<String, Object> requestContext;

    @Mock
    SecurityMgr securityMgr;

    @Mock
    Credentials credentials;

    String apiVersion;

    @Before
    public void setup() throws SOAPException {
        namespaceUri = TestUtils.generateUniqueStr("namespace", "uri");
        sessionId = TestUtils.generateUniqueStr("session", "id");
        binding = new BindingStub();

        service = new StubService();

        Mockito.when(service.getServiceName()).thenReturn(serviceName);
        Mockito.when(serviceName.getNamespaceURI()).thenReturn(namespaceUri);

        Mockito.when(soapHeader.addChildElement((QName) Mockito.anyObject())).thenReturn(headerSoapElement);
        Mockito.when(headerSoapElement.addChildElement(Mockito.anyString())).thenReturn(sessionIdSoapElement);

        Mockito.when(port.getBinding()).thenReturn(binding);

        Mockito.when(service.getPort((Class) Mockito.any())).thenReturn(port);

        requestContext = new HashMap<>();

        Mockito.when(port.getRequestContext()).thenReturn(requestContext);

        Mockito.when(securityMgr.getCredentials()).thenReturn(credentials);

        apiVersion = TestUtils.generateUniqueStr();
        Mockito.when(credentials.getApiVersion()).thenReturn(apiVersion);
    }

    /**
     * Test preparing a port with a null security mgr.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_preparePort_null_securityMgr() {
        new AbstractPortFactoryStub().preparePort(null, TestUtils.generateUniqueStr(), WebServiceTypeEnum.APEX_SERVICE_TYPE, service, port);
    }

    /**
     * Test preparing a port with a null baseUrl.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_preparePort_null_baseUrl() {
        new AbstractPortFactoryStub().preparePort(securityMgr, null, WebServiceTypeEnum.APEX_SERVICE_TYPE, service, port);
    }

    /**
     * Test preparing a port with an emptu baseUrl.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_preparePort_empty_baseUrl() {
        new AbstractPortFactoryStub().preparePort(securityMgr, "", WebServiceTypeEnum.CUSTOM_SERVICE_TYPE, service, port);
    }

    /**
     * Test preparing a port with a blank baseUrl.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_preparePort_blank_baseUrl() {
        new AbstractPortFactoryStub().preparePort(securityMgr, "  ", WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service, port);
    }

    /**
     * Test preparing a port with a null WebServiceTypeEnum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_preparePort_null_webServiceType() {
        new AbstractPortFactoryStub().preparePort(securityMgr, TestUtils.generateUniqueStr(), null, service, port);
    }

    /**
     * Test preparing a port with a null service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_preparePort_null_service() {
        new AbstractPortFactoryStub().preparePort(securityMgr, TestUtils.generateUniqueStr(), WebServiceTypeEnum.METADATA_SERVICE_TYPE, null, port);
    }

    /**
     * Test preparing a port with a null port.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_preparePort_null_port() {
        new AbstractPortFactoryStub().preparePort(securityMgr, TestUtils.generateUniqueStr(), WebServiceTypeEnum.PARTNER_SERVICE_TYPE, service, null);
    }

    /**
     * Test preparing a port.
     */
    @Test
    public void test_preparePort() {
        new AbstractPortFactoryStub().preparePort(securityMgr, namespaceUri, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service, port);
    }

    /**
     * Test creating a port using a null url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createPort_Service_null() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.ENTERPRISE_TYPE, "http://foo/bar", null, Object.class, "hello");
    }

    /**
     * Test creating a port using a null url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createPort_serverUrl_null() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.ENTERPRISE_TYPE, null, service, Object.class, "hello");
    }

    /**
     * Test creating a port using a blank url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createPort_serverUrl_blank() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.ENTERPRISE_TYPE, "  ", service, Object.class, "hello");
    }

    /**
     * Test creating a port using an empty url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createPort_serverUrl_empty() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.ENTERPRISE_TYPE, "", service, Object.class, "hello");
    }

    /**
     * Test creating a port using a maleformed url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createPort_serverUrl_malformed() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.ENTERPRISE_TYPE, "h ttp:_fo", service, Object.class, "hello");
    }

    /**
     * Test creating an apex port using a proper url.
     */
    @Test
    public void test_createPort_apex() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.APEX_TYPE, "https://foo1.com/bar/alpha", service, Object.class, "myApex");

        Assert.assertFalse("Should contain data", requestContext.isEmpty());
        Assert.assertEquals("Should only be one element", 1, requestContext.size());

        Assert.assertEquals("Should be correct url", "https://foo1.com/services/Soap/s/myApex", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating a custom port using a proper url.
     */
    @Test
    public void test_createPort_custom() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.CUSTOM_TYPE, "https://foo2.com/bar/alpha", service, Object.class, "myCustom");

        Assert.assertFalse("Should contain data", requestContext.isEmpty());
        Assert.assertEquals("Should only be one element", 1, requestContext.size());

        Assert.assertEquals("Should be correct url", "https://foo2.com/services/Soap/class/myCustom", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating an enterprise port using a proper url.
     */
    @Test
    public void test_createPort_enterprise() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.ENTERPRISE_TYPE, "https://foo3.com/bar/alpha", service, Object.class, "myEnterprise");

        Assert.assertFalse("Should contain data", requestContext.isEmpty());
        Assert.assertEquals("Should only be one element", 1, requestContext.size());

        Assert.assertEquals("Should be correct url", "https://foo3.com/services/Soap/c/myEnterprise", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating an metadata port using a proper url.
     */
    @Test
    public void test_createPort_metadata() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.METADATA_TYPE, "https://foo4.com/bar/alpha", service, Object.class, "myMetadata");

        Assert.assertFalse("Should contain data", requestContext.isEmpty());
        Assert.assertEquals("Should only be one element", 1, requestContext.size());

        Assert.assertEquals("Should be correct url", "https://foo4.com/services/Soap/m/myMetadata", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating an partner port using a proper url.
     */
    @Test
    public void test_createPort_partner() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.PARTNER_TYPE, "https://foo5.com/bar/alpha", service, Object.class, "myPartner");

        Assert.assertFalse("Should contain data", requestContext.isEmpty());
        Assert.assertEquals("Should only be one element", 1, requestContext.size());

        Assert.assertEquals("Should be correct url", "https://foo5.com/services/Soap/u/myPartner", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating a tooling port using a proper url.
     */
    @Test
    public void test_createPort_tooling() {
        new AbstractPortFactoryStub().createPort(WebServiceSubUrlEnum.TOOLING_TYPE, "https://foo6.com/bar/alpha", service, Object.class, "myTooling");

        Assert.assertFalse("Should contain data", requestContext.isEmpty());
        Assert.assertEquals("Should only be one element", 1, requestContext.size());

        Assert.assertEquals("Should be correct url", "https://foo6.com/services/Soap/T/myTooling", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating a session port with a null service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createSessionPort_Service_null() {
        new AbstractPortFactoryStub().createSessionPort(WebServiceSubUrlEnum.ENTERPRISE_TYPE, sessionId, null, Object.class, namespaceUri, sessionId);
    }

    /**
     * Test creating a session port with an apex service.
     */
    @Test()
    public void test_createSessionPort_apex() {
        final BindingProvider bindingProvider = (BindingProvider) new AbstractPortFactoryStub().createSessionPort(WebServiceSubUrlEnum.APEX_TYPE, "https://foo7.com/bar/alpha", service, Object.class, "myApex1", sessionId);

        Assert.assertNotNull("Should have a binding provider", bindingProvider);
        final SessionIdSoapRequestHeaderHandler handler = (SessionIdSoapRequestHeaderHandler) binding.getHandlerChain().get(0);
        Assert.assertEquals("Should be same session id", sessionId, handler.getValue());

        Assert.assertEquals("Should be correct url", "https://foo7.com/services/Soap/s/myApex1", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating a session port with a custom service.
     */
    @Test()
    public void test_createSessionPort_custom() {
        final BindingProvider bindingProvider = (BindingProvider) new AbstractPortFactoryStub().createSessionPort(WebServiceSubUrlEnum.CUSTOM_TYPE, "https://foo8.com/bar/alpha", service, Object.class, "myCustom1", sessionId);

        Assert.assertNotNull("Should have a binding provider", bindingProvider);
        final SessionIdSoapRequestHeaderHandler handler = (SessionIdSoapRequestHeaderHandler) binding.getHandlerChain().get(0);
        Assert.assertEquals("Should be same session id", sessionId, handler.getValue());

        Assert.assertEquals("Should be correct url", "https://foo8.com/services/Soap/class/myCustom1", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating a session port with an enterprise service.
     */
    @Test()
    public void test_createSessionPort_enterprise() {
        final BindingProvider bindingProvider = (BindingProvider) new AbstractPortFactoryStub().createSessionPort(WebServiceSubUrlEnum.ENTERPRISE_TYPE, "https://foo9.com/bar/alpha", service, Object.class, "myEnterprise1", sessionId);

        Assert.assertNotNull("Should have a binding provider", bindingProvider);
        final SessionIdSoapRequestHeaderHandler handler = (SessionIdSoapRequestHeaderHandler) binding.getHandlerChain().get(0);
        Assert.assertEquals("Should be same session id", sessionId, handler.getValue());

        Assert.assertEquals("Should be correct url", "https://foo9.com/services/Soap/c/myEnterprise1", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating a session port with a metadata service.
     */
    @Test()
    public void test_createSessionPort_metadata() {
        final BindingProvider bindingProvider = (BindingProvider) new AbstractPortFactoryStub().createSessionPort(WebServiceSubUrlEnum.METADATA_TYPE, "https://foo10.com/bar/alpha", service, Object.class, "myMetadata1", sessionId);

        Assert.assertNotNull("Should have a binding provider", bindingProvider);
        final SessionIdSoapRequestHeaderHandler handler = (SessionIdSoapRequestHeaderHandler) binding.getHandlerChain().get(0);
        Assert.assertEquals("Should be same session id", sessionId, handler.getValue());

        Assert.assertEquals("Should be correct url", "https://foo10.com/services/Soap/m/myMetadata1", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating a session port with a partner service.
     */
    @Test()
    public void test_createSessionPort_partner() {
        final BindingProvider bindingProvider = (BindingProvider) new AbstractPortFactoryStub().createSessionPort(WebServiceSubUrlEnum.PARTNER_TYPE, "https://foo11.com/bar/alpha", service, Object.class, "myPartner1", sessionId);

        Assert.assertNotNull("Should have a binding provider", bindingProvider);
        final SessionIdSoapRequestHeaderHandler handler = (SessionIdSoapRequestHeaderHandler) binding.getHandlerChain().get(0);
        Assert.assertEquals("Should be same session id", sessionId, handler.getValue());

        Assert.assertEquals("Should be correct url", "https://foo11.com/services/Soap/u/myPartner1", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    /**
     * Test creating a session port with a tooling service.
     */
    @Test()
    public void test_createSessionPort_tooling() {
        final BindingProvider bindingProvider = (BindingProvider) new AbstractPortFactoryStub().createSessionPort(WebServiceSubUrlEnum.TOOLING_TYPE, "https://foo12.com/bar/alpha", service, Object.class, "myTooling1", sessionId);

        Assert.assertNotNull("Should have a binding provider", bindingProvider);
        final SessionIdSoapRequestHeaderHandler handler = (SessionIdSoapRequestHeaderHandler) binding.getHandlerChain().get(0);
        Assert.assertEquals("Should be same session id", sessionId, handler.getValue());

        Assert.assertEquals("Should be correct url", "https://foo12.com/services/Soap/T/myTooling1", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }
}
