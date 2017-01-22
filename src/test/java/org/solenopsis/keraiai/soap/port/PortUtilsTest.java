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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.solenopsis.keraiai.soap.port;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.LoginContext;
import org.solenopsis.keraiai.SecurityMgr;
import org.solenopsis.keraiai.soap.utils.ExceptionUtils;
import org.solenopsis.keraiai.wsdl.tooling.SforceServicePortType;
import org.solenopsis.keraiai.wsdl.tooling.SforceServiceService;

/**
 * Test the PortUtils class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class PortUtilsTest {

    class Stub {

        public void someMethod() {

        }
    }

    class TestException extends Exception {

    }

    @Mock
    Service service;

    @Mock
    SecurityMgr securityMgr;

    @Mock
    Credentials credentials;

    @Mock
    LoginContext session;

    @Before
    public void setup() {
        Mockito.when(securityMgr.getCredentials()).thenReturn(credentials);
        Mockito.when(securityMgr.getSession()).thenReturn(session);
        Mockito.when(session.getSessionId()).thenReturn(TestUtils.generateUniqueStr());
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void test_constructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Constructor constructor = PortUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance(new Object[0]);
    }

    /**
     * Test computing a session header name with a null String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_String_null() {
        PortUtils.computeSessionHeaderName((String) null);
    }

    /**
     * Test computing a session header name with an empty String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_String_empty() {
        PortUtils.computeSessionHeaderName("");
    }

    /**
     * Test computing a session header name with a blank String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_String_blank() {
        PortUtils.computeSessionHeaderName("    ");
    }

    /**
     * Test computing session header name with a String.
     */
    @Test
    public void test_computeSessionHeaderName_String() {
        final String namespaceUri = TestUtils.generateUniqueStr();

        final QName toCompare = new QName(namespaceUri, PortUtils.SESSION_HEADER);

        Assert.assertEquals("Should be the same QName", toCompare, PortUtils.computeSessionHeaderName(namespaceUri));
    }

    /**
     * Test computing a session header name with a null QName.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_QName_null() {
        PortUtils.computeSessionHeaderName((QName) null);
    }

    /**
     * Test computing a session header name with a null QName namespaceURI.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_QName_null_namespaceUri() {
        final QName qname = new QName(null, "foo");
        PortUtils.computeSessionHeaderName(qname);
    }

    /**
     * Test computing a session header name with an empty QName namespaceURI.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_QName_empty_namespaceUri() {
        final QName qname = new QName("", "foo");
        PortUtils.computeSessionHeaderName(qname);
    }

    /**
     * Test computing a session header name with a blank QName namespaceURI.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_QName_blank_namespaceUri() {
        final QName qname = new QName("  ", "foo");
        PortUtils.computeSessionHeaderName(qname);
    }

    /**
     * Test computing a session header name with a QName.
     */
    @Test
    public void test_computeSessionHeaderName_QName() {
        final String namespaceUri = TestUtils.generateUniqueStr("name");
        final QName qname = new QName(namespaceUri, TestUtils.generateUniqueStr("foo"));
        final QName toCompare = new QName(namespaceUri, PortUtils.SESSION_HEADER);

        Assert.assertEquals("QNames should be equal", toCompare, PortUtils.computeSessionHeaderName(qname));
    }

    /**
     * Test computing a session header name with a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_Service_null() {
        PortUtils.computeSessionHeaderName((Service) null);
    }

    /**
     * Test computing a session header name with a Service whose QName namespaceURI is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_Service_null_namespaceUri() {
        final QName qname = new QName(null, "foo");

        Mockito.when(service.getServiceName()).thenReturn(qname);

        PortUtils.computeSessionHeaderName(service);
    }

    /**
     * Test computing a session header name with a Service whose QName namespaceURI is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_Service_empty_namespaceUri() {
        final QName qname = new QName("", "foo");

        Mockito.when(service.getServiceName()).thenReturn(qname);

        PortUtils.computeSessionHeaderName(service);
    }

    /**
     * Test computing a session header name with a Service whose QName namespaceURI is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderName_Service_blank_namespaceUri() {
        final QName qname = new QName("  ", "foo");

        Mockito.when(service.getServiceName()).thenReturn(qname);

        PortUtils.computeSessionHeaderName(service);
    }

    /**
     * Test computing a session header name with a Service.
     */
    @Test
    public void test_computeSessionHeaderName_Service() {
        final String namespaceUri = TestUtils.generateUniqueStr("name");
        final QName qname = new QName(namespaceUri, TestUtils.generateUniqueStr("foo"));

        Mockito.when(service.getServiceName()).thenReturn(qname);

        final QName toCompare = new QName(namespaceUri, PortUtils.SESSION_HEADER);

        Assert.assertEquals("QNames should be equal", toCompare, PortUtils.computeSessionHeaderName(service));
    }

    /**
     * Test a call being re-tryable.
     */
    @Test
    public void test_isCallRetriable() {
        for (int i = 0; i < PortUtils.MAX_RETRIES; i++) {
            Assert.assertTrue("Should be re-tryable", PortUtils.isCallRetriable(i));
        }

        for (int i = PortUtils.MAX_RETRIES; i < PortUtils.MAX_RETRIES * 2; i++) {
            Assert.assertFalse("Should NOT be re-tryable", PortUtils.isCallRetriable(i));
        }
    }

    /**
     * Test processException for a non-relogin exception.
     */
    @Test(expected = TestException.class)
    public void test_processException_noRelogin() throws NoSuchMethodException, Throwable {
        final Method method = Stub.class.getDeclaredMethod("someMethod", new Class[0]);

        PortUtils.processException(new TestException(), method);
    }

    /**
     * Test processException for relogin exceptions.
     */
    @Test
    public void test_processException() throws NoSuchMethodException, Throwable {
        final Method method = Stub.class.getDeclaredMethod("someMethod", new Class[0]);

        PortUtils.processException(new NullPointerException(ExceptionUtils.INVALID_SESSION_ID), method);
        PortUtils.processException(new IllegalArgumentException(TestUtils.generateUniqueStr(), new IOException(TestUtils.generateUniqueStr())), method);
    }

    /**
     * Test computing if a WebServiceTypeEnum is a custom web service.
     */
    @Test
    public void test_isWebServiceCustomService() {
        Assert.assertFalse("Should not be a custom web service", PortUtils.isWebServiceCustomService(null));
        Assert.assertFalse("Should not be a custom web service", PortUtils.isWebServiceCustomService(WebServiceTypeEnum.APEX_SERVICE_TYPE));
        Assert.assertFalse("Should not be a custom web service", PortUtils.isWebServiceCustomService(WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE));
        Assert.assertFalse("Should not be a custom web service", PortUtils.isWebServiceCustomService(WebServiceTypeEnum.METADATA_SERVICE_TYPE));
        Assert.assertFalse("Should not be a custom web service", PortUtils.isWebServiceCustomService(WebServiceTypeEnum.PARTNER_SERVICE_TYPE));
        Assert.assertFalse("Should not be a custom web service", PortUtils.isWebServiceCustomService(WebServiceTypeEnum.TOOLING_SERVICE_TYPE));

        Assert.assertTrue("Should be a custom web service", PortUtils.isWebServiceCustomService(WebServiceTypeEnum.CUSTOM_SERVICE_TYPE));
    }

    /**
     * Test computing a port name using a null WebServiceTypeEnum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_String_Service_nullWebServiceTypeEnum() {
        PortUtils.computePortName(null, TestUtils.generateUniqueStr(), service);
    }

    /**
     * Test computing a port name using a null String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_String_Service_nullString() {
        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, (String) null, service);
    }

    /**
     * Test computing a port name using a blank String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_String_Service_blankString() {
        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, "  ", service);
    }

    /**
     * Test computing a port name using an empty String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_String_Service_emptyString() {
        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, "", service);
    }

    /**
     * Test computing a port name using a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_String_Service_nullService() {
        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, TestUtils.generateUniqueStr(), null);
    }

    /**
     * Test computing a port name.
     */
    @Test
    public void test_computePortName_WebServiceTypeEnum_String_Service() {
        final String apiVersion = TestUtils.generateUniqueStr("Some", "Thing");

        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, apiVersion, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, apiVersion, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.METADATA_SERVICE_TYPE, apiVersion, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.PARTNER_SERVICE_TYPE, apiVersion, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.TOOLING_SERVICE_TYPE, apiVersion, service));

        Assert.assertEquals("Should be the correct port name", "SforceService", PortUtils.computePortName(WebServiceTypeEnum.CUSTOM_SERVICE_TYPE, apiVersion, new SforceServiceService()));
    }

    /**
     * Test computing a port name using a null WebServiceTypeEnum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_Credentials_Service_nullWebServiceTypeEnum() {
        PortUtils.computePortName(null, credentials, service);
    }

    /**
     * Test computing a port name using null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_Credentials_Service_nullCredentials() {
        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, (Credentials) null, service);
    }

    /**
     * Test computing a port name using a null String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_Credentials_Service_nullString() {
        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, credentials, service);
    }

    /**
     * Test computing a port name using a blank String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_Credentials_Service_blankString() {
        Mockito.when(credentials.getApiVersion()).thenReturn("  ");

        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, credentials, service);
    }

    /**
     * Test computing a port name using an empty String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_Credentials_Service_emptyString() {
        Mockito.when(credentials.getApiVersion()).thenReturn("");

        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, credentials, service);
    }

    /**
     * Test computing a port name using a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_Credentials_Service_nullService() {
        Mockito.when(credentials.getApiVersion()).thenReturn(TestUtils.generateUniqueStr());

        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, credentials, null);
    }

    /**
     * Test computing a port name.
     */
    @Test
    public void test_computePortName_WebServiceTypeEnum_Credentials_Service() {
        final String apiVersion = TestUtils.generateUniqueStr("Some1", "Thing1");
        Mockito.when(credentials.getApiVersion()).thenReturn(apiVersion);

        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, credentials, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, credentials, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.METADATA_SERVICE_TYPE, credentials, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.PARTNER_SERVICE_TYPE, credentials, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.TOOLING_SERVICE_TYPE, credentials, service));

        Assert.assertEquals("Should be the correct port name", "SforceService", PortUtils.computePortName(WebServiceTypeEnum.CUSTOM_SERVICE_TYPE, credentials, new SforceServiceService()));
    }

    /**
     * Test computing a port name using a null SecurityMgr.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_SecurityMgr_Service_nullSecurityMgr() {
        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, (SecurityMgr) null, service);
    }

    /**
     * Test computing a port name using a null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_SecurityMgr_Service_nullCredentials() {
        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, Mockito.mock(SecurityMgr.class), service);
    }

    /**
     * Test computing a port name using a null API version String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_SecurityMgr_Service_nullString() {
        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, securityMgr, service);
    }

    /**
     * Test computing a port name using a blank String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_SecurityMgr_Service_blankString() {
        Mockito.when(credentials.getApiVersion()).thenReturn("  ");

        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, securityMgr, service);
    }

    /**
     * Test computing a port name using an empty String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_SecurityMgr_Service_emptyString() {
        Mockito.when(credentials.getApiVersion()).thenReturn("");

        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, securityMgr, service);
    }

    /**
     * Test computing a port name using a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortName_WebServiceTypeEnum_SecurityMgr_Service_nullService() {
        Mockito.when(credentials.getApiVersion()).thenReturn(TestUtils.generateUniqueStr());

        PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, securityMgr, null);
    }

    /**
     * Test computing a port name.
     */
    @Test
    public void test_computePortName_WebServiceTypeEnum_SecurityMgr_Service() {
        final String apiVersion = TestUtils.generateUniqueStr("Some1", "Thing1");
        Mockito.when(credentials.getApiVersion()).thenReturn(apiVersion);

        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.APEX_SERVICE_TYPE, securityMgr, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, securityMgr, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.METADATA_SERVICE_TYPE, securityMgr, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.PARTNER_SERVICE_TYPE, securityMgr, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortName(WebServiceTypeEnum.TOOLING_SERVICE_TYPE, securityMgr, service));

        Assert.assertEquals("Should be the correct port name", "SforceService", PortUtils.computePortName(WebServiceTypeEnum.CUSTOM_SERVICE_TYPE, securityMgr, new SforceServiceService()));
    }

    /**
     * Test creating a session port using a null SecurityMgr.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createSessionPort_SecurityMgr_null() {
        PortUtils.createSessionPort(null, service, SforceServicePortType.class, "https://login.salesforce.com");
    }

    /**
     * Test creating a session port using a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createSessionPort_Service_null() {
        PortUtils.createSessionPort(securityMgr, null, SforceServicePortType.class, "https://login.salesforce.com");
    }

    /**
     * Test creating a session port using a null port type..
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createSessionPort_Class_null() {
        PortUtils.createSessionPort(securityMgr, service, null, "https://login.salesforce.com");
    }

    /**
     * Test creating a session port using a null url..
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createSessionPort_String_null() {
        PortUtils.createSessionPort(securityMgr, service, SforceServicePortType.class, null);
    }

    /**
     * Test creating a session port using a blank url..
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createSessionPort_String_blank() {
        PortUtils.createSessionPort(securityMgr, service, SforceServicePortType.class, "  ");
    }

    /**
     * Test creating a session port using an empty url..
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createSessionPort_String_empty() {
        PortUtils.createSessionPort(securityMgr, service, SforceServicePortType.class, "");
    }

    /**
     * Test creating a session port...
     */
    @Test
    public void test_createSessionPort() {
        final Object port = PortUtils.createSessionPort(securityMgr, new SforceServiceService(), SforceServicePortType.class, "https://login.salesforce.com");

        Assert.assertNotNull("Should have created a port", port);

        final BindingProvider bindingProvider = (BindingProvider) port;
        Assert.assertNotNull("Should have a handler", bindingProvider.getBinding().getHandlerChain());
        Assert.assertEquals("Should have one handler", 1, bindingProvider.getBinding().getHandlerChain().size());
        Assert.assertEquals("Should be a session id handler", SessionIdSoapRequestHeaderHandler.class, bindingProvider.getBinding().getHandlerChain().get(0).getClass());

        Assert.assertEquals("Should have the correct url", "https://login.salesforce.com", bindingProvider.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }
}
