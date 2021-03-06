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
package zzzz.org.solenopsis.keraiai.soap.oldport;

import org.solenopsis.keraiai.soap.WebServiceType;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.flossware.jcore.utils.LoggerUtils;
import org.flossware.jcore.utils.ObjectUtils;
import org.flossware.jcore.utils.PauseUtils;
import org.flossware.jcore.utils.StringUtils;
import org.flossware.jcore.utils.soap.ServiceUtils;
import org.flossware.jcore.utils.soap.SoapUtils;
import org.solenopsis.keraiai.Credentials;
import org.solenopsis.keraiai.LoginContext;
import org.solenopsis.keraiai.soap.security.SecurityMgr;
import org.solenopsis.keraiai.credentials.CredentialsUtils;
import org.solenopsis.keraiai.soap.port.PortInvocationHandler;
import org.solenopsis.keraiai.soap.session.SessionIdSoapRequestHeaderHandler;
import org.solenopsis.keraiai.soap.port.WebServiceTypeEnum;
import zzzz.org.solenopsis.keraiai.soap.exception.SalesforceExceptionUtils;

/**
 * Utility class for port functionality.
 *
 * @author Scot P. Floess
 */
final class OldPortUtils {

    /**
     * Default pause time in millis.
     */
    final static int DEFAULT_PAUSE_TIME = 5000;

    /**
     * When setting up the soap header, we need to set the session header using this attribute.
     */
    final static String SESSION_HEADER = "SessionHeader";

    /**
     * Key when keeping track of invalid session ids on a call.
     */
    final static String INVALID_SESSION_ID_KEY = "invalid session id";

    /**
     * Key when keeping track of IOExceptions on a call.
     */
    final static String IOEXCEPTION_KEY = "IOException";

    /**
     * Key when keeping track of server unavailabe on a call.
     */
    final static String SERVER_UNAVAILABLE_KEY = "server unavailable";

    /**
     * Key when keeping track of unable to lock rows on a call.
     */
    final static String UNABLE_TO_LOCK_ROW_KEY = "unable to lock row";

    /**
     * Key when keeping track of service unavailables on a call.
     */
    final static String SERVICE_UNAVAILABLE_KEY = "service unavailable";

    /**
     * Maximum retries.
     */
    static final int MAX_RETRIES = 8;

    /**
     * Used to determine if methods are part of a LoginMgr.
     */
    final static Set<Method> SECURITY_MGR_METHOD_SET;

    /**
     * Our logger.
     */
    private static final Logger LOGGER;

    /**
     * Class initializer.
     */
    static {
        final Set<Method> securityMgrSet = new HashSet<>();

        // Cache all the methods from the security manager interface...
        for (final Method method : SecurityMgr.class.getMethods()) {
            securityMgrSet.add(method);
        }

        SECURITY_MGR_METHOD_SET = Collections.unmodifiableSet(securityMgrSet);

        LOGGER = Logger.getLogger(OldPortUtils.class.getName());
    }

    /**
     * Return the LOGGER.
     */
    private static Logger getLogger() {
        return LOGGER;
    }

    /**
     * Returns the security manager methods.
     *
     * @return the set of security manager methods.
     */
    static Set<Method> getSecurityMgrMethodSet() {
        return SECURITY_MGR_METHOD_SET;
    }

    /**
     * Using service, create a QName for the SOAP session header.
     *
     * @param service the service for whom we desire a QName for the session header.
     *
     * @return a QName for the session header.
     *
     * @throws IllegalArgumentException if service is null.
     */
    static QName computeSessionHeaderNameForNamespace(final String namespaceUri) {
        StringUtils.ensureString(namespaceUri, "Must provide a namespace URI!");

        return new QName(namespaceUri, SESSION_HEADER);
    }

    /**
     * Using service, create a QName for the SOAP session header.
     *
     * @param service the service for whom we desire a QName for the session header.
     *
     * @return a QName for the session header.
     *
     * @throws IllegalArgumentException if service is null.
     */
    static QName computeSessionHeaderNameForQname(final QName serviceName) {
        ObjectUtils.ensureObject(serviceName, "Must provide a service QName!");

        return computeSessionHeaderNameForNamespace(serviceName.getNamespaceURI());
    }

