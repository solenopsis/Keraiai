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
package org.solenopsis.keraiai.soap.session;

import org.solenopsis.keraiai.LoginContext;

/**
 * Metadata session server factory - when computing the server name, it uses the metadata server url found in the login context.
 *
 * @author Scot P. Floess
 */
final class MetadataSessionServerHostNameFactory implements SessionServerHostNameFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public String computeServer(final LoginContext loginContext) {
        return loginContext.getMetadataServerUrl();
    }
}
