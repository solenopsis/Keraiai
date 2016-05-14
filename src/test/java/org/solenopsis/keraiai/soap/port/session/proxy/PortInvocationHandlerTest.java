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

import java.lang.reflect.InvocationTargetException;
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
import org.solenopsis.keraiai.soap.utils.ExceptionUtils;
import org.solenopsis.keraiai.wsdl.enterprise.Soap;
import org.solenopsis.keraiai.wsdl.enterprise.UnexpectedErrorFault;
import org.solenopsis.keraiai.wsdl.enterprise.UnexpectedErrorFault_Exception;

/**
 * Tests the PortInvocationHandler class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class PortInvocationHandlerTest {

    ProxiedSessionPortFactory proxiedSessionPortFactory;

    @Mock
    SecurityMgr securityMgr;

    StubService service;

    @Mock
    LoginContext loginContext;

    String url;

    String sessionId;

    @Mock(extraInterfaces = {BindingProvider.class})
    Soap port;

    @Mock
    Binding binding;

    PortInvocationHandler portInvocationHandler;

    @Before
    public void setup() {
        proxiedSessionPortFactory = new ProxiedSessionPortFactory(securityMgr);

        Mockito.when(securityMgr.getSession()).thenReturn(loginContext);

        url = TestUtils.generateUniqueStr("http://foo.com");
        Mockito.when(loginContext.getServerUrl()).thenReturn(url);

        sessionId = TestUtils.generateUniqueStr("sessionId");
        Mockito.when(loginContext.getSessionId()).thenReturn(sessionId);

        service = new StubService(port);

        Mockito.when(((BindingProvider) port).getBinding()).thenReturn(binding);

        portInvocationHandler = new PortInvocationHandler(proxiedSessionPortFactory, WebServiceTypeEnum.ENTERPRISE_TYPE, service, Soap.class, TestUtils.generateUniqueStr());
    }

    /**
     * Tests a null ProxiedSessionPortFactory.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null_ProxiedSessionPortFactory() {
        new PortInvocationHandler(null, WebServiceTypeEnum.ENTERPRISE_TYPE, service, Soap.class, TestUtils.generateUniqueStr());
    }

    /**
     * Tests a null WebServiceTypeEnum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null_WebServiceTypeEnum() {
        new PortInvocationHandler(proxiedSessionPortFactory, null, service, Soap.class, TestUtils.generateUniqueStr());
    }

    /**
     * Tests a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null_Service() {
        new PortInvocationHandler(proxiedSessionPortFactory, WebServiceTypeEnum.ENTERPRISE_TYPE, null, Soap.class, TestUtils.generateUniqueStr());
    }

    /**
     * Tests a null port type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null_PortType() {
        new PortInvocationHandler(proxiedSessionPortFactory, WebServiceTypeEnum.ENTERPRISE_TYPE, service, null, TestUtils.generateUniqueStr());
    }

    /**
     * Tests a null ProxiedSessionPortFactory.
     */
    @Test
    public void test_constructor() {
        new PortInvocationHandler(proxiedSessionPortFactory, WebServiceTypeEnum.ENTERPRISE_TYPE, service, Soap.class, TestUtils.generateUniqueStr());
    }

    /**
     * Tests the createCaller method with a null login context.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createCaller_null() {
        portInvocationHandler.createCaller(null);
    }

    /**
     * Tests the createCaller method.
     */
    @Test
    public void test_createCaller() {
        final Object obj = portInvocationHandler.createCaller(loginContext);

        Assert.assertNotNull("Should have a created object", obj);
        Assert.assertTrue("Should be a Soap class", (obj instanceof Soap));
    }

    /**
     * Tests invoking when we get a SERVER_UNAVAILABLE.
     */
    @Test(expected = InvocationTargetException.class)
    public void test_invoke_exception_ServerUnavailable() throws Throwable {
        Mockito.when(port.describeTabs()).thenThrow(new UnexpectedErrorFault_Exception(TestUtils.generateUniqueStr("Foo"), new UnexpectedErrorFault()));

        portInvocationHandler.invoke(port, Soap.class.getDeclaredMethod("describeTabs", new Class[0]), new Object[0]);
    }

    /**
     * Tests invoking and doing retries.
     */
    @Test
    public void test_invoke_exception_retries() throws Throwable {
        try {
            Mockito.when(port.describeTabs()).thenThrow(new UnexpectedErrorFault_Exception(TestUtils.generateUniqueStr(ExceptionUtils.INVALID_SESSION_ID), new UnexpectedErrorFault()));

            portInvocationHandler.invoke(port, Soap.class.getDeclaredMethod("describeTabs", new Class[0]), new Object[0]);
        } catch (final InvocationTargetException ite) {
            Mockito.verify(port, Mockito.times(ProxyUtils.MAX_RETRIES)).describeTabs();
        }
    }

    /**
     * Tests invoking...
     */
    @Test
    public void test_invoke() throws Throwable {
        // Should just invoke with no exceptions...
        portInvocationHandler.invoke(port, Soap.class.getDeclaredMethod("describeTabs", new Class[0]), new Object[0]);
    }
}