    /**
     * Using service, create a QName for the SOAP session header.
     *
     * @param service the service for whom we desire a QName for the session header.
     *
     * @return a QName for the session header.
     *
     * @throws IllegalArgumentException if service is null.
     */
    static QName computeSessionHeaderNameForService(final Service service) {
        ObjectUtils.ensureObject(service, "Must provide a service!");

        return computeSessionHeaderNameForQname(service.getServiceName());
    }

    /**
     * Can we retry a call?
     *
     * @param totalCalls the total calls made thus far.
     *
     * @return true if a call can be retried or false if we've reached our limit.
     */
    static boolean isCallRetriable(int totalCalls) {
        return totalCalls < MAX_RETRIES;
    }

    /**
     * W
     */
    static void incrementFailureCount(final String key, final Map<String, Integer> callFailureTotal) {
        Integer total = callFailureTotal.get(key);

        callFailureTotal.put(key, null == total ? 1 : total + 1);

        LoggerUtils.log(getLogger(), Level.INFO, "When calling Salesforce, encountered a [{0}]", key);
    }

    /**
     * Increment a failure count if <code>isToIncrement</code> is true.
     */
    static boolean incrementFailureCount(final boolean isToIncrement, final String key, final Map<String, Integer> callFailureTotal) {
        if (isToIncrement) {
            incrementFailureCount(key, callFailureTotal);
        }

        return isToIncrement;
    }

    /**
     * Returns true if the failure represents one where relogin should occur.
     *
     * @param failure the exception to examine if relogin is necessary.
     *
     * @return true if relogin is necessary.
     */
    static boolean isReloginException(final Throwable failure, final Map<String, Integer> callFailureTotal) {
        return incrementFailureCount(SalesforceExceptionUtils.isInvalidSessionIdException(failure), INVALID_SESSION_ID_KEY, callFailureTotal)
               || incrementFailureCount(SalesforceExceptionUtils.containsIOException(failure), IOEXCEPTION_KEY, callFailureTotal);
    }

    /**
     * Returns true if the failure represents one where a retry should occur.
     *
     * @param failure the exception to examine if retry is necessary.
     *
     * @return true if retry is necessary.
     */
    static boolean isRetryException(final Throwable failure, final Map<String, Integer> callFailureTotal) {
        return incrementFailureCount(SalesforceExceptionUtils.isServerUnavailableException(failure), SERVER_UNAVAILABLE_KEY, callFailureTotal)
               || incrementFailureCount(SalesforceExceptionUtils.isUnableToLockRowException(failure), UNABLE_TO_LOCK_ROW_KEY, callFailureTotal)
               || incrementFailureCount(SalesforceExceptionUtils.isServiceUnavailableException(failure), SERVICE_UNAVAILABLE_KEY, callFailureTotal);
    }

    /**
     * When an exception happens on call, this method will handle the exception.
     *
     * @param callFailure the exception that arose when calling SFDC.
     * @param method      the method being called when the failure arose.
     *
     * @return true if a relogin is necessary.
     *
     * @throws Throwable if the exception cannot be handled.
     */
    static void processException(final PortInvocationHandler handler, final Object proxy, final Method method, final Throwable callFailure, final Map<String, Integer> callFailureTotal) throws Throwable {
        if (isRetryException(callFailure, callFailureTotal)) {
            LoggerUtils.log(getLogger(), Level.WARNING, callFailure, "Received a retry exception (will attempt to perform call) when calling [{0}.{1}]", proxy.getClass().getName(), method.getName());

            PauseUtils.randomPause(DEFAULT_PAUSE_TIME);

            return;
        } else if (isReloginException(callFailure, callFailureTotal)) {
            LoggerUtils.log(getLogger(), Level.WARNING, callFailure, "Received a relogin exception when calling [{0}.{1}]", proxy.getClass().getName(), method.getName());

            handler.getSecurityMgr().resetSession();
            handler.getPort().set(createSessionPort(handler.getSecurityMgr(), handler.getService(), handler.getPortType(), handler.getUrl()));
        }

        LoggerUtils.log(getLogger(), Level.SEVERE, callFailure, "Trouble calling [{0}.{1}]...raising exception", proxy.getClass().getName(), method.getName());

        throw callFailure;
    }

    /**
     * Determine if <code>webServiceType</code> is a CUSTOM_SERVICE_TYPE, returning true if so, or false otherwise.
     *
     * @param webServiceType to examine if a CUSTOM_SERVICE_TYPE.
     *
     * @return true if <code>webServiceType</code> is a CUSTOM_SERVICE_TYPE or false if not.
     */
    static boolean isCustomWebService(final WebServiceType webServiceType) {
        return webServiceType == WebServiceTypeEnum.CUSTOM_SERVICE_TYPE;
    }

