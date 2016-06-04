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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.flossware.jcore.utils.LoggerUtils;

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
        return LoggerUtils.logAndReturn(
                getLogger(), Level.FINEST, "Exception messsage contained result [{0}] for [{1}] message [{2}]",
                null == toCompare || null == message ? false : message.contains(toCompare), toCompare, message);
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    static boolean isExceptionMsgContained(final String toCompare, final Throwable failure) {
        LoggerUtils.log(getLogger(), Level.FINEST, "Seeing [{0}] is in {1}", toCompare, failure);

        if (null == failure) {
            LoggerUtils.log(getLogger(), Level.FINEST, "Failure is null - not containerd in {0}", failure);

            return false;
        }

        if (failure instanceof InvocationTargetException) {
            LoggerUtils.log(getLogger(), Level.FINEST, "Failure is an instance of InvocationTargetException [{0}] - determining target exception containment [{1}]", failure, ((InvocationTargetException) failure).getTargetException());

            return isExceptionMsgContained(toCompare, ((InvocationTargetException) failure).getTargetException());
        }

        LoggerUtils.log(getLogger(), Level.FINEST, "Checking the string [{0}] to the failure's message [{1}]", toCompare, failure.getMessage());

        return isExceptionMsgContained(toCompare, failure.getMessage());
    }

    /**
     * Return true if message contains invalid session id.
     *
     * @param message is the message to examine for being an invalid session id.
     */
    static boolean isInvalidSessionId(final String message) {
        return LoggerUtils.logAndReturn(getLogger(), Level.FINEST, "Result of invalid sesssion id [{0}] for string [{1}]", isExceptionMsgContained(INVALID_SESSION_ID, message), message);
    }

    /**
     * Return true if message contains server unavailable.
     *
     * @param message is the message to examine for being server unavailable.
     */
    static boolean isServerUnavailable(final String message) {
        return LoggerUtils.logAndReturn(getLogger(), Level.FINEST, "Result of is server unavailable [{0}] for string [{1}]", isExceptionMsgContained(SERVER_UNAVAILABLE, message), message);
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    public static boolean isInvalidSessionId(final Throwable failure) {
        return LoggerUtils.logAndReturn(getLogger(), Level.FINEST, "Result of invalid sesssion id [{0}] for throwable {1}", isExceptionMsgContained(INVALID_SESSION_ID, failure), failure);
    }

    /**
     * Return true if we have server unavailable or false if not.
     *
     * @param failure is the failure to examine for server unavailable.
     */
    public static boolean isServerUnavailable(final Throwable failure) {
        return LoggerUtils.logAndReturn(getLogger(), Level.FINEST, "Result of is server unavailable [{0}] for throwable {1}", isExceptionMsgContained(SERVER_UNAVAILABLE, failure), failure);
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
            getLogger().log(Level.FINEST, "Null throwable - does not contain IOException");

            return false;
        }

        if (throwable instanceof IOException) {
            getLogger().log(Level.FINEST, "Throwable {0} is an instance of IOException", throwable);

            return true;
        }

        LoggerUtils.log(getLogger(), Level.FINEST, "Examining the cause {0} of throwable {1} is an instance of IOException", throwable.getCause(), throwable);

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
        return LoggerUtils.logAndReturn(getLogger(), Level.FINEST, "Result of is relogin exception [{0}] for throwable {1}", isInvalidSessionId(failure) || containsIOException(failure), failure);
    }

    /**
     * Not allowed.
     */
    private ExceptionUtils() {
    }
}
