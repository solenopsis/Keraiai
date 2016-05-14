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
package org.solenopsis.keraiai.soap.port.session;

import org.solenopsis.keraiai.soap.port.session.AbstractSessionPortFactory;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.solenopsis.keraiai.soap.WebServiceTypeEnum;
import org.solenopsis.keraiai.soap.credentials.Credentials;
import org.solenopsis.keraiai.soap.security.SecurityMgr;

/**
 * Tests the AbstractSessionPort class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractSessionPortFactoryTest {
    
    class AbstractSessionPortFactoryStub extends AbstractSessionPortFactory {
        
        WebServiceTypeEnum webServiceType;
        Service service;
        Class portType;
        String name;
        
        Object port;
        
        AbstractSessionPortFactoryStub(final SecurityMgr securityMgr) {
            super(securityMgr);
        }
        
        @Override
        protected <P> P createSessionPort(final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name) {
            this.webServiceType = webServiceType;
            this.service = service;
            this.portType = portType;
            this.name = name;
            
            return (P) port;
        }
    }
    
    AbstractSessionPortFactoryStub sessionPortFactory;
    
    @Mock
    SecurityMgr securityMgr;
    
    @Mock
    Credentials credentials;
    
    String apiVersion;
    
    @Before
    public void setup() {
        sessionPortFactory = new AbstractSessionPortFactoryStub(securityMgr);
        
        Mockito.when(securityMgr.getCredentials()).thenReturn(credentials);
        
        apiVersion = TestUtils.generateUniqueStr("api");
        Mockito.when(credentials.getApiVersion()).thenReturn(apiVersion);
    }

    /**
     * Test the constructor where the security mgr is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null() {
        new AbstractSessionPortFactoryStub(null);
    }

    /**
     * Test the constructor.
     */
    @Test
    public void test_constructor() {
        Assert.assertSame("Should be the same security mgr", securityMgr, sessionPortFactory.getSecurityMgr());
    }

    /**
     * Test creating a session port with just a service.
     */
    @Test
    public void test_createSessionPort_Service() {
        final org.solenopsis.keraiai.wsdl.enterprise.SforceService service = new org.solenopsis.keraiai.wsdl.enterprise.SforceService(getClass().getClassLoader().getResource("wsdl/Keraiai-enterprise.wsdl"));
        final org.solenopsis.keraiai.wsdl.enterprise.Soap port = sessionPortFactory.createSessionPort(WebServiceTypeEnum.ENTERPRISE_TYPE, service);
        
        Assert.assertEquals("Should be the same web service type", WebServiceTypeEnum.ENTERPRISE_TYPE, sessionPortFactory.webServiceType);
        
        Assert.assertEquals("Should be the same service", service, sessionPortFactory.service);
        Assert.assertEquals("Should be the same port type", org.solenopsis.keraiai.wsdl.enterprise.Soap.class, sessionPortFactory.portType);
        Assert.assertEquals("Should be the same port name", apiVersion, sessionPortFactory.name);
    }

    /**
     * Test creating a session port with just a "custom" service.
     */
    @Test
    public void test_createSessionPort_Service_custom() {
        final org.solenopsis.keraiai.wsdl.enterprise.SforceService service = new org.solenopsis.keraiai.wsdl.enterprise.SforceService(getClass().getClassLoader().getResource("wsdl/Keraiai-enterprise.wsdl"));
        final org.solenopsis.keraiai.wsdl.enterprise.Soap port = sessionPortFactory.createSessionPort(WebServiceTypeEnum.CUSTOM_TYPE, service);
        
        Assert.assertEquals("Should be the same web service type", WebServiceTypeEnum.CUSTOM_TYPE, sessionPortFactory.webServiceType);
        
        Assert.assertEquals("Should be the same service", service, sessionPortFactory.service);
        Assert.assertEquals("Should be the same port type", org.solenopsis.keraiai.wsdl.enterprise.Soap.class, sessionPortFactory.portType);
        Assert.assertEquals("Should be the same port name", "Soap", sessionPortFactory.name);
    }

    /**
     * Test creating a session port with a URL.
     */
    @Test
    public void test_createSessionPort_URL() {
        final org.solenopsis.keraiai.wsdl.partner.Soap port = sessionPortFactory.createSessionPort(WebServiceTypeEnum.PARTNER_TYPE, org.solenopsis.keraiai.wsdl.partner.SforceService.class, getClass().getClassLoader().getResource("wsdl/Keraiai-partner.wsdl"));
        
        Assert.assertEquals("Should be the same web service type", WebServiceTypeEnum.PARTNER_TYPE, sessionPortFactory.webServiceType);
        
        Assert.assertSame("Should be the same service", org.solenopsis.keraiai.wsdl.partner.SforceService.class, sessionPortFactory.service.getClass());
        Assert.assertEquals("Should be the same port type", org.solenopsis.keraiai.wsdl.partner.Soap.class, sessionPortFactory.portType);
        Assert.assertEquals("Should be the same port name", apiVersion, sessionPortFactory.name);
    }

    /**
     * Test creating a session port with a URL for a "custom" service.
     */
    @Test
    public void test_createSessionPort_URL_custom() {
        final org.solenopsis.keraiai.wsdl.partner.Soap port = sessionPortFactory.createSessionPort(WebServiceTypeEnum.CUSTOM_TYPE, org.solenopsis.keraiai.wsdl.partner.SforceService.class, getClass().getClassLoader().getResource("wsdl/Keraiai-partner.wsdl"));
        
        Assert.assertEquals("Should be the same web service type", WebServiceTypeEnum.CUSTOM_TYPE, sessionPortFactory.webServiceType);
        
        Assert.assertSame("Should be the same service", org.solenopsis.keraiai.wsdl.partner.SforceService.class, sessionPortFactory.service.getClass());
        Assert.assertEquals("Should be the same port type", org.solenopsis.keraiai.wsdl.partner.Soap.class, sessionPortFactory.portType);
        Assert.assertEquals("Should be the same port name", "Soap", sessionPortFactory.name);
    }

    /**
     * Test creating a session port with just a service.
     */
    @Test
    public void test_createSessionPort_String() {
        final org.solenopsis.keraiai.wsdl.tooling.SforceServicePortType port = sessionPortFactory.createSessionPort(WebServiceTypeEnum.TOOLING_TYPE, org.solenopsis.keraiai.wsdl.tooling.SforceServiceService.class, "file://" + getClass().getClassLoader().getResource("wsdl/Keraiai-tooling.wsdl").getFile());
        
        Assert.assertEquals("Should be the same web service type", WebServiceTypeEnum.TOOLING_TYPE, sessionPortFactory.webServiceType);
        
        Assert.assertSame("Should be the same service", org.solenopsis.keraiai.wsdl.tooling.SforceServiceService.class, sessionPortFactory.service.getClass());
        Assert.assertEquals("Should be the same port type", org.solenopsis.keraiai.wsdl.tooling.SforceServicePortType.class, sessionPortFactory.portType);
        Assert.assertEquals("Should be the same port name", apiVersion, sessionPortFactory.name);
    }

    /**
     * Test creating a session port with just a "custom" service.
     */
    @Test
    public void test_createSessionPort_String_custom() {
        final org.solenopsis.keraiai.wsdl.tooling.SforceServicePortType port = sessionPortFactory.createSessionPort(WebServiceTypeEnum.CUSTOM_TYPE, org.solenopsis.keraiai.wsdl.tooling.SforceServiceService.class, "file://" + getClass().getClassLoader().getResource("wsdl/Keraiai-tooling.wsdl").getFile());
        
        Assert.assertEquals("Should be the same web service type", WebServiceTypeEnum.CUSTOM_TYPE, sessionPortFactory.webServiceType);
        
        Assert.assertSame("Should be the same service", org.solenopsis.keraiai.wsdl.tooling.SforceServiceService.class, sessionPortFactory.service.getClass());
        Assert.assertEquals("Should be the same port type", org.solenopsis.keraiai.wsdl.tooling.SforceServicePortType.class, sessionPortFactory.portType);
        Assert.assertEquals("Should be the same port name", "SforceService", sessionPortFactory.name);
    }
}
