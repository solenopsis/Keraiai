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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.LoggerUtils;
import org.flossware.jcore.utils.soap.ServiceUtils;
import org.solenopsis.keraiai.soap.WebServiceTypeEnum;
import org.solenopsis.keraiai.soap.security.SecurityMgr;

/**
 * Utility class for port names. A port name is the API version for all non custom web services or the WebEndpoint annotation for
 * the service class.
 *
 * @author Scot P. Floess
 */
class PortNameUtils {

    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(PortNameUtils.class.getName());

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    private static Logger getLogger() {
        return logger;
    }

    /**
     * Computes a port name. If the web service type is custom, it's the port name on the service class. Otherwise its the AI
     * version found in the security mgr.
     *
     * @param webServiceType the type of web service.
     * @param service the web service itself.
     * @param securityMgr the security manager to use when calling to SFDC.
     *
     * @return either the service class' port name or the API version (if not a custom web service) or the API version found in the
     * security mgr.
     */
    static String computePortName(final WebServiceTypeEnum webServiceType, final Service service, final SecurityMgr securityMgr) {
        return LoggerUtils.logAndReturn(getLogger(), Level.FINEST, "Computed port name [{0}]]", (webServiceType == WebServiceTypeEnum.CUSTOM_TYPE ? ServiceUtils.getPortName(service.getClass()) : securityMgr.getCredentials().getApiVersion()));
    }
}