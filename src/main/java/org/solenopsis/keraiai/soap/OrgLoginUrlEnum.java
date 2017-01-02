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
package org.solenopsis.keraiai.soap;

/**
 * Denotes the login URL for sandboxes and production orgs.
 *
 * @author Scot P. Floess
 */
public enum OrgLoginUrlEnum {
    SANDBOX("https://test.salesforce.com"),
    PRODUCTION("https://login.salesforce.com");

    /**
     * The URL for login.
     */
    private final String loginUrl;

    /**
     * This constructor sets login URL.
     *
     * @param loginUrl the login URL.
     */
    private OrgLoginUrlEnum(final String loginUrl) {
        this.loginUrl = loginUrl;
    }

    /**
     * Return the login URL.
     *
     * @return the login URL.
     */
    public String getLoginUrl() {
        return loginUrl;
    }
}
