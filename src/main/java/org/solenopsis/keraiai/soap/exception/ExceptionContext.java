/*
 * Copyright (C) 2017 Scot P. Floess
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

import java.util.Map;
import java.util.TreeMap;

/**
 * Context for managing the type of exceptions caught when calling SFDC web services via a proxy port.
 *
 * @author Scot P. Floess
 */
public class ExceptionContext {

    /**
     * Cache of totals.
     */
    private final Map<SalesforceExceptionEnum, Integer> totalMap;

    /**
     * Return the cache of totals.
     *
     * @return the cache of totals.
     */
    private Map<SalesforceExceptionEnum, Integer> getTotalMap() {
        return totalMap;
    }

    /**
     * Default constructor.
     */
    public ExceptionContext() {
        totalMap = new TreeMap<>();

        // Instead of complicating code later to ensure key is set,
        // Let's just set the total to 0.
        for (final SalesforceExceptionEnum exceptionEnum : SalesforceExceptionEnum.values()) {
            totalMap.put(exceptionEnum, 0);
        }
    }

    /**
     * Increment our totals.
     *
     * @param salesforceSoapFault the failure that arose.
     *
     * @return the Salesforce exception incremented.
     */
    public SalesforceExceptionEnum incrementFailureCount(final SalesforceExceptionEnum salesforceSoapFault) {
        getTotalMap().put(salesforceSoapFault, getTotalMap().get(salesforceSoapFault) + 1);

        return salesforceSoapFault;
    }

    /**
     * Increment our totals.
     *
     * @param failure the failure that arose.
     *
     * @return the Salesforce exception incremented.
     */
    public SalesforceExceptionEnum incrementFailureCount(final Throwable failure) {
        return incrementFailureCount(SalesforceExceptionEnum.computeType(failure));
    }

    /**
     * Compute our totals. If <code>isZeroIncluded</code> will include all totals. Otherwise totals of zero are excluded.
     *
     * @param isZeroIncluded flag, if true denotes all zero totals are included.
     *
     * @return a String of the totals.
     */
    public String computeTotals(boolean isZeroIncluded) {
        final StringBuilder sb = new StringBuilder();

        for (final SalesforceExceptionEnum exceptionEnum : SalesforceExceptionEnum.values()) {
            final int total = getTotalMap().get(exceptionEnum);

            if (0 == total && !isZeroIncluded) {
                continue;
            }

            sb.append(exceptionEnum.getHumanReadbleString()).append(" [").append(total).append("] ");
        }

        return sb.toString().trim();
    }

    /**
     * Returns our totals.
     *
     * @return our totals.
     */
    public String computeTotals() {
        return computeTotals(true);
    }
}
