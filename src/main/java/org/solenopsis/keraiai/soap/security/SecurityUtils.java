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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.flossware.jcore.utils.LoggerUtils;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.StringUtils;
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
     * Using protocol, host and name, return a URL string.
     *
     * @param protocol the web protocol like https.
     * @param host     the host being called.
     *
     * @return a URL string.
     */
    static String computeUrlString(final String protocol, final String host) {
        StringUtils.ensureString(protocol, "Must provide a protocol!");
        StringUtils.ensureString(host, "Must provide a host!");

        final String retVal = StringUtils.concat(protocol, "://", host);

        LoggerUtils.log(getLogger(), Level.FINEST, "Computed URL [{]}]", retVal);

        return retVal;
    }

    /**
     * Using the serverUrl, get the protcol and host plus the name to create a String version of the URL. The serverUrl may have
     * extraneous data. For example: http://na7.salesforce.com/alpha/beta. We want to compute http://na7.salesforce.com/ plus the
     * name.
     *
     * @param serverUrl the actual URL.
     * @param name      the name of the web service.
     *
     * @return
     */
    static String computeUrlString(final URL serverUrl) {
        ObjectUtils.ensureObject(serverUrl, "Must provide a server URL!");

        return computeUrlString(serverUrl.getProtocol(), serverUrl.getHost());
    }

    /**
     * Will compute a URL string from serverUrl, partialUrl and name. The serverUrl may have extraneous data. For example:
     * http://na7.salesforce.com/alpha/beta. We want to compute http://na7.salesforce.com/ plus the name.
     *
     * @param serverUrl a full SFDC web service URL.
     *
     * @return a URL string for the web service.
     */
    static String computeUrlString(final String serverUrl) {
        StringUtils.ensureString(serverUrl, "Must provide a string URL!");

        try {
            return computeUrlString(new URL(serverUrl));
        } catch (final MalformedURLException ex) {
            getLogger().log(Level.SEVERE, "Bad URL [" + serverUrl + "]", ex);

            throw new IllegalArgumentException(ex);
        }
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

        return StringUtils.concatWithSeparator(false, "/", securityMgr.getCredentials().getUrl(), webServiceType.getWebServiceSubUrl().getPartialUrl(), securityMgr.getCredentials().getApiVersion());
    }

    /**
     * Default constructor not allowed.
     */
    private SecurityUtils() {
    }
}
