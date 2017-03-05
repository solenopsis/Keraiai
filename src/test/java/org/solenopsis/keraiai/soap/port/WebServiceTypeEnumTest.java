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

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
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

/**
 * Tests the WebServiceTypeEnum.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class WebServiceTypeEnumTest {

    public static interface StubInterface {

        int computeFoo();
    }

    public static class StubImpl implements StubInterface {

        @Override
        public int computeFoo() {
            return 10;
        }
    }

    /**
     * Simple stub class for examining web endpoint methods.
     */
    @WebServiceClient(targetNamespace = "Target_Namespace", name = "Name")
    public static class StubService extends Service {

        @WebEndpoint(name = "Web_Endpoint_Method")
        public StubInterface webEndpointMethod() {
            return new StubImpl();
        }

        public void nonWebEndpointMethod() {
        }

        public StubService(final URL url) {
            super(WebServiceTypeEnumTest.class.getResource("/TestService.wsdl"), new QName("urn:foo.bar", "TestService"));
        }

        public StubService() {
            this(WebServiceTypeEnumTest.class.getResource("/TestService.wsdl"));
        }

        public StubInterface getPortType(final Class portType) {
            return new StubImpl();
        }

        @Override
        public URL getWSDLDocumentLocation() {
            return WebServiceTypeEnumTest.class.getResource("/TestService.wsdl");
        }
    }

    @Mock
    SecurityMgr securityMgr;

    @Mock
    LoginContext loginContext;

    @Mock
    Credentials credentials;

    @Mock
    Service service;

    String apiVersion;

    String baseUrl;

    @Before
    public void setup() {
        apiVersion = TestUtils.generateUniqueStr();
        baseUrl = TestUtils.generateUniqueStr("http://foo.com/");

        Mockito.when(securityMgr.getSession()).thenReturn(loginContext);
        Mockito.when(securityMgr.getCredentials()).thenReturn(credentials);
        Mockito.when(loginContext.getCredentials()).thenReturn(credentials);
        Mockito.when(loginContext.getBaseServerUrl()).thenReturn(baseUrl);
        Mockito.when(credentials.getApiVersion()).thenReturn(apiVersion);
    }

    /**
     * Tests getWebServiceSubUrl()..
     */
    @Test
    public void test_getWebServiceSubUrl() {
        for (final WebServiceTypeEnum webServiceTypeEnum : WebServiceTypeEnum.values()) {
            Assert.assertNotNull("Should be a sub url", webServiceTypeEnum.getWebServiceSubUrl());
        }
    }

    /**
     * Test getWebService().
     */
    @Test
    public void test_getWebService() {
        for (final WebServiceTypeEnum webServiceTypeEnum : WebServiceTypeEnum.values()) {
            // There is no precomputed web service to use on custom web services -
            // they can't be computed...
            if (webServiceTypeEnum == WebServiceTypeEnum.CUSTOM_SERVICE_TYPE) {
                continue;
            }

            Assert.assertNotNull("Should be a web service", webServiceTypeEnum.getWebService());
        }
    }

    /**
     * Test computeSessionUrl().
     */
    @Test
    public void test_computeSessionUrl_SecurityMgr_Service() {
        for (final WebServiceTypeEnum webServiceTypeEnum : WebServiceTypeEnum.values()) {
            String computedUrl;
            String actualUrl = baseUrl + "/" + webServiceTypeEnum.getWebServiceSubUrl().getPartialUrl() + "/";

            if (WebServiceTypeEnum.CUSTOM_SERVICE_TYPE == webServiceTypeEnum) {
                computedUrl = PortUtils.computeSessionUrl(securityMgr, webServiceTypeEnum, new StubService());
                actualUrl += "Web_Endpoint_Method";
            } else {
                computedUrl = PortUtils.computeSessionUrl(securityMgr, webServiceTypeEnum, webServiceTypeEnum.getWebService().getService());
                actualUrl += apiVersion;
            }

            Assert.assertEquals("Should be the correct URL", actualUrl, computedUrl);
        }
    }

    /**
     * Test creating a session port.
     */
    @Test
    public void test_createSessionPort_SecurityMgr_Service_Class() {
        for (final WebServiceTypeEnum webServiceTypeEnum : WebServiceTypeEnum.values()) {
            Object computedPort;
            Class expectedPortClass;

            if (WebServiceTypeEnum.CUSTOM_SERVICE_TYPE == webServiceTypeEnum) {
                computedPort = webServiceTypeEnum.createSessionPort(securityMgr, new StubService(), StubInterface.class);
                expectedPortClass = StubInterface.class;

            } else {
                computedPort = webServiceTypeEnum.createSessionPort(securityMgr, webServiceTypeEnum.getWebService().getService(), webServiceTypeEnum.getWebService().getPortType());
                expectedPortClass = webServiceTypeEnum.getWebService().getPortType();
            }

            Assert.assertTrue("Should be the correct type of port", expectedPortClass.isAssignableFrom(computedPort.getClass()));
        }
    }

    /**
     * Test creating a session port.
     */
    @Test
    public void test_createSessionPort_SecurityMgr_Service() {
        for (final WebServiceTypeEnum webServiceTypeEnum : WebServiceTypeEnum.values()) {
            Object computedPort;
            Class expectedPortClass;

            if (WebServiceTypeEnum.CUSTOM_SERVICE_TYPE == webServiceTypeEnum) {
                computedPort = webServiceTypeEnum.createSessionPort(securityMgr, new StubService());
                expectedPortClass = StubInterface.class;

            } else {
                computedPort = webServiceTypeEnum.createSessionPort(securityMgr, webServiceTypeEnum.getWebService().getService());
                expectedPortClass = webServiceTypeEnum.getWebService().getPortType();
            }

            Assert.assertTrue("Should be the correct type of port", expectedPortClass.isAssignableFrom(computedPort.getClass()));
        }
    }

    /**
     * Test creating a session port.
     */
    @Test
    public void test_createSessionPort_SecurityMgr_Class_URL() {
        for (final WebServiceTypeEnum webServiceTypeEnum : WebServiceTypeEnum.values()) {
            Object computedPort;
            Class expectedPortClass;

            if (WebServiceTypeEnum.CUSTOM_SERVICE_TYPE == webServiceTypeEnum) {
                computedPort = webServiceTypeEnum.createSessionPort(securityMgr, StubService.class, WebServiceTypeEnumTest.class.getResource("/TestService.wsdl"));
                expectedPortClass = StubInterface.class;

            } else {
                computedPort = webServiceTypeEnum.createSessionPort(securityMgr, webServiceTypeEnum.getWebService().getService().getClass(), webServiceTypeEnum.getWebService().getService().getWSDLDocumentLocation());
                expectedPortClass = webServiceTypeEnum.getWebService().getPortType();
            }

            Assert.assertTrue("Should be the correct type of port", expectedPortClass.isAssignableFrom(computedPort.getClass()));
        }
    }

    /**
     * Test creating a session port.
     */
    @Test
    public void test_createSessionPort_SecurityMgr_Class_String() {
        for (final WebServiceTypeEnum webServiceTypeEnum : WebServiceTypeEnum.values()) {
            Object computedPort;
            Class expectedPortClass;

            if (WebServiceTypeEnum.CUSTOM_SERVICE_TYPE == webServiceTypeEnum) {
                computedPort = webServiceTypeEnum.createSessionPort(securityMgr, StubService.class, "file://" + WebServiceTypeEnumTest.class.getResource("/TestService.wsdl").getFile());
                expectedPortClass = StubInterface.class;

            } else {
                computedPort = webServiceTypeEnum.createSessionPort(securityMgr, webServiceTypeEnum.getWebService().getService().getClass(), "file://" + webServiceTypeEnum.getWebService().getService().getWSDLDocumentLocation().getFile());
                expectedPortClass = webServiceTypeEnum.getWebService().getPortType();
            }

            Assert.assertTrue("Should be the correct type of port", expectedPortClass.isAssignableFrom(computedPort.getClass()));
        }
    }
}
