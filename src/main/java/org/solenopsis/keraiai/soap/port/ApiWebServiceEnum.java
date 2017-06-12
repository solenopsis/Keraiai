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
package org.solenopsis.keraiai.soap.port;

import javax.xml.ws.Service;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.soap.ApiWebService;
import org.solenopsis.keraiai.soap.LoginWebService;
import org.solenopsis.keraiai.soap.WebServiceType;
import org.solenopsis.keraiai.wsdl.apex.ApexPortType;
import org.solenopsis.keraiai.wsdl.apex.ApexService;
import org.solenopsis.keraiai.wsdl.enterprise.SforceService;
import org.solenopsis.keraiai.wsdl.enterprise.Soap;
import org.solenopsis.keraiai.wsdl.metadata.MetadataPortType;
import org.solenopsis.keraiai.wsdl.metadata.MetadataService;
import org.solenopsis.keraiai.wsdl.tooling.SforceServicePortType;
import org.solenopsis.keraiai.wsdl.tooling.SforceServiceService;

/**
 * This enum denotes the built in SFDC API web services.
 *
 * @author Scot P. Floess
 */
public enum ApiWebServiceEnum implements ApiWebService {
    APEX_SERVICE(WebServiceTypeEnum.APEX_SERVICE_TYPE, new ApexService(ApiWebServiceEnum.class.getClassLoader().getResource("wsdl/Keraiai-apex.wsdl")), ApexPortType.class),
    ENTERPRISE_SERVICE(WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE, new SforceService(ApiWebServiceEnum.class.getClassLoader().getResource("wsdl/Keraiai-enterprise.wsdl")), Soap.class),
    METADATA_SERVICE(WebServiceTypeEnum.METADATA_SERVICE_TYPE, new MetadataService(ApiWebServiceEnum.class.getClassLoader().getResource("wsdl/Keraiai-metadata.wsdl")), MetadataPortType.class),
    PARTNER_SERVICE(WebServiceTypeEnum.PARTNER_SERVICE_TYPE, new org.solenopsis.keraiai.wsdl.partner.SforceService(ApiWebServiceEnum.class.getClassLoader().getResource("wsdl/Keraiai-partner.wsdl")), org.solenopsis.keraiai.wsdl.partner.Soap.class),
    TOOLING_SERVICE(WebServiceTypeEnum.TOOLING_SERVICE_TYPE, new SforceServiceService(ApiWebServiceEnum.class.getClassLoader().getResource("wsdl/Keraiai-tooling.wsdl")), SforceServicePortType.class);

    /**
     * The web service type.
     */
    private final WebServiceType webServiceType;

    /**
     * The SFDC web service.
     */
    private final Service service;

    /**
     * The port for the web service.
     */
    private final Class portType;

    /**
     * This constructor sets the SFDC web service, port type and partial URL (as defined in the Java doc header).
     *
     * @param service  the SFDC web service.
     * @param portType the port for the web service.
     */
    private ApiWebServiceEnum(final WebServiceType webServiceType, final Service service, final Class portType) {
        this.webServiceType = webServiceType;
        this.service = service;
        this.portType = portType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebServiceType getWebServiceType() {
        return webServiceType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Service getService() {
        return service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getPortType() {
        return portType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createProxyPort(Credentials credentials, LoginWebService loginWebService) {
        return (P) getWebServiceType().createProxyPort(credentials, loginWebService, getService(), getPortType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P> P createProxyPort(final Credentials credentials) {
        return (P) getWebServiceType().createProxyPort(credentials, getService(), getPortType());
    }
}
