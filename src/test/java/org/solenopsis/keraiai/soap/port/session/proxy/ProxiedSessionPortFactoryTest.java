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
package org.solenopsis.keraiai.soap.port.session.proxy;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.soap.WebServiceTypeEnum;
import org.solenopsis.keraiai.soap.security.LoginContext;
import org.solenopsis.keraiai.soap.security.SecurityMgr;
import org.solenopsis.keraiai.wsdl.enterprise.Soap;

/**
 * Tests the ProxiedSessionPortFactory.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class ProxiedSessionPortFactoryTest {

    @Mock
    SecurityMgr securityMgr;

    StubService service;

    @Mock
    LoginContext loginContext;

    String url;

    String name;

    String sessionId;

    @Mock(extraInterfaces = {BindingProvider.class})
    Soap port;

    @Mock
    Binding binding;

    ProxiedSessionPortFactory factory;

    @Before
    public void setup() {
        factory = new ProxiedSessionPortFactory(securityMgr);

        Mockito.when(securityMgr.getSession()).thenReturn(loginContext);

        url = TestUtils.generateUniqueStr("http://foo.com");
        Mockito.when(loginContext.getServerUrl()).thenReturn(url);

        name = TestUtils.generateUniqueStr();

        sessionId = TestUtils.generateUniqueStr("sessionId");
        Mockito.when(loginContext.getSessionId()).thenReturn(sessionId);

        service = new StubService(port);

        Mockito.when(((BindingProvider) port).getBinding()).thenReturn(binding);
    }

    /**
     * Test the constructor with a null SecurityMgr.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null() {
        new ProxiedSessionPortFactory(null);
    }

    /**
     * Test the doCreateSessionPort() method - null WebServiceTypeEnum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_doCreateSessionPort_null_WebServiceTypeEnum() {
        factory.doCreateSessionPort(null, service, Soap.class, name, loginContext);
    }

    /**
     * Test the doCreateSessionPort() method - null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_doCreateSessionPort_null_Service() {
        factory.doCreateSessionPort(WebServiceTypeEnum.ENTERPRISE_TYPE, null, Soap.class, name, loginContext);
    }

    /**
     * Test the doCreateSessionPort() method - null Port type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_doCreateSessionPort_null_PortType() {
        factory.doCreateSessionPort(WebServiceTypeEnum.ENTERPRISE_TYPE, service, null, name, loginContext);
    }

    /**
     * Test the doCreateSessionPort() method - null login context.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_doCreateSessionPort_null_LoginContext() {
        factory.doCreateSessionPort(WebServiceTypeEnum.ENTERPRISE_TYPE, service, Soap.class, name, null);
    }

    /**
     * Test the doCreateSessionPort() method.
     */
    @Test
    public void test_doCreateSessionPort() {
        Assert.assertNotNull("Should have a created a Soap port", factory.doCreateSessionPort(WebServiceTypeEnum.ENTERPRISE_TYPE, service, Soap.class, name, loginContext));
    }

    /**
     * Test the createSessionPort() method with a null WebServiceTypeEnum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createSessionPort_null_WebServiceTypeEnum() {
        factory.createSessionPort(null, service, Soap.class, name);
    }

    /**
     * Test the createSessionPort() method with a null WebServiceTypeEnum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createSessionPort_null_Service() {
        factory.createSessionPort(WebServiceTypeEnum.ENTERPRISE_TYPE, null, Soap.class, name);
    }

    /**
     * Test the createSessionPort() method with a null Port type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createSessionPort_null_PortType() {
        factory.createSessionPort(WebServiceTypeEnum.ENTERPRISE_TYPE, service, null, name);
    }

    /**
     * Test the createSessionPort() method.
     */
    @Test
    public void test_createSessionPort() {
        Assert.assertNotNull("Should have created a Soap port", factory.createSessionPort(WebServiceTypeEnum.ENTERPRISE_TYPE, service, Soap.class, name));
    }
}
