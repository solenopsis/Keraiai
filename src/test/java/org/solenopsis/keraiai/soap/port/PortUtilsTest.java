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
import org.solenopsis.keraiai.soap.WebServiceSubUrlEnum;
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
    public void test_computeSessionHeaderNameForNamespace_null() {
        PortUtils.computeSessionHeaderNameForNamespace((String) null);
    }

    /**
     * Test computing a session header name with an empty String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderNameForNamespace_empty() {
        PortUtils.computeSessionHeaderNameForNamespace("");
    }

    /**
     * Test computing a session header name with a blank String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderNameForNamespace_blank() {
        PortUtils.computeSessionHeaderNameForNamespace("    ");
    }

    /**
     * Test computing session header name with a String.
     */
    @Test
    public void test_computeSessionHeaderNameForNamespace() {
        final String namespaceUri = TestUtils.generateUniqueStr();

        final QName toCompare = new QName(namespaceUri, PortUtils.SESSION_HEADER);

        Assert.assertEquals("Should be the same QName", toCompare, PortUtils.computeSessionHeaderNameForNamespace(namespaceUri));
    }

    /**
     * Test computing a session header name with a null QName.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderNameForQname_null() {
        PortUtils.computeSessionHeaderNameForQname((QName) null);
    }

    /**
     * Test computing a session header name with a null QName namespaceURI.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderNameForQname_null_namespaceUri() {
        PortUtils.computeSessionHeaderNameForQname(new QName(null, "foo"));
    }

    /**
     * Test computing a session header name with an empty QName namespaceURI.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderNameForQname_empty_namespaceUri() {
        PortUtils.computeSessionHeaderNameForQname(new QName("", "foo"));
    }

    /**
     * Test computing a session header name with a blank QName namespaceURI.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderNameForQname_blank_namespaceUri() {
        PortUtils.computeSessionHeaderNameForQname(new QName("  ", "foo"));
    }

    /**
     * Test computing a session header name with a QName.
     */
    @Test
    public void test_computeSessionHeaderNameForQname() {
        final String namespaceUri = TestUtils.generateUniqueStr("name");
        final QName qname = new QName(namespaceUri, TestUtils.generateUniqueStr("foo"));
        final QName toCompare = new QName(namespaceUri, PortUtils.SESSION_HEADER);

        Assert.assertEquals("QNames should be equal", toCompare, PortUtils.computeSessionHeaderNameForQname(qname));
    }

    /**
     * Test computing a session header name with a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderNameForService_null() {
        PortUtils.computeSessionHeaderNameForService((Service) null);
    }

    /**
     * Test computing a session header name with a Service whose QName namespaceURI is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderNameForService_null_namespaceUri() {
        Mockito.when(service.getServiceName()).thenReturn(new QName(null, "foo"));

        PortUtils.computeSessionHeaderNameForService(service);
    }

    /**
     * Test computing a session header name with a Service whose QName namespaceURI is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderNameForService_empty_namespaceUri() {
        Mockito.when(service.getServiceName()).thenReturn(new QName("", "foo"));

        PortUtils.computeSessionHeaderNameForService(service);
    }

    /**
     * Test computing a session header name with a Service whose QName namespaceURI is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionHeaderNameForService_blank_namespaceUri() {
        Mockito.when(service.getServiceName()).thenReturn(new QName("  ", "foo"));

        PortUtils.computeSessionHeaderNameForService(service);
    }

    /**
     * Test computing a session header name with a Service.
     */
    @Test
    public void test_computeSessionHeaderNameForService() {
        final String namespaceUri = TestUtils.generateUniqueStr("name");
        final QName qname = new QName(namespaceUri, TestUtils.generateUniqueStr("foo"));

        Mockito.when(service.getServiceName()).thenReturn(qname);

        final QName toCompare = new QName(namespaceUri, PortUtils.SESSION_HEADER);

        Assert.assertEquals("QNames should be equal", toCompare, PortUtils.computeSessionHeaderNameForService(service));
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
    public void test_isCustomWebService() {
        Assert.assertFalse("Should not be a custom web service", PortUtils.isCustomWebService(null));
        Assert.assertFalse("Should not be a custom web service", PortUtils.isCustomWebService(WebServiceTypeEnum.APEX_SERVICE_TYPE));
        Assert.assertFalse("Should not be a custom web service", PortUtils.isCustomWebService(WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE));
        Assert.assertFalse("Should not be a custom web service", PortUtils.isCustomWebService(WebServiceTypeEnum.METADATA_SERVICE_TYPE));
        Assert.assertFalse("Should not be a custom web service", PortUtils.isCustomWebService(WebServiceTypeEnum.PARTNER_SERVICE_TYPE));
        Assert.assertFalse("Should not be a custom web service", PortUtils.isCustomWebService(WebServiceTypeEnum.TOOLING_SERVICE_TYPE));

        Assert.assertTrue("Should be a custom web service", PortUtils.isCustomWebService(WebServiceTypeEnum.CUSTOM_SERVICE_TYPE));
    }

    /**
     * Test computing a port name using a null WebServiceTypeEnum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromApiVersion_nullWebServiceTypeEnum() {
        PortUtils.computePortNameFromApiVersion(TestUtils.generateUniqueStr(), null, service);
    }

    /**
     * Test computing a port name using a null String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromApiVersion_nullString() {
        PortUtils.computePortNameFromApiVersion(null, WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using a blank String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromApiVersion_blankString() {
        PortUtils.computePortNameFromApiVersion("  ", WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using an empty String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromApiVersion_emptyString() {
        PortUtils.computePortNameFromApiVersion("", WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromApiVersion_nullService() {
        PortUtils.computePortNameFromApiVersion(TestUtils.generateUniqueStr(), WebServiceTypeEnum.APEX_SERVICE_TYPE, null);
    }

    /**
     * Test computing a port name.
     */
    @Test
    public void test_computePortNameFromApiVersion() {
        final String apiVersion = TestUtils.generateUniqueStr("Some", "Thing");

        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromApiVersion(apiVersion, WebServiceTypeEnum.APEX_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromApiVersion(apiVersion, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromApiVersion(apiVersion, WebServiceTypeEnum.METADATA_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromApiVersion(apiVersion, WebServiceTypeEnum.PARTNER_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromApiVersion(apiVersion, WebServiceTypeEnum.TOOLING_SERVICE_TYPE, service));

        Assert.assertEquals("Should be the correct port name", "SforceService", PortUtils.computePortNameFromApiVersion(apiVersion, WebServiceTypeEnum.CUSTOM_SERVICE_TYPE, new SforceServiceService()));
    }

    /**
     * Test computing a port name using a null WebServiceTypeEnum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromCredentials_nullWebServiceTypeEnum() {
        PortUtils.computePortNameFromCredentials(credentials, null, service);
    }

    /**
     * Test computing a port name using null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromCredentials_nullCredentials() {
        PortUtils.computePortNameFromCredentials(null, WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using a null String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromCredentials_nullString() {
        PortUtils.computePortNameFromCredentials(credentials, WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using a blank String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromCredentials_blankString() {
        Mockito.when(credentials.getApiVersion()).thenReturn("  ");

        PortUtils.computePortNameFromCredentials(credentials, WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using an empty String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromCredentials_emptyString() {
        Mockito.when(credentials.getApiVersion()).thenReturn("");

        PortUtils.computePortNameFromCredentials(credentials, WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromCredentials_nullService() {
        Mockito.when(credentials.getApiVersion()).thenReturn(TestUtils.generateUniqueStr());

        PortUtils.computePortNameFromCredentials(credentials, WebServiceTypeEnum.APEX_SERVICE_TYPE, null);
    }

    /**
     * Test computing a port name.
     */
    @Test
    public void test_computePortNameFromCredentials() {
        final String apiVersion = TestUtils.generateUniqueStr("Some1", "Thing1");
        Mockito.when(credentials.getApiVersion()).thenReturn(apiVersion);

        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromCredentials(credentials, WebServiceTypeEnum.APEX_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromCredentials(credentials, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromCredentials(credentials, WebServiceTypeEnum.METADATA_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromCredentials(credentials, WebServiceTypeEnum.PARTNER_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromCredentials(credentials, WebServiceTypeEnum.TOOLING_SERVICE_TYPE, service));

        Assert.assertEquals("Should be the correct port name", "SforceService", PortUtils.computePortNameFromCredentials(credentials, WebServiceTypeEnum.CUSTOM_SERVICE_TYPE, new SforceServiceService()));
    }

    /**
     * Test computing a port name using a null SecurityMgr.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromSecurityMgr_nullSecurityMgr() {
        PortUtils.computePortNameFromSecurityMgr(null, WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using a null credentials.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromSecurityMgr_nullCredentials() {
        PortUtils.computePortNameFromSecurityMgr(Mockito.mock(SecurityMgr.class), WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using a null API version String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromSecurityMgr_nullString() {
        PortUtils.computePortNameFromSecurityMgr(securityMgr, WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using a blank String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromSecurityMgr_blankString() {
        Mockito.when(credentials.getApiVersion()).thenReturn("  ");

        PortUtils.computePortNameFromSecurityMgr(securityMgr, WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using an empty String.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromSecurityMgr_emptyString() {
        Mockito.when(credentials.getApiVersion()).thenReturn("");

        PortUtils.computePortNameFromSecurityMgr(securityMgr, WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a port name using a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computePortNameFromSecurityMgr_nullService() {
        Mockito.when(credentials.getApiVersion()).thenReturn(TestUtils.generateUniqueStr());

        PortUtils.computePortNameFromSecurityMgr(securityMgr, WebServiceTypeEnum.APEX_SERVICE_TYPE, null);
    }

    /**
     * Test computing a port name.
     */
    @Test
    public void test_computePortNameFromSecurityMgr() {
        final String apiVersion = TestUtils.generateUniqueStr("Some1", "Thing1");
        Mockito.when(credentials.getApiVersion()).thenReturn(apiVersion);

        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromSecurityMgr(securityMgr, WebServiceTypeEnum.APEX_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromSecurityMgr(securityMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromSecurityMgr(securityMgr, WebServiceTypeEnum.METADATA_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromSecurityMgr(securityMgr, WebServiceTypeEnum.PARTNER_SERVICE_TYPE, service));
        Assert.assertEquals("Should be API version", apiVersion, PortUtils.computePortNameFromSecurityMgr(securityMgr, WebServiceTypeEnum.TOOLING_SERVICE_TYPE, service));

        Assert.assertEquals("Should be the correct port name", "SforceService", PortUtils.computePortNameFromSecurityMgr(securityMgr, WebServiceTypeEnum.CUSTOM_SERVICE_TYPE, new SforceServiceService()));
    }

    /**
     * Test computing a session url using a null base server url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionUrlFromBaseServerUrl_baseServerUrl_null() {
        PortUtils.computeSessionUrlFromBaseServerUrl(null, WebServiceTypeEnum.APEX_SERVICE_TYPE, service, TestUtils.generateUniqueStr());
    }

    /**
     * Test computing a session url using a blank base server url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionUrlFromBaseServerUrl_baseServerUrl_blank() {
        PortUtils.computeSessionUrlFromBaseServerUrl("  ", WebServiceTypeEnum.APEX_SERVICE_TYPE, service, TestUtils.generateUniqueStr());
    }

    /**
     * Test computing a session url using an empty base server url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionUrlFromBaseServerUrl_baseServerUrl_empty() {
        PortUtils.computeSessionUrlFromBaseServerUrl("", WebServiceTypeEnum.APEX_SERVICE_TYPE, service, TestUtils.generateUniqueStr());
    }

    /**
     * Test computing a session url using a null port name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionUrlFromBaseServerUrl_portName_null() {
        PortUtils.computeSessionUrlFromBaseServerUrl(TestUtils.generateUniqueStr(), WebServiceTypeEnum.APEX_SERVICE_TYPE, service, null);
    }

    /**
     * Test computing a session url using a blank port name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionUrlFromBaseServerUrl_portName_blank() {
        PortUtils.computeSessionUrlFromBaseServerUrl(TestUtils.generateUniqueStr(), WebServiceTypeEnum.APEX_SERVICE_TYPE, service, "  ");
    }

    /**
     * Test computing a session url.
     */
    @Test
    public void test_computeSessionUrlFromBaseServerUrl() {
        final String baseServerUrl = TestUtils.generateUniqueStr("http://");
        final String portName = TestUtils.generateUniqueStr();
        final String toCompare = baseServerUrl + "/" + WebServiceSubUrlEnum.APEX_TYPE.getPartialUrl() + "/" + portName;

        Assert.assertEquals("Should be the correct session url", toCompare, PortUtils.computeSessionUrlFromBaseServerUrl(baseServerUrl, WebServiceTypeEnum.APEX_SERVICE_TYPE, service, portName));
    }

    /**
     * Test computing a session url with a null login context.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionUrlFromLoginContext_null() {
        PortUtils.computeSessionUrlFromLoginContext(null, WebServiceTypeEnum.APEX_SERVICE_TYPE, service, TestUtils.generateUniqueStr());
    }

    /**
     * Test computing a session url with a login context.
     */
    @Test
    public void test_computeSessionUrlFromLoginContext() {
        final String baseServerUrl = TestUtils.generateUniqueStr("http://");
        final String portName = TestUtils.generateUniqueStr();

        Mockito.when(session.getBaseServerUrl()).thenReturn(baseServerUrl);

        final String toCompare = baseServerUrl + "/" + WebServiceSubUrlEnum.ENTERPRISE_TYPE.getPartialUrl() + "/" + portName;

        Assert.assertEquals("Should be the correct session url", toCompare, PortUtils.computeSessionUrlFromBaseServerUrl(baseServerUrl, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service, portName));
    }

    /**
     * Test computing a session url with a null security manager
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeSessionUrl_null() {
        PortUtils.computeSessionUrl(null, WebServiceTypeEnum.APEX_SERVICE_TYPE, service);
    }

    /**
     * Test computing a session url with a login context.
     */
    @Test
    public void test_computeSessionUrl() {
        final String baseServerUrl = TestUtils.generateUniqueStr("http://");
        final String portName = TestUtils.generateUniqueStr();

        Mockito.when(credentials.getApiVersion()).thenReturn(portName);
        Mockito.when(session.getBaseServerUrl()).thenReturn(baseServerUrl);

        final String toCompare = baseServerUrl + "/" + WebServiceSubUrlEnum.TOOLING_TYPE.getPartialUrl() + "/" + portName;

        Assert.assertEquals("Should be the correct session url", toCompare, PortUtils.computeSessionUrl(securityMgr, WebServiceTypeEnum.TOOLING_SERVICE_TYPE, service));
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
