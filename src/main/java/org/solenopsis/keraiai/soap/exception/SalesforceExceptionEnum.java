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
package org.solenopsis.keraiai.soap.exception;

import java.io.IOException;
import java.util.logging.Logger;
import org.flossware.jcore.exception.ContainsExceptionProcessor;
import org.flossware.jcore.exception.ContainsMessageExceptionProcessor;
import org.flossware.jcore.exception.ExceptionProcessor;
import org.flossware.jcore.exception.UnknownExceptionProcessor;

/**
 * Enum representing Salesforce exceptions.
 *
 * @author Scot P. Floess
 */
public enum SalesforceExceptionEnum {
    INVALID_SESSION_ID(new ContainsMessageExceptionProcessor("INVALID_SESSION_ID"), "invalid session id"),
    SERVER_UNAVAILABLE(new ContainsMessageExceptionProcessor("SERVER_UNAVAILABLE"), "server unavailable"),
    UNABLE_TO_LOCK_ROW(new ContainsMessageExceptionProcessor("UNABLE_TO_LOCK_ROW"), "unable to lock row"),
    SERVICE_UNAVAILABLE(new ContainsMessageExceptionProcessor("Service Unavailable"), "service unavailable"),
    IOEXCEPTION(new ContainsExceptionProcessor(IOException.class), "IOException"),
    UNKNOWN(new UnknownExceptionProcessor(), "Unknown");

    /**
     * Our logger.
     */
    private static final Logger logger = Logger.getLogger(SalesforceExceptionEnum.class.getName());

    /**
     * The exception processor.
     */
    private final ExceptionProcessor exceptionProcessor;

    /**
     * The human readable string of the failure.
     */
    private final String humanReadableString;

    /**
     * This constructor sets the exception processor and the human readable version of the failure.
     *
     * @param exceptionProcessor can process a failure to determine its applicability.
     * @param humanReadableString the human readable version of the failure.
     */
    private SalesforceExceptionEnum(final ExceptionProcessor exceptionProcessor, final String humanReadableString) {
        this.exceptionProcessor = exceptionProcessor;
        this.humanReadableString = humanReadableString;
    }

    /**
     * Return the logger.
     */
    static Logger getLogger() {
        return logger;
    }

    /**
     * Return the exception processor.
     */
    ExceptionProcessor getExceptionProcessor() {
        return exceptionProcessor;
    }

    /**
     * Compute the type of Salesforce SOAP fault based upon <code>failure</code>.
     *
     * @param throwable the type of exception in Salesforce for whom we desire the type of SOAP fault representation.
     *
     * @return the type of Salesforce SOAP fault based upon <code>throwable</code>.
     */
    public static SalesforceExceptionEnum computeType(final Throwable throwable) {
        for (final SalesforceExceptionEnum soapFaultEnum : SalesforceExceptionEnum.values()) {
            if (soapFaultEnum.getExceptionProcessor().isExceptionApplicable(throwable)) {
                return soapFaultEnum;
            }
        }

        return UNKNOWN;
    }

    /**
     * Returns true if the failure represents one where relogin should occur.
     *
     * @param salesforceException the exception to examine if relogin is necessary.
     *
     * @return true if relogin is necessary.
     */
    public static boolean isReloginException(final SalesforceExceptionEnum salesforceException) {
        return INVALID_SESSION_ID == salesforceException || IOEXCEPTION == salesforceException;
    }

    /**
     * Returns true if the failure represents one where relogin should occur.
     *
     * @param failure the exception to examine if relogin is necessary.
     *
     * @return true if relogin is necessary.
     */
    public static boolean isReloginException(final Throwable failure) {
        return isReloginException(computeType(failure));
    }

    /**
     * Returns true if the failure represents one where a retry should occur.
     *
     * @param salesforceException the exception to examine if retry is necessary.
     *
     * @return true if retry is necessary.
     */
    public static boolean isRetryException(final SalesforceExceptionEnum salesforceException) {
        return SERVER_UNAVAILABLE == salesforceException || UNABLE_TO_LOCK_ROW == salesforceException || SERVICE_UNAVAILABLE == salesforceException;
    }

    /**
     * Returns true if the failure represents one where a retry should occur.
     *
     * @param failure the exception to examine if retry is necessary.
     *
     * @return true if retry is necessary.
     */
    public static boolean isRetryException(final Throwable failure) {
        return isRetryException(computeType(failure));
    }

    /**
     * Return the human readable version of this enum.
     *
     * @return the human readable version of this enum.
     */
    public String getHumanReadbleString() {
        return humanReadableString;
    }
}
