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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
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
import org.solenopsis.keraiai.wsdl.enterprise.InvalidIdFault_Exception;
import org.solenopsis.keraiai.wsdl.enterprise.Soap;
import org.solenopsis.keraiai.wsdl.enterprise.UnexpectedErrorFault_Exception;

/**
 * Test the PortInvocationHandler class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class PortInvocationHandlerTest {

    interface StubInterface extends Soap, BindingProvider {

        public void testMethod(String arg);
    }

    @Mock
    Service service;

    @Mock
    QName serviceName;

    @Mock
    SecurityMgr securityMgr;

    @Mock
    LoginContext loginContext;

    @Mock
    Credentials credentials;

    @Mock
    StubInterface soap;

    @Mock
    Binding binding;

    @Before
    public void setup() {
        Mockito.when(loginContext.getSessionId()).thenReturn(TestUtils.generateUniqueStr("Session ID"));

        Mockito.when(securityMgr.getCredentials()).thenReturn(credentials);
        Mockito.when(securityMgr.getSession()).thenReturn(loginContext);

        Mockito.when(loginContext.getBaseServerUrl()).thenReturn("https://foo.com");
        Mockito.when(credentials.getApiVersion()).thenReturn(TestUtils.generateUniqueStr());

        Mockito.when(service.getPort((Class) Mockito.any())).thenReturn(soap);
        Mockito.when(service.getServiceName()).thenReturn(serviceName);
        Mockito.when(serviceName.getNamespaceURI()).thenReturn(TestUtils.generateUniqueStr());

        Mockito.when(soap.getBinding()).thenReturn(binding);
    }

    /**
     * Test a null SecurityMgr.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_SecurityMgr_null() {
        new PortInvocationHandler(null, WebServiceTypeEnum.APEX_SERVICE_TYPE, service, Object.class);
    }

    /**
     * Test a null WebServiceTypeEnum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_WebServiceTypeEnum_null() {
        new PortInvocationHandler(securityMgr, null, service, Object.class);
    }

    /**
     * Test a null Service.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_Service_null() {
        new PortInvocationHandler(securityMgr, WebServiceTypeEnum.CUSTOM_SERVICE_TYPE, null, Object.class);
    }

    /**
     * Test a null port.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_Class_null() {
        new PortInvocationHandler(securityMgr, WebServiceTypeEnum.CUSTOM_SERVICE_TYPE, service, null);
    }

    /**
     * Test good constructor.
     */
    @Test
    public void test_constructor() {
        new PortInvocationHandler(securityMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service, Object.class);
    }

    /**
     * Test invoking with a null proxy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_invoke_Object_null() throws NoSuchMethodException, NoSuchMethodException, Throwable {
        final Method method = StubInterface.class.getDeclaredMethod("testMethod", new Class[]{ String.class });

        new PortInvocationHandler(securityMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service, Object.class).invoke(null, method, new Object[]{ TestUtils.generateUniqueStr() });
    }

    /**
     * Test invoking with a null method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_invoke_Method_null() throws NoSuchMethodException, NoSuchMethodException, Throwable {
        new PortInvocationHandler(securityMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service, Object.class).invoke(new Object(), null, new Object[]{ TestUtils.generateUniqueStr() });
    }

    /**
     * Test invoking and getting an invalid session id.
     */
    @Test
    public void test_invoke_InvalidSessionId() throws NoSuchMethodException, NoSuchMethodException, Throwable {
        final PortInvocationHandler invoker = new PortInvocationHandler(securityMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service, StubInterface.class);

        final String str = TestUtils.generateUniqueStr();

        final InvalidIdFault_Exception invalidSessionIdException = new InvalidIdFault_Exception(ExceptionUtils.INVALID_SESSION_ID, null);

        Mockito.when(soap.query(Mockito.anyString())).thenThrow(invalidSessionIdException);

        try {
            invoker.invoke(soap, StubInterface.class.getMethod("query", new Class[]{ String.class }), new Object[]{ str });
        } catch (final InvocationTargetException ite) {
            ite.printStackTrace();
        }

        Mockito.verify(soap, Mockito.times(PortUtils.MAX_RETRIES)).query(str);
        Mockito.verify(securityMgr, Mockito.times(PortUtils.MAX_RETRIES)).resetSession((LoginContext) Mockito.anyObject());
    }

    /**
     * Test invoking and getting an IOException.
     */
    @Test
    public void test_invoke_IOException() throws NoSuchMethodException, NoSuchMethodException, Throwable {
        final PortInvocationHandler invoker = new PortInvocationHandler(securityMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service, StubInterface.class);

        final String str = TestUtils.generateUniqueStr();

        final UnexpectedErrorFault_Exception faultException = new UnexpectedErrorFault_Exception(TestUtils.generateUniqueStr(), null, new IOException());

        Mockito.when(soap.query(Mockito.anyString())).thenThrow(faultException);

        try {
            invoker.invoke(soap, StubInterface.class.getMethod("query", new Class[]{ String.class }), new Object[]{ str });
        } catch (final InvocationTargetException ite) {

        }

        Mockito.verify(soap, Mockito.times(PortUtils.MAX_RETRIES)).query(str);
        Mockito.verify(securityMgr, Mockito.times(PortUtils.MAX_RETRIES)).resetSession((LoginContext) Mockito.anyObject());
    }

    /**
     * Test invoking and getting an Exception.
     */
    @Test
    public void test_invoke_Exception() throws NoSuchMethodException, NoSuchMethodException, Throwable {
        final PortInvocationHandler invoker = new PortInvocationHandler(securityMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service, StubInterface.class);

        final String str = TestUtils.generateUniqueStr();

        final UnexpectedErrorFault_Exception faultException = new UnexpectedErrorFault_Exception(TestUtils.generateUniqueStr(), null, new NullPointerException());

        Mockito.when(soap.query(Mockito.anyString())).thenThrow(faultException);

        try {
            invoker.invoke(soap, StubInterface.class.getMethod("query", new Class[]{ String.class }), new Object[]{ str });
        } catch (final InvocationTargetException ite) {
            Assert.assertEquals("Should have gotten an UnexpectedErrorFault_Exception", ite.getCause().getClass(), UnexpectedErrorFault_Exception.class);
        }

        Mockito.verify(soap, Mockito.times(1)).query(str);
        Mockito.verify(securityMgr, Mockito.times(0)).resetSession((LoginContext) Mockito.anyObject());
    }

    /**
     * Test a good invoke.
     */
    @Test
    public void test_invoke() throws NoSuchMethodException, NoSuchMethodException, Throwable {
        final PortInvocationHandler invoker = new PortInvocationHandler(securityMgr, WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, service, StubInterface.class);

        final String str = TestUtils.generateUniqueStr();

        invoker.invoke(soap, StubInterface.class.getMethod("query", new Class[]{ String.class }), new Object[]{ str });

        Mockito.verify(soap, Mockito.times(1)).query(str);
        Mockito.verify(securityMgr, Mockito.never()).resetSession((LoginContext) Mockito.anyObject());
    }
}
