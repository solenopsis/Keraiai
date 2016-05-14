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
package org.solenopsis.keraiai.soap.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 *
 * @author Scot P. Floess
 */
public class ExceptionUtils {

    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(ExceptionUtils.class.getName());

    /**
     * Denotes an invalid session id.
     */
    public static final String INVALID_SESSION_ID = "INVALID_SESSION_ID";

    /**
     * Denotes server is unavailable.
     */
    public static final String SERVER_UNAVAILABLE = "SERVER_UNAVAILABLE";

    /**
     * Return the logger.
     */
    private static Logger getLogger() {
        return logger;
    }

    /**
     * Return true if message contains invalid session id.
     *
     * @param toCompare is the
     * @param message is the message to examine for being an invalid session id.
     */
    static boolean isExceptionMsgContained(final String toCompare, final String message) {
        return ((null == toCompare || null == message) ? false : message.contains(toCompare));
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    static boolean isExceptionMsgContained(final String toCompare, final Throwable failure) {
        if (null == failure) {
            return false;
        }

        if (failure instanceof InvocationTargetException) {
            return isExceptionMsgContained(toCompare, ((InvocationTargetException) failure).getTargetException());
        }

        return isExceptionMsgContained(toCompare, failure.getMessage());
    }

    /**
     * Return true if message contains invalid session id.
     *
     * @param message is the message to examine for being an invalid session id.
     */
    static boolean isInvalidSessionId(final String message) {
        return isExceptionMsgContained(INVALID_SESSION_ID, message);
    }

    /**
     * Return true if message contains server unavailable.
     *
     * @param message is the message to examine for being server unavailable.
     */
    static boolean isServerUnavailable(final String message) {
        return isExceptionMsgContained(SERVER_UNAVAILABLE, message);
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    public static boolean isInvalidSessionId(final Throwable failure) {
        return isExceptionMsgContained(INVALID_SESSION_ID, failure);
    }

    /**
     * Return true if we have server unavailable or false if not.
     *
     * @param failure is the failure to examine for server unavailable.
     */
    public static boolean isServerUnavailable(final Throwable failure) {
        return isExceptionMsgContained(SERVER_UNAVAILABLE, failure);
    }

    /**
     * Return true if throwable or any of its root causes is an IOException.
     *
     * @param throwable to examine if being an IOException or any root causes is an IOException.
     *
     * @return if throwable or its root causes is an IOException, or false if not.
     */
    public static boolean containsIOException(final Throwable throwable) {
        if (null == throwable) {
            return false;
        }

        if (throwable instanceof IOException) {
            return true;
        }

        return containsIOException(throwable.getCause());
    }

    /**
     * Returns true if the failure represents one where relogin should occur.
     *
     * @param failure the exception to examine if relogin is necessary.
     *
     * @return true if relogin is necessary.
     */
    public static boolean isReloginException(final Throwable failure) {
        return isInvalidSessionId(failure) || containsIOException(failure);
    }

    /**
     * Not allowed.
     */
    private ExceptionUtils() {
    }
}
