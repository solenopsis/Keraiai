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

import javax.xml.ws.Service;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.soap.ServiceUtils;
import org.solenopsis.keraiai.SecurityMgr;
import org.solenopsis.keraiai.soap.port.WebServiceTypeEnum;

/**
 * Acts as a proxy to call methods on ports. This is the real place that auto logins, retries, etc happen.
 *
 * @author Scot P. Floess
 */
final class SessionContext {

    /**
     * The type of web service.
     */
    private final WebServiceTypeEnum webServiceType;

    /**
     * The web service (can instantiate ports).
     */
    private final Service service;

    /**
     * The type of port to call on the web service.
     */
    private final Class portType;

    /**
     * The name for the web service. For "stock" SFDC web services (apex, enterprise, metatadata, partner and tooling) its the API
     * version). For custom web services is the name of the web service itself. Will be used as part of the URL when binding.
     */
    private final String name;

    /**
     * The security manager being used.
     */
    private final SecurityMgr securityMgr;

    /**
     * Return the web service type.
     *
     * @return the web service type.
     */
    WebServiceTypeEnum getWebServiceType() {
        return webServiceType;
    }

    /**
     * Return the web service that can create ports to call.
     *
     * @return the web service that can create ports to call.
     */
    Service getService() {
        return service;
    }

    /**
     * Return the type of port that will be called.
     *
     * @return the type of port that will be called.
     */
    Class getPortType() {
        return portType;
    }

    /**
     * Return the name for the web service. For "stock" SFDC web services (apex, enterprise, metatadata, partner and tooling) its
     * the API version). For custom web services is the name of the web service itself. Will be used as part of the URL when
     * binding.
     *
     * @return the name for the web service.
     */
    String getName() {
        return name;
    }

    /**
     * Return the security manager.
     *
     * @return the security manager.
     */
    SecurityMgr getSecurityMgr() {
        return securityMgr;
    }

    /**
     * Return an instantiated port that can be called against the web service.
     *
     * @param <P> The type of port desired.
     *
     * @return the port.
     */
    <P> P createPort() {
        return (P) getService().getPort(getPortType());
    }

    /**
     * This constructor all one needs to provide proxy calls for autologins and retries.
     *
     * @param webServiceType the type of web service being used.
     * @param service        the web service that can create ports.
     * @param portType       the type of port to create for web service calls.
     * @param name           the name of the web service used in the URL.
     * @param securityMgr    the security manager being used for logins/autologins.
     *
     * @throws IllegalArgumentException if any of the params are null or if name is blank.
     */
    <P> SessionContext(final WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name, final SecurityMgr securityMgr) {
        this.webServiceType = ObjectUtils.ensureObject(webServiceType, "WebServiceEnum cannot be null!");
        this.service = ObjectUtils.ensureObject(service, "Service cannot be null!");
        this.portType = ObjectUtils.ensureObject(portType, "Class for port type cannot be null!");
        this.securityMgr = ObjectUtils.ensureObject(securityMgr, "SecurityMgr cannot be null!");

        this.name = name;
    }

    /**
     * This constructor all one needs to provide proxy calls for autologins and retries.
     *
     * @param webServiceType the type of web service being used.
     * @param service        the web service that can create ports.
     * @param securityMgr    the security manager being used for logins/autologins.
     *
     * @throws IllegalArgumentException if any of the params are null or if name is blank.
     */
    <P> SessionContext(final WebServiceTypeEnum webServiceType, final Service service, final SecurityMgr securityMgr) {
        this(webServiceType, service, ServiceUtils.getPortType(service.getClass()), ServiceUtils.getPortName(service.getClass()), securityMgr);
    }
}
