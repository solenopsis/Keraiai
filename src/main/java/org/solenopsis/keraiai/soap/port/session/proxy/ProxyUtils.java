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

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.solenopsis.keraiai.soap.utils.ExceptionUtils;

/**
 * Utility class for proxies.
 *
 * @author Scot P. Floess
 */
class ProxyUtils {

    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(ProxyUtils.class.getName());

    /**
     * Return the logger.
     */
    private static Logger getLogger() {
        return logger;
    }

    /**
     * Maximum retries.
     */
    static final int MAX_RETRIES = 4;

    /**
     * Can we retry a call?
     *
     * @param totalCalls the total calls made thus far.
     *
     * @return true if a call can be retried or false if we've reached our limit.
     */
    static boolean isCallRetriable(int totalCalls) {
        return totalCalls < MAX_RETRIES;
    }

    /**
     * When an exception happens on call, this method will handle the exception.
     *
     * @param callFailure the exception that arose when calling SFDC.
     * @param method the method being called when the failure arose.
     *
     * @throws Throwable if the exception cannot be handled.
     */
    static void processException(final Throwable callFailure, final Method method) throws Throwable {
        if (!ExceptionUtils.isReloginException(callFailure)) {
            getLogger().log(Level.SEVERE, "Trouble calling [{0}] - [{1}]...raising exception", new Object[]{method.getName(), callFailure.getLocalizedMessage()});

            throw callFailure;
        }

        getLogger().log(Level.WARNING, "Received a relogin exception when calling [{0}] - initiating a new login", method.getName());
    }
}
