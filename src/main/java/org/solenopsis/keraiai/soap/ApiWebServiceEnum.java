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
package org.solenopsis.keraiai.soap;

import javax.xml.ws.Service;
import org.solenopsis.keraiai.wsdl.apex.ApexPortType;
import org.solenopsis.keraiai.wsdl.apex.ApexService;
import org.solenopsis.keraiai.wsdl.enterprise.SforceService;
import org.solenopsis.keraiai.wsdl.enterprise.Soap;
import org.solenopsis.keraiai.wsdl.metadata.MetadataPortType;
import org.solenopsis.keraiai.wsdl.metadata.MetadataService;
import org.solenopsis.keraiai.wsdl.tooling.SforceServicePortType;
import org.solenopsis.keraiai.wsdl.tooling.SforceServiceService;

/**
 * This enum denotes the built in API SFDC web services.
 *
 * @author Scot P. Floess
 */
public enum ApiWebServiceEnum implements ApiWebService {
    APEX_SERVICE(new ApexService(ApiWebServiceEnum.class.getClassLoader().getResource("wsdl/Keraiai-apex.wsdl")), ApexPortType.class),
    ENTERPRISE_SERVICE(new SforceService(ApiWebServiceEnum.class.getClassLoader().getResource("wsdl/Keraiai-enterprise.wsdl")), Soap.class),
    METADATA_SERVICE(new MetadataService(ApiWebServiceEnum.class.getClassLoader().getResource("wsdl/Keraiai-metadata.wsdl")), MetadataPortType.class),
    PARTNER_SERVICE(new org.solenopsis.keraiai.wsdl.partner.SforceService(ApiWebServiceEnum.class.getClassLoader().getResource("wsdl/Keraiai-partner.wsdl")), org.solenopsis.keraiai.wsdl.partner.Soap.class),
    TOOLING_SERVICE(new SforceServiceService(ApiWebServiceEnum.class.getClassLoader().getResource("wsdl/Keraiai-tooling.wsdl")), SforceServicePortType.class);

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
    private ApiWebServiceEnum(final Service service, final Class portType) {
        this.service = service;
        this.portType = portType;
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
}
