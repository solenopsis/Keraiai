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
package bak;

import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.logging.Level;
import javax.xml.ws.Service;
import org.flossware.jcore.AbstractCommonBase;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.soap.ServiceUtils;
import org.solenopsis.keraiai.SecurityMgr;
import org.solenopsis.keraiai.soap.port.PortInvocationHandler;
import org.solenopsis.keraiai.soap.port.WebServiceTypeEnum;

/**
 * Utility class to create web service ports.
 *
 * @author Scot P. Floess
 */
public final class DefaultPortFactory extends AbstractCommonBase implements PortFactory {

    /**
     * The security manager to use.
     */
    private final SecurityMgr securityMgr;

    /**
     * Default constructor not allowed.
     *
     * @param securityMgr the security manager to use for logins/auto logins.
     *
     * @throws IllegalArgumentException if securityMgr is null.
     */
    public DefaultPortFactory(final SecurityMgr securityMgr) {
        this.securityMgr = ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SecurityMgr getSecurityMgr() {
        return securityMgr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createSessionPort(final WebServiceTypeEnum webServiceType, final Service service) {
        final P retVal = (P) Proxy.newProxyInstance(DefaultPortFactory.class.getClassLoader(),
                                                    new Class[]{ ServiceUtils.getPortType(service.getClass()) },
                                                    new PortInvocationHandler(new SessionContext(webServiceType, service, getSecurityMgr()))
        );

        log(Level.FINEST, "Created for a proxy [{0}]", retVal);

        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createSessionPort(final WebServiceTypeEnum webServiceType, final Class<S> serviceClass, final URL wsdlResource) {
        final P retVal = createSessionPort(webServiceType, ServiceUtils.createService(serviceClass, wsdlResource));

        log(Level.FINEST, "Created session port [{0}]  using WebServiceTypeEnum {0}, Class [{1}] and URL [{2}]", retVal, webServiceType, serviceClass, wsdlResource);

        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createSessionPort(final WebServiceTypeEnum webServiceType, final Class<S> serviceClass, final String wsdlResource) {
        final P retVal = createSessionPort(webServiceType, ServiceUtils.createService(serviceClass, wsdlResource));

        log(Level.FINEST, "Created session port [{0}]  using WebServiceTypeEnum {0}, Class [{1}] and URL [{2}]", retVal, webServiceType, serviceClass, wsdlResource);

        return retVal;
    }

}
