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
package org.solenopsis.keraiai.soap.login;

import org.flossware.jcore.utils.soap.SoapUtils;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.LoginContext;
import org.solenopsis.keraiai.soap.ApiWebService;
import org.solenopsis.keraiai.soap.LoginWebService;
import org.solenopsis.keraiai.soap.port.ApiWebServiceEnum;
import org.solenopsis.keraiai.soap.session.SessionPortFactory;

/**
 * Represents all login SOAP web service: enterprise, partner and tooling. Additionally provides the ability to create a usable
 * login port for logins.
 *
 * @author Scot P. Floess
 */
public enum LoginWebServiceEnum implements LoginWebService {
    ENTERPRISE_LOGIN_SERVICE(ApiWebServiceEnum.ENTERPRISE_SERVICE, LoginMgr.ENTERPRISE_LOGIN_MGR),
    PARTNER_LOGIN_SERVICE(ApiWebServiceEnum.PARTNER_SERVICE, LoginMgr.PARTNER_LOGIN_MGR),
    TOOLING_LOGIN_SERVICE(ApiWebServiceEnum.TOOLING_SERVICE, LoginMgr.TOOLING_LOGIN_MGR);

    /**
     * The actual web service type.
     */
    private final ApiWebService apiWebService;

    /**
     * The login mgr.
     */
    private final LoginMgr loginMgr;

    private LoginMgr getLoginMgr() {
        return loginMgr;
    }

    /**
     * This constructor sets the SFDC web service, port type and partial URL (as defined in the Java doc header).
     *
     * @param webService the SFDC web service.
     * @param portType   the port for the web service.
     */
    private LoginWebServiceEnum(final ApiWebService apiWebService, final LoginMgr loginMgr) {
        this.apiWebService = apiWebService;
        this.loginMgr = loginMgr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApiWebService getApiWebService() {
        return apiWebService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginContext login(final Credentials credentials) {
        return getLoginMgr().login(SoapUtils.createPort(getApiWebService().getService(), getApiWebService().getPortType(), getApiWebService().getWebServiceType().getSessionUrlFactory().computeUrl(credentials, getApiWebService().getService())), credentials);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(final LoginContext loginContext) {
        getLoginMgr().logout(SessionPortFactory.createSessionPort(getApiWebService(), loginContext));
    }
}