    /**
     * Compute the login URL from credentials - the API version from the credentials is used in the URL.
     *
     * @param credentials    contains the "base" url and the API version used to construct the URL.
     * @param webServiceType is the type of web service being called. It contains the partial URL we need to compute a login URL.
     *
     * @return a login URL.
     *
     * @throws IllegalArgumentException if <code>credentials</code> or <code>webServiceType</code> are null.
     */
    static String computeLoginUrl(final Credentials credentials, final WebServiceType webServiceType) {
        CredentialsUtils.ensureCredentials(credentials, "Must provide credentials!");
        ObjectUtils.ensureObject(webServiceType, "Must provide a web service type!");

        return StringUtils.concatWithSeparator(false, "/", credentials.getUrl(), webServiceType.getWebServiceSubUrl().getPartialUrl(), credentials.getApiVersion());
    }

    /**
     * Compute the login URL from credentials - the API version from the credentials is used in the URL.
     *
     * @param securityMgr    contains credentials whose "base" url and the API version used to construct the URL.
     * @param webServiceType is the type of web service being called. It contains the partial URL we need to compute a login URL.
     *
     * @return a login URL.
     *
     * @throws IllegalArgumentException if <code>securityMgr</code> is null.
     */
    static String computeLoginUrl(final SecurityMgr securityMgr, final WebServiceType webServiceType) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security mananger!");
        ObjectUtils.ensureObject(webServiceType, "Must provide a web service type!");

