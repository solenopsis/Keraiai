/*
 * Copyright (C) 2015 Scot P. Floess
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

import java.net.URL;
import java.util.logging.Level;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.soap.ServiceUtils;
import org.solenopsis.keraiai.soap.WebServiceTypeEnum;
import org.solenopsis.keraiai.soap.port.AbstractPortFactory;
import org.solenopsis.keraiai.soap.security.SecurityMgr;

/**
 * Abstract base class for session port factories.
 *
 * @author Scot P. Floess
 */
public abstract class AbstractSessionPortFactory extends AbstractPortFactory implements SessionPortFactory {

    /**
     * Holds our login context, as well as allow for session resets and logins.
     */
    private final SecurityMgr securityMgr;

    /**
     * Default constructor.
     *
     * @param securityMgr manages session to web services.
     *
     * @throws IllegalArgumentException if securityMgr is null.
     */
    protected AbstractSessionPortFactory(final SecurityMgr securityMgr) {
        this.securityMgr = ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");
    }

    /**
     * Create a port that is named appropriately in its URL and has the session id in the SOAP header.
     *
     * @param <P> the type of web service port.
     *
     * @param webServiceType the type of web service.
     * @param service the web service that can be used to create a port.
     * @param portType the class of the port to create.
     * @param name the name that is used as part of the URL for the web service.
     *
     * @return a port that is named appropriately in its URL and has the session id in the SOAP header.
     */
    protected abstract <P> P createSessionPort(WebServiceTypeEnum webServiceType, Service service, Class<P> portType, String name);

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
        return logAndReturn(
                Level.FINEST, "Created session port [{0}] using WebServiceTypeEnum [{1}] and Service [{2}]",
                createSessionPort(webServiceType, service, ServiceUtils.getPortType(service.getClass()), PortNameUtils.computePortName(webServiceType, service, securityMgr)),
                webServiceType, service
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createSessionPort(final WebServiceTypeEnum webServiceType, final Class<S> serviceClass, final URL wsdlResource) {
        return (P) logAndReturn(Level.FINEST, "Created session port [{0}]  using WebServiceTypeEnum {0}, Class [{1}] and URL [{2}]",
                createSessionPort(webServiceType, ServiceUtils.createService(serviceClass, wsdlResource)),
                webServiceType, serviceClass, wsdlResource
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Service, P> P createSessionPort(final WebServiceTypeEnum webServiceType, final Class<S> serviceClass, final String wsdlResource) {
        return (P) logAndReturn(
                Level.FINEST, "Created session port [{0}] using WebServiceTypeEnum {0}, Class [{1}] and String URL [{2}]",
                createSessionPort(webServiceType, ServiceUtils.createService(serviceClass, wsdlResource)),
                webServiceType, serviceClass, wsdlResource
        );
    }
}
