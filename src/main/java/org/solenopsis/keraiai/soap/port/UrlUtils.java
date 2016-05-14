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
package org.solenopsis.keraiai.soap.port;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;

/**
 * Utility class for SOAP.
 *
 * @author sfloess
 */
class UrlUtils {

    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(UrlUtils.class.getName());

    /**
     * Return the logger.
     *
     * @return the logger.
     */
    private static Logger getLogger() {
        return logger;
    }

    /**
     * Using protocol, host and name, return a URL string.
     *
     * @param protocol the web protocol like https.
     * @param host the host being called.
     * @param name the name of the service being called.
     *
     * @return a URL string.
     */
    static String computeUrlString(final String protocol, final String host, final String partialUrl, final String name) {
        return new StringBuilder().append(protocol).append("://").append(host).append('/').append(partialUrl).append('/').append(name).toString();
    }

    /**
     * Using the serverUrl, get the protcol and host plus the name to create a String version of the URL. The serverUrl may have
     * extraneous data. For example: http://na7.salesforce.com/alpha/beta. We want to compute http://na7.salesforce.com/ plus the
     * name.
     *
     * @param serverUrl the actual URL.
     * @param name the name of the web service.
     *
     * @return
     */
    static String computeUrlString(final URL serverUrl, final String partialUrl, final String name) {
        return UrlUtils.computeUrlString(serverUrl.getProtocol(), serverUrl.getHost(), partialUrl, name);
    }

    /**
     * Will compute a URL string from serverUrl and name. The serverUrl may have extraneous data. For example:
     * http://na7.salesforce.com/alpha/beta. We want to compute http://na7.salesforce.com/ plus the name.
     *
     * @param serverUrl a full SFDC web service URL.
     * @param name the web service name.
     *
     * @return a URL string for the web service.
     */
    static String computeUrlString(final String serverUrl, final String partialUrl, final String name) {
        try {
            return computeUrlString(new URL(serverUrl), partialUrl, name);
        } catch (final MalformedURLException ex) {
            getLogger().log(Level.SEVERE, "Bad URL [" + serverUrl + "]", ex);

            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Set the URL to call on the web service.
     *
     * @param port is the port for whom we want to set the web service url.
     *
     * @throws IllegalArgumentException if svcUrl is null or blank.
     */
    static <P> P setUrl(final P port, final String serverUrl, final String partialUrl, final String name) {
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, computeUrlString(serverUrl, partialUrl, name));

        return port;
    }

    /**
     * Default constructor not allowed.
     */
    private UrlUtils() {
    }
}