        return computeLoginUrl(securityMgr.getCredentials(), webServiceType);
    }

    /**
     * Compute the port name for the <code>service</code> - if self is a custom web service, it's the name of the QName of the port
     * on the service, otherwise it's the API version.
     *
     * @param apiVersion     the API version.
     * @param webServiceType the type of web service.
     * @param service        contains the QName of the port and is used if self is a custom web service.
     *
     * @return the port name.
     *
     * @throws IllegalArgumentException if any of the params are null.
     */
    static String computePortNameFromApiVersion(final String apiVersion, final WebServiceType webServiceType, final Service service) {
        StringUtils.ensureString(apiVersion, "Must provide an API version!");
        ObjectUtils.ensureObject(webServiceType, "Must provide a web service type!");
        ObjectUtils.ensureObject(service, "Must provide a service!");

        return !isCustomWebService(webServiceType) ? apiVersion : ServiceUtils.getPortName(service.getClass());
    }

    /**
     * Compute the port name for the <code>service</code> - if self is a custom web service, it's the name of the QName of the port
     * on the service, otherwise it's the API version as found in the <code>credentials</code>.
     *
     * @param credentials    contains the API version from it's credentials.
     * @param webServiceType the type of web service.
     * @param service        contains the QName of the port and is used if self is a custom web service.
     *
     * @return the port name.
     *
     * @throws IllegalArgumentException if any of the params are null.
     */
    static String computePortNameFromCredentials(final Credentials credentials, final WebServiceType webServiceType, final Service service) {
        ObjectUtils.ensureObject(credentials, "Must provide credentials!");

        return computePortNameFromApiVersion(credentials.getApiVersion(), webServiceType, service);
    }

    /**
     * Compute the port name for the <code>service</code> - if self is a custom web service, it's the name of the QName of the port
     * on the service, otherwise it's the API version as found in the <code>securityMgr</code>'s credentials.
     *
     * @param securityMgr    contains the API version from it's credentials.
     * @param webServiceType the type of web service.
     * @param service        contains the QName of the port and is used if self is a custom web service.
     *
     * @return the port name.
     *
     * @throws IllegalArgumentException if any of the params are null.
     */
    static String computePortNameFromSecurityMgr(final SecurityMgr securityMgr, final WebServiceType webServiceType, final Service service) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");

        return computePortNameFromCredentials(securityMgr.getCredentials(), webServiceType, service);
    }

    /**
     * Compute the session URL using the base URL from the <code>securityMgr</code>'s session, the partial URL for a web service
     * and the name of the port. By port name, for custom services it is the name of the QName of the port for service, otherwise it
     * is the API version for from the credentials as found in the <code>securityMgr</code>'s credentials.
     *
     * @param securityMgr contains session id and credentials.
     *
     * @return the computed session URL.
     */
    static String computeSessionUrlFromBaseServerUrl(final String baseServerUrl, final WebServiceType webServiceType, final String portName) {
        StringUtils.ensureString(baseServerUrl, "Must provide a base server url!");
        StringUtils.ensureString(portName, "Must provide a port name!");

        return StringUtils.concatWithSeparator(false, "/", baseServerUrl, webServiceType.getWebServiceSubUrl().getPartialUrl(), portName);
    }

    /**
     * Compute the session URL using the base URL from the <code>securityMgr</code>'s session, the partial URL for a web service
     * and the name of the port. By port name, for custom services it is the name of the QName of the port for service, otherwise it
     * is the API version for from the credentials as found in the <code>securityMgr</code>'s credentials.
     *
     * @param securityMgr contains session id and credentials.
     *
     * @return the computed session URL.
     */
    static String computeSessionUrlFromLoginContext(final LoginContext loginContext, final WebServiceType webServiceType, final String portName) {
        ObjectUtils.ensureObject(loginContext, "Must provide a login context!");

        return computeSessionUrlFromBaseServerUrl(loginContext.getBaseServerUrl(), webServiceType, portName);
    }

    /**
     * Compute the session URL using the base URL from the <code>securityMgr</code>'s session, the partial URL for a web service
     * and the name of the port. By port name, for custom services it is the name of the QName of the port for service, otherwise it
     * is the API version for from the credentials as found in the <code>securityMgr</code>'s credentials.
     *
     * @param securityMgr contains session id and credentials.
     * @param service     if using a custom web service, will use the name of the QName of the port name of the service. Otherwise
     *                    it is the API version found in <code>securityMgr</code>'s session.
     *
     * @return the computed session URL.
     */
    static String computeUrl(final WebServiceType webServiceType, final SecurityMgr securityMgr, final Service service) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");

        return computeSessionUrlFromLoginContext(securityMgr.getSession(), webServiceType, computePortNameFromSecurityMgr(securityMgr, webServiceType, service));
    }

    static <P> P createPort(final SecurityMgr securityMgr, final Service service, final Class portType, final String url) {
        ObjectUtils.ensureObject(securityMgr, "Must provide a security manager!");
        ObjectUtils.ensureObject(service, "Must probide a service!");
        ObjectUtils.ensureObject(portType, "Must provide a port type!");
        StringUtils.ensureString(url, "Must provide a URL!");

        final P port = (P) service.getPort(portType);
        SoapUtils.setUrl(port, url);

        LoggerUtils.log(getLogger(), Level.FINE, "Port = [{0}]", port);

        return port;
    }

    static <P> P createPort(final WebServiceType webServiceType, final SecurityMgr securityMgr, final Service service, final Class portType) {
        return createPort(securityMgr, service, portType, computeUrl(webServiceType, securityMgr, service));
    }

    /**
     * Create a usable session port including a URL and session id .
     *
     * @param securityMgr contains the session id.
     * @param service     the service to call.
     * @param portType    used to retrieve a port from the service.
     * @param url         the URL for the port.
     *
     * @return a usable port that has session id and URL set.
     */
    static <P> P createSessionPort(final SecurityMgr securityMgr, final Service service, final Class portType, final String url) {
        final P port = createPort(securityMgr, service, portType, url);

        SoapUtils.setHandler(port, new SessionIdSoapRequestHeaderHandler(securityMgr, service));

        LoggerUtils.log(getLogger(), Level.FINE, "Session Port = [{0}]", port);

        return port;
    }

    static <P> P createSessionPort(final WebServiceType webServiceType, final SecurityMgr securityMgr, final Service service, final Class portType) {
        return createSessionPort(securityMgr, service, portType, computeUrl(webServiceType, securityMgr, service));
    }

    /**
     * Will return true if the method is defined in the LoginMgr interface.
     *
     * @param method used to determine if defined in the LoginMgr interface.
     *
     * @return true if the method is defined in the LoginMgr interface.
     *
     * @throws IllegalArgumentException if method is null.
     */
    static boolean isSecurityMgrMethod(final Method method) {
        return getSecurityMgrMethodSet().contains(ObjectUtils.ensureObject(method, "Must provide a method to seek!"));
    }

    /**
     * Default constructor not allowed.
     */
    private OldPortUtils() {
    }
}
