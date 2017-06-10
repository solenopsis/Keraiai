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
package zzzz.org.solenopsis.keraiai.soap.exception;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.flossware.jcore.utils.ExceptionUtils;
import org.flossware.jcore.utils.LoggerUtils;

/**
 * Exception utility class.
 *
 * @author Scot P. Floess
 */
public final class SalesforceExceptionUtils {
    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(SalesforceExceptionUtils.class.getName());

    /**
     * Denotes an invalid session id.
     */
    public static final String INVALID_SESSION_ID = "INVALID_SESSION_ID";

    /**
     * Denotes server is unavailable.
     */
    public static final String SERVER_UNAVAILABLE = "SERVER_UNAVAILABLE";

    /**
     * Denotes unable to lock a row.
     */
    public static final String UNABLE_TO_LOCK_ROW = "UNABLE_TO_LOCK_ROW";

    /**
     * Denotes server is unavailable.
     */
    public static final String SERVICE_UNAVAILABLE = "Service Unavailable";

    /**
     * Return the logger.
     */
    private static Logger getLogger() {
        return logger;
    }

    /**
     * Return true if we have an invalid session id or false if not.
     *
     * @param failure is the failure to examine for an invalid session id.
     */
    public static boolean isInvalidSessionIdException(final Throwable failure) {
        final boolean retVal = ExceptionUtils.isExceptionMsgContained(failure, INVALID_SESSION_ID);

        LoggerUtils.log(getLogger(), Level.FINEST, "Result of invalid sesssion id [{0}] for throwable {1}", retVal, failure);

        return retVal;
    }

    /**
     * Return true if we have server unavailable or false if not.
     *
     * @param failure is the failure to examine for server unavailable.
     */
    public static boolean isServerUnavailableException(final Throwable failure) {
        final boolean retVal = ExceptionUtils.isExceptionMsgContained(failure, SERVER_UNAVAILABLE);

        LoggerUtils.log(getLogger(), Level.FINEST, "Result of is server unavailable [{0}] for throwable {1}", retVal, failure);

        return retVal;
    }

    /**
     * Return true if we have unable to lock row or false if not.
     *
     * @param failure is the failure to examine for unable to lock row.
     */
    public static boolean isUnableToLockRowException(final Throwable failure) {
        final boolean retVal = ExceptionUtils.isExceptionMsgContained(failure, UNABLE_TO_LOCK_ROW);

        LoggerUtils.log(getLogger(), Level.FINEST, "Result of unable to lock row [{0}] for throwable {1}", retVal, failure);

        return retVal;
    }

    /**
     * Return true if we have service unavailable or false if not.
     *
     * @param failure is the failure to examine for service unavailable.
     */
    public static boolean isServiceUnavailableException(final Throwable failure) {
        final boolean retVal = ExceptionUtils.isExceptionMsgContained(failure, SERVICE_UNAVAILABLE);

        LoggerUtils.log(getLogger(), Level.FINEST, "Result of is service unavailable [{0}] for throwable {1}", retVal, failure);

        return retVal;
    }

    /**
     * Returns true if the failure represents one where relogin should occur.
     *
     * @param failure the exception to examine if relogin is necessary.
     *
     * @return true if relogin is necessary.
     */
    static boolean isReloginException(final Throwable failure) {
        return isInvalidSessionIdException(failure) || ExceptionUtils.containsIOException(failure);
    }

    /**
     * Returns true if the failure represents one where a retry should occur.
     *
     * @param failure the exception to examine if retry is necessary.
     *
     * @return true if retry is necessary.
     */
    static boolean isRetryException(final Throwable failure) {
        return isServerUnavailableException(failure) || isUnableToLockRowException(failure) || isServiceUnavailableException(failure);
    }

    /**
     * Not allowed.
     */
    private SalesforceExceptionUtils() {
    }
}
