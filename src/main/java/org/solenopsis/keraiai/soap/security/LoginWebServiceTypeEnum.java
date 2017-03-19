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
package org.solenopsis.keraiai.soap.security;

import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.soap.SoapUtils;
import org.solenopsis.keraiai.SecurityMgr;
import org.solenopsis.keraiai.soap.port.WebServiceTypeEnum;

/**
 * Represents all login SOAP web service: enterprise, partner and tooling. Additionally provides the ability to create a usable
 * login port for logins.
 *
 * @author Scot P. Floess
 */
public enum LoginWebServiceTypeEnum implements LoginPortFactory {
    ENTERPRISE_LOGIN_SERVICE(WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE),
    PARTNER_LOGIN_SERVICE(WebServiceTypeEnum.PARTNER_SERVICE_TYPE),
    TOOLING_LOGIN_SERVICE(WebServiceTypeEnum.TOOLING_SERVICE_TYPE);

    /**
     * The actual web service type.
     */
    private final WebServiceTypeEnum apiWebserviceType;

    /**
     * This constructor sets the SFDC web service, port type and partial URL (as defined in the Java doc header).
     *
     * @param webService the SFDC web service.
     * @param portType   the port for the web service.
     */
    private LoginWebServiceTypeEnum(final WebServiceTypeEnum loginWebserviceType) {
        this.apiWebserviceType = loginWebserviceType;
    }

    /**
     * Return the login web service type.
     *
     * @return the login web service type.
     */
    public WebServiceTypeEnum getWebServiceType() {
        return apiWebserviceType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createLoginPort(final SecurityMgr securityMgr) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security mananger!");

        return SoapUtils.setUrl((P) getWebServiceType().getWebService().createPort(), SecurityUtils.computeLoginUrl(securityMgr, getWebServiceType()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createLoginSessionPort(final SecurityMgr securityMgr) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security mananger!");

        return getWebServiceType().createSessionPort(securityMgr, getWebServiceType().getWebService().getService());
    }
}
