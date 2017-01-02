# Keraiai

Welcome to Keraiai - a Java communication library for SFDC.

## Keraiai vs Lasius?

Currently, [Lasius] (https://github.com/solenopsis/Lasius) contains WSDLs for the Enterprise, Partner, Metadata and Tooling APIs...as well as similar communications functionality as found here.  However, we decided to simplifiy the libraries:
* Keraiai will provide all communications related functionality.
* [Lasius] (https://github.com/solenopsis/Lasius) will provide other general utility functionality.

## What Does Keraiai Mean?

Like all [Solenopsis] (https://github.com/solenopsis) themes, we wanted to choose a Latin or Greek word related to ants.  Since this is project is for SFDC communication, we considered an ant's antenna.  The word [keraiai] (http://dictionary.reference.com/browse/antennae) is actually Greek and refers to an insect's horns.

## How To

### Creating Web Service Ports

Web service ports can be created using [WebServiceTypeEnum] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/port/WebServiceTypeEnum.java)'s various `createSessionPort()` methods.  The returned ports will manage auto login, and re-login should your session become invalid.  A [SecurityMgr] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/SecurityMgr.java) implementation will be required.  Those can be found as follows:
* [Enterprise Security Manager] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/security/enterprise/EnterpriseSecurityMgr.java)
* [Partner Security Manager] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/security/partner/PartnerSecurityMgr.java)
* [Tooling Security Manager] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/security/tooling/ToolingSecurityMgr.java)

Additionally, an implementation of [Credentials] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/Credentials.java) is needed to construct the aforemention  [SecurityMgr] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/SecurityMgr.java) implementations.  The following implementations are provided:
* [Properties Credentials] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/credentials/PropertiesCredentials.java)
* [String Credentials] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/credentials/StringCredentials.java)

#### Examples

Assume your credentials exist in ```${HOME}/sfdc.properties``` and contain the following fictitious values:

```
username   = myuser@mycompany.com
password   = MyPassword
token      = abcdefghijklmnopqrstuvwxy
url        = https://test.salesforce.com
apiVersion = 34.0
```

Also assume your [SecurityMgr] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/SecurityMgr.java) implementation is the [Enterprise Security Manager] (https://github.com/solenopsis/Keraiai/blob/master/src/main/java/org/solenopsis/keraiai/soap/security/enterprise/EnterpriseSecurityMgr.java) and all your WSDLs are found in your home directory:

```java
final FileInputStream fis = new FileInputStream("/home/myuser/sfdc.properties");

final Properties properties = new Properties();
properties.load(fis);

fis.close();

final Credentials credentials = new PropertiesCredentials(properties);
final SecurityMgr securityMgr = new EnterpriseSecurityMgr(credentials);
```

##### Apex Web Service

```java
final ApexPortType apexPort = WebServiceTypeEnum.APEX_SERVICE_TYPE.createSessionPort(securityMgr, ApexService.class, MyClass.class.getClassLoader().getResource("/home/myuser/apex.wsdl");
```

##### Custom Web Service

```java
final FooPortType fooPort = WebServiceTypeEnum.CUSTOM_SERVICE_TYPE.createSessionPort(securityMgr, FooService.class, MyClass.class.getClassLoader().getResource("/home/myuser/foo.wsdl");
```

##### Enterprise Web Service

```java
final Soap enterprisePort = WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE.createSessionPort(securityMgr, SforceService.class, MyClass.class.getClassLoader().getResource("/home/myuser/enterprise.wsdl");
```

##### Metadata API Web Service

```java
final MetadataPortType metadataPort = WebServiceTypeEnum.METADATA_SERVICE_TYPE.createSessionPort(securityMgr, MetadataService.class, MyClass.class.getClassLoader().getResource("/home/myuser/metadata.wsdl");
```

##### Partner Web Service

```java
final Soap partnerPort = WebServiceTypeEnum.PARTNER_SERVICE_TYPE.createSessionPort(securityMgr, SforceService.class, MyClass.class.getClassLoader().getResource("/home/myuser/partner.wsdl");
```

##### Tooling API Web Service

```java
final SforceServicePortType toolingPort = WebServiceTypeEnum.TOOLING_SERVICE_TYPE.createSessionPort(securityMgr, SforceServiceService.class, MyClass.class.getClassLoader().getResource("/home/myuser/tooling.wsdl");
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

            FOO_PORT = WebServiceTypeEnum.CUSTOM_SERVICE_TYPE.createSessionPort(securityMgr, FooService.class, MyClass.class.getClassLoader().getResource("/home/myuser/foo.wsdl");
            ENTERPRISE_PORT = WebServiceTypeEnum.ENTERPRISE_SERVICE_TYPE.createSessionPort(securityMgr, SforceService.class, MyClass.class.getClassLoader().getResource("/home/myuser/enterprise.wsdl");
        }
    } catch (final IOException ioException) {
        // Do something...
    }
    
    private SessionContext() {
    }
}

```

Now your application can simply use SessionContext with no regard to logins, etc.

## Links

You may find the following links useful:
* [Maven Site Information] (http://solenopsis.github.io/Keraiai/)
* [Java Docs] (http://solenopsis.github.io/Keraiai/apidocs/)
* [Test Java Docs] (http://solenopsis.github.io/Keraiai/testapidocs/)