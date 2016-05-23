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
package org.solenopsis.keraiai.soap.security.tooling;

import java.util.logging.Level;
import org.solenopsis.keraiai.soap.credentials.Credentials;
import org.solenopsis.keraiai.soap.security.AbstractSecurityMgr;
import org.solenopsis.keraiai.soap.security.LoginContext;
import org.solenopsis.keraiai.soap.security.LoginWebServiceTypeEnum;
import org.solenopsis.keraiai.wsdl.tooling.SforceServicePortType;

/**
 * Implementation using the tooling web service.
 *
 * @author Scot P. Floess
 */
public class ToolingSecurityMgr extends AbstractSecurityMgr<SforceServicePortType> {

    /**
     * This constructor will set session based credentials.
     *
     * @param credentials our credentials.
     */
    public ToolingSecurityMgr(final Credentials credentials) {
        super(credentials);
    }

    /**
     * Default constructor.
     */
    public ToolingSecurityMgr() {
        this(null);
    }

    @Override
    protected LoginWebServiceTypeEnum getLoginWebServiceType() {
        return LoginWebServiceTypeEnum.TOOLING_LOGIN_SERVICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LoginContext doLogin(SforceServicePortType port) throws Exception {
        log(Level.FINEST, "Performing login on [{0}]", port);

        return new ToolingLoginContext(port.login(getCredentials().getUserName(), getCredentials().getSecurityPassword()), getCredentials());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLogout(SforceServicePortType port) throws Exception {
        log(Level.FINEST, "Performing logout on [{0}]", port);

        port.logout();
    }
}
