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
 * Tests the UnproxiedSessionPortFactory class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class UnproxiedSessionPortFactoryTest {

    @Mock
    SecurityMgr securityMgr;

    @Mock
    LoginContext loginContext;

    String url;

    String sessionId;

    UnproxiedSessionPortFactory portFactory;

    @Before
    public void setup() {
        portFactory = new UnproxiedSessionPortFactory(securityMgr);

        Mockito.when(securityMgr.getSession()).thenReturn(loginContext);

        url = TestUtils.generateUniqueStr("http://foo.com");
        Mockito.when(loginContext.getServerUrl()).thenReturn(url);

        sessionId = TestUtils.generateUniqueStr("sessionId");
        Mockito.when(loginContext.getSessionId()).thenReturn(sessionId);
    }

    /**
     * Test the constructor with a null SecurityMgr.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_null() {
        new UnproxiedSessionPortFactory(null);
    }

    /**
     * Test creating a session port.
     */
    @Test
    public void test_createSessionPort() {
        final String name = TestUtils.generateUniqueStr("name");
        final org.solenopsis.keraiai.wsdl.enterprise.SforceService service = new org.solenopsis.keraiai.wsdl.enterprise.SforceService(getClass().getClassLoader().getResource("wsdl/Keraiai-enterprise.wsdl"));
        Assert.assertNotNull("Should create a port", portFactory.createSessionPort(WebServiceTypeEnum.ENTERPRISE_TYPE, service, Soap.class, name));
    }
}
