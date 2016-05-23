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

import java.util.logging.Level;
import javax.xml.ws.Service;
import org.solenopsis.keraiai.soap.WebServiceTypeEnum;
import org.solenopsis.keraiai.soap.security.SecurityMgr;

/**
 * Abstract base class for port factories.
 *
 * @author Scot P. Floess
 */
public class UnproxiedSessionPortFactory extends AbstractSessionPortFactory {

    /**
     * Default constructor.
     *
     * @param securityMgr
     */
    public UnproxiedSessionPortFactory(final SecurityMgr securityMgr) {
        super(securityMgr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <P> P createSessionPort(WebServiceTypeEnum webServiceType, Service service, Class<P> portType, String name) {
        return logAndReturn(Level.FINEST, "Created unproxied session [{0}]", createSessionPort(webServiceType, getSecurityMgr().getSession().getServerUrl(), service, portType, name, getSecurityMgr().getSession().getSessionId()));
    }
}
