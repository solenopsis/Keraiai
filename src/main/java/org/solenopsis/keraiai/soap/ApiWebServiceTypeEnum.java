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
 * Denotes an SFDC web service type, and the partial URL for one needs when calling an SFDC web service:
 * <ul>
 * <li>Apex = services/Soap/s/</li>
 * <li>Custom = services/Soap/class/</li>
 * <li>Enterprise = services/Soap/c/</li>
 * <li>Metadata = services/Soap/m/</li>
 * <li>Partner = services/Soap/u/</li>
 * <li>Tooling = services/Soap/T/</li>
 * </ul>
 *
 * The fully qualified SFDC URLs for a web service always use the aforementioned partial URLs and a host plus either an API version
 * or a custom web service name. Here are some examples:
 * <ul>
 * <li>Apex = https://cs9.salesforce.com/services/Soap/s/31.0</li>
 * <li>Custom = https://cs9.salesforce.com/services/Soap/class/Foo</li>
 * <li>Enterprise = https://cs9.salesforce.com/services/Soap/m/32.0</li>
 * <li>Metadata = https://cs9.salesforce.com/services/Soap/m/33.0</li>
 * <li>Partner = https://cs9.salesforce.com/services/Soap/u/34.0</li>
 * <li>Tooling = https://cs9.salesforce.com/services/Soap/T/35.0</li>
 * </ul>
 *
 * When logging in, you have to use the typical production (https://login.salesforce.com) or sandbox (https://test.salesforce.com)
 * URLS with either the enterprise, partner or tooling partial URLs plus API version. Test login URL examples:
 * <ul>
 * <li>Enterprise = https://test.salesforce.com/services/Soap/m/32.0</li>
 * <li>Partner = https://test.salesforce.com/services/Soap/u/33.0</li>
 * <li>Tooling = https://test.salesforce.com/services/Soap/T/34.0</li>
 * </ul>
 *
 * Production login URL examples:
 * <ul>
 * <li>Enterprise = https://login.salesforce.com/services/Soap/m/32.0</li>
 * <li>Partner = https://login.salesforce.com/services/Soap/u/33.0</li>
 * <li>Tooling = https://login.salesforce.com/services/Soap/T/34.0</li>
 * </ul>
 *
 * Additionally, an instance of Keraia's web services are included, along with the class of the port those web services use. The
 * exception here is for user defined custom web services as those will always be defined by consumers of this library.
 *
 * @author Scot P. Floess
 */
public enum ApiWebServiceTypeEnum {
    APEX_SERVICE(WebServiceTypeEnum.APEX_TYPE, new ApexService(ApiWebServiceTypeEnum.class.getClassLoader().getResource("wsdl/Keraiai-apex.wsdl")), ApexPortType.class),
    ENTERPRISE_SERVICE(WebServiceTypeEnum.ENTERPRISE_TYPE, new SforceService(ApiWebServiceTypeEnum.class.getClassLoader().getResource("wsdl/Keraiai-enterprise.wsdl")), Soap.class),
    METADATA_SERVICE(WebServiceTypeEnum.METADATA_TYPE, new MetadataService(ApiWebServiceTypeEnum.class.getClassLoader().getResource("wsdl/Keraiai-metadata.wsdl")), MetadataPortType.class),
    PARTNER_SERVICE(WebServiceTypeEnum.PARTNER_TYPE, new org.solenopsis.keraiai.wsdl.partner.SforceService(ApiWebServiceTypeEnum.class.getClassLoader().getResource("wsdl/Keraiai-partner.wsdl")), org.solenopsis.keraiai.wsdl.partner.Soap.class),
    TOOLING_SERVICE(WebServiceTypeEnum.TOOLING_TYPE, new SforceServiceService(ApiWebServiceTypeEnum.class.getClassLoader().getResource("wsdl/Keraiai-tooling.wsdl")), SforceServicePortType.class);

    private final WebServiceTypeEnum webServiceType;

    /**
     * The SFDC web service.
     */
    private final Service webService;

    /**
     * The port for the web service.
     */
    private final Class portType;

    /**
     * This constructor sets the SFDC web service, port type and partial URL (as defined in the Java doc header).
     *
     * @param webService the SFDC web service.
     * @param portType the port for the web service.
     */
    private ApiWebServiceTypeEnum(final WebServiceTypeEnum webserviceType, final Service webService, final Class portType) {
        this.webServiceType = webserviceType;
        this.webService = webService;
        this.portType = portType;
    }

    public WebServiceTypeEnum getWebServiceType() {
        return webServiceType;
    }

    /**
     * Return the SFDC web service.
     *
     * @return the SFDC web service.
     */
    public Service getWebService() {
        return webService;
    }

    /**
     * Return the port for the web service.
     *
     * @return the port for the web service.
     */
    public Class getPortType() {
        return portType;
    }
}
