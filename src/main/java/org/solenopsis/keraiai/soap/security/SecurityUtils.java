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
package org.solenopsis.keraiai.soap.security;

import java.util.logging.Logger;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.SecurityMgr;
import org.solenopsis.keraiai.soap.port.WebServiceTypeEnum;

/**
 * Security utility class.
 *
 * @author Scot P. Floess
 */
final class SecurityUtils {

    /**
     * Our LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(SecurityUtils.class.getName());

    /**
     * Return the LOGGER.
     */
    private static Logger getLogger() {
        return LOGGER;
    }

    /**
     * Compute the login URL from credentials - the API version from the credentials is used in the URL.
     *
     * @param credentials    contains the "base" url and the API version used to construct the URL.
     * @param webServiceType is the type of web service being called. It contains the partial URL we need to compute a login URL.
     *
     * @return a login URL.
     *
     * @throws IllegalArgumentException if <code>credentials</code> or <code>webServiceType</code> are null.
     */
    static String computeLoginUrl(final Credentials credentials, final WebServiceTypeEnum webServiceType) {
        ObjectUtils.ensureObject(credentials, "Must provide credentials!");
        ObjectUtils.ensureObject(webServiceType, "Must provide a web service type!");

        return StringUtils.concatWithSeparator(false, "/", credentials.getUrl(), webServiceType.getWebServiceSubUrl().getPartialUrl(), credentials.getApiVersion());
    }

    /**
     * Compute the login URL from credentials - the API version from the credentials is used in the URL.
     *
     * @param securityMgr    contains credentials whose "base" url and the API version used to construct the URL.
     * @param webServiceType is the type of web service being called. It contains the partial URL we need to compute a login URL.
     *
     * @return a login URL.
     *
     * @throws IllegalArgumentException if <code>securityMgr</code> is null.
     */
    static String computeLoginUrl(final SecurityMgr securityMgr, final WebServiceTypeEnum webServiceType) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security mananger!");
        ObjectUtils.ensureObject(webServiceType, "Must provide a web service type!");

        return computeLoginUrl(securityMgr.getCredentials(), webServiceType);
    }

    /**
     * Default constructor not allowed.
     */
    private SecurityUtils() {
    }
}
