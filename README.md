# Keraiai

Welcome to Keraiai - a Java communication library for SFDC.

![Build Status](http://flossware.no-ip.org:58080/buildStatus/icon?job=Solenopsis-Keraiai&style=plastic)

## Keraiai vs Lasius?

Currently, [Lasius](https://github.com/solenopsis/Lasius) contains WSDLs for the Enterprise, Partner, Metadata and Tooling APIs...as well as similar communications functionality as found here.  However, we decided to simplifiy the libraries:
* Keraiai will provide all communications related functionality.
* [Lasius](https://github.com/solenopsis/Lasius) will provide other general utility functionality.

## What Does Keraiai Mean?

Like all [Solenopsis](https://github.com/solenopsis) themes, we wanted to choose a Latin or Greek word related to ants.  Since this is project is for SFDC communication, we considered an ant's antenna.  The word [keraiai](http://dictionary.reference.com/browse/antennae) is actually Greek and refers to an insect's horns.

## Design Decisions

### Interfaces Located Within Parent Packages

When an interface can be shared across sub-packages, we chose to define those interfaces in parent packages.

### Enums

#### Implement Interfaces

If you browse the source code, you will note we make heavy use of enums which implement interfaces.  Doing so allows us to more naturally decouple and allows us to mock implementations in our unit tests.

#### Declarative Model

You may note our enums are more than simple named markers - they contain code as well as what we consider a "declarative" model.  As an example, refer to [SessionUrlFactoryEnum](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/session/SessionUrlFactoryEnum.java).  We have two varying pieces:
* The sub-URL for a session.
* The factory that computes the "name" of the web service.  For some SFDC web services, one uses the API version as the name of the web service.  For custom web services it is the name of the Apex class.

To illustrate:
* Apex: `${SALESFORCE URL}/services/Soap/s/39.0`
* Custom: `${SALESFORCE URL}/services/Soap/class/Foo`
* Enterprise: `${SALESFORCE URL}/services/Soap/c/39.0`
* Metadata: `${SALESFORCE URL}/services/Soap/m/39.0`
* Partner: `${SALESFORCE URL}/services/Soap/u/39.0`
* Tooling: `${SALESFORCE URL}/services/Soap/T/39.0`

Above you can see the sub-URL (`services/Soap/[varying portion]`) followed by the "name" of the webservice.

Using enums in this fashion allows us to declare how to do things vs if/else-if constructs.  This keeps the code simple and clean.

## How To

### Creating Web Service Ports

Web service ports can be created using [WebServiceTypeEnum](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/port/WebServiceTypeEnum.java)'s various `createProxyPort()` methods.  The returned ports will manage auto login, and re-login should your session become invalid.  A [LoginMgr](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/login/LoginMgr.java) implementation is required and can be found in the following:
* [Enterprise Login Manager](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/login/EnterpriseLoginMgr.java)
* [Partner Login Manager](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/login/PartnerLoginMgr.java)
* [Tooling Login Manager](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/login/ToolingLoginMgr.java)

Please note:  when omitted, the default [LoginMgr](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/login/LoginMgr.java) is the [Enterprise Login Manager](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/login/EnterpriseLoginMgr.java) in creating proxy ports.

Additionally, an implementation of [Credentials](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/Credentials.java) is needed whe creating a proxy port.  The following implementations are provided for your convenience:
* [File Credentials](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/credentials/FilePropertiesCredentials.java)
* [InputStream Credentials](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/credentials/InputStreamCredentials.java)
* [Properties Credentials](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/credentials/PropertiesCredentials.java)
* [String Credentials](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/credentials/StringCredentials.java)

#### Examples

Assume your credentials exist in ```${HOME}/sfdc.properties``` and contain the following fictitious values:

```
username   = myuser@mycompany.com
password   = MyPassword
token      = abcdefghijklmnopqrstuvwxy
url        = https://test.salesforce.com
apiVersion = 34.0
```

Also assume your WSDLs are found in your home directory:

```java
final FileInputStream fis = new FileInputStream("/home/myuser/sfdc.properties");

final Properties properties = new Properties();
properties.load(fis);

fis.close();

final Credentials credentials = new PropertiesCredentials(properties);
```

##### Apex Web Service

```java
final ApexPortType apexPort = WebServiceTypeEnum.APEX_SERVICE_TYPE.createProxyPort(credentials, ApexService.class, MyClass.class.getClassLoader().getResource("/home/myuser/apex.wsdl");
```

##### Custom Web Service

```java
final FooPortType fooPort = WebServiceTypeEnum.CUSTOM_SERVICE_TYPE.createProxyPort(credentials, FooService.class, MyClass.class.getClassLoader().getResource("/home/myuser/foo.wsdl");
```

##### Enterprise Web Service

```java
final Soap enterprisePort = WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE.createProxyPort(credentials, SforceService.class, MyClass.class.getClassLoader().getResource("/home/myuser/enterprise.wsdl");
```

##### Metadata API Web Service

```java
final MetadataPortType metadataPort = WebServiceTypeEnum.METADATA_SERVICE_TYPE.createProxyPort(credentials, MetadataService.class, MyClass.class.getClassLoader().getResource("/home/myuser/metadata.wsdl");
```

##### Partner Web Service

```java
final Soap partnerPort = WebServiceTypeEnum.PARTNER_SERVICE_TYPE.createProxyPort(credentials, SforceService.class, MyClass.class.getClassLoader().getResource("/home/myuser/partner.wsdl");
```

##### Tooling API Web Service

```java
final SforceServicePortType toolingPort = WebServiceTypeEnum.TOOLING_SERVICE_TYPE.createProxyPort(credentials, SforceServiceService.class, MyClass.class.getClassLoader().getResource("/home/myuser/tooling.wsdl");
```

The above can now be used like normal objects - there is no need to worry about session ids, logins, invalid session ids, etc.  All will be managed for you - and likely you can share the ports without needing to create ports for indivdual use cases.  It may be desirable to create a static utility/context class containing your port instances that can be used across your application.  Using the above as an example, suppose your application needs to use both the ```fooPort``` and ```enterprisePort```:

```java
public static final class SessionContext {
    public static final FooPortType FOO_PORT;
    public static final Soap ENTERPRISE_PORT;

    static {
        try(final FileInputStream fis = new FileInputStream("/home/myuser/sfdc.properties")) {

            final Properties properties = new Properties();
            properties.load(fis);

            final Credentials credentials = new PropertiesCredentials(properties);
            final SecurityMgr securityMgr = new EnterpriseSecurityMgr(credentials);

            FOO_PORT = WebServiceTypeEnum.CUSTOM_SERVICE_TYPE.createProxyPort(credentials, FooService.class, MyClass.class.getClassLoader().getResource("/home/myuser/foo.wsdl");
            ENTERPRISE_PORT = WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE.createProxyPortcredentials, SforceService.class, MyClass.class.getClassLoader().getResource("/home/myuser/enterprise.wsdl");
        }
    } catch (final IOException ioException) {
        // Do something...
    }
    
    private SessionContext() {
    }
}

```

Now your application can simply use SessionContext with no regard to logins, etc.

### Getting Your LoginContext

Above we illustrated how to create and use a proxy port.  You can also get your current [LoginContext](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/LoginContext.java) by casting the proxy port to a [LoginContext](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/LoginContext.java).  This works, because the proxy port implements both the web service port and [LoginContext](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/LoginContext.java) interfaces!

#### Examples

```java
final MetadataPortType metadataPort = WebServiceTypeEnum.METADATA_SERVICE_TYPE.createProxyPort(credentials, MetadataService.class, MyClass.class.getClassLoader().getResource("/home/myuser/metadata.wsdl");

final LoginContext loginContext = (LoginContext) metadataPort;
```

The advantage here is that your [LoginContext](https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/LoginContext.java) implicitly follows your web service port.

## Links

You may find the following links useful:
* [Maven Site Information](http://solenopsis.github.io/Keraiai/)
* [Java Docs](http://solenopsis.github.io/Keraiai/apidocs/)
* [Test Java Docs](http://solenopsis.github.io/Keraiai/testapidocs/)
