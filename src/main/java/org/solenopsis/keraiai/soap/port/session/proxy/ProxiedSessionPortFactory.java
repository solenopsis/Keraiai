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
package org.solenopsis.keraiai.soap.port.session.proxy;

import java.lang.reflect.Proxy;
import java.util.logging.Level;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.ObjectUtils;
import org.solenopsis.keraiai.soap.WebServiceTypeEnum;
import org.solenopsis.keraiai.soap.port.session.AbstractSessionPortFactory;
import org.solenopsis.keraiai.soap.security.LoginContext;
import org.solenopsis.keraiai.soap.security.SecurityMgr;

/**
 *
 * This class is used to make method calls on web service ports and acts as a proxy. It performs auto logins, relogins as well as
 * retrying SFDC calls when they fail.
 *
 * @author Scot P. Floess
 *
 */
public class ProxiedSessionPortFactory extends AbstractSessionPortFactory {

    /**
     * Create a port that has the session id in the SOAP header and is ready to use.
     *
     * @param <P> the port type desired.
     *
     * @param webServiceType the type of web service.
     * @param service the actual Service subclass.
     * @param portType the port type for the service.
     * @param name the name of the web service. For API based web services it will represent that number whereas for Custom web
     * services it is the name of the Apex class.
     * @param loginContext contains the session id needed to call the Salesforce web service.
     *
     * @return a usable port to call on a web service that has <code>sessionId</code> in the SOAP header.
     */
    <P> P doCreateSessionPort(WebServiceTypeEnum webServiceType, final Service service, final Class<P> portType, final String name, final LoginContext loginContext) {
        ObjectUtils.ensureObject(webServiceType, "Should have a web service type");
        ObjectUtils.ensureObject(service, "Should have a service");
        ObjectUtils.ensureObject(portType, "Should have a port type");
        ObjectUtils.ensureObject(loginContext, "Should have a login context");

        return logAndReturn(Level.FINEST, "Created for a proxy port [{0}]", createSessionPort(webServiceType, getSecurityMgr().getSession().getServerUrl(), service, portType, name, loginContext.getSessionId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <P> P createSessionPort(WebServiceTypeEnum webServiceType, Service service, Class<P> portType, String name) {
        return (P) logAndReturn(Level.FINEST, "Created for a proxy [{0}]", (Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{portType}, new PortInvocationHandler(this, webServiceType, service, portType, name))));
    }

    /**
     * This constructor sets the security manager.
     *
     * @param securityMgr the security manager.
     *
     * @throws IllegalArguentException is securityMgr is null.
     */
    public ProxiedSessionPortFactory(SecurityMgr securityMgr) {
        super(securityMgr);
    }

}
