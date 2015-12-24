# Keraiai

Welcome to Keraiai - a Java communication library for SFDC.  This project will contain things like [wsimport] (https://docs.oracle.com/javase/6/docs/technotes/tools/share/wsimport.html) generated WSDLs for the enterprise, partner, metadata and tooling API.  Please note the WSDLs are simply from an empty Developer org but can be used for logins (to gain session ids), and to invoke the metadata or tooling APIs.

## Why Keraiai and not Lasius?

Currently, [Lasius] (https://github.com/solenopsis/Lasius) contains all WSDLs and generated Java code.  However, depending upon usage, one may not want all the framework code associated with that project.  Therefore, this project was created to support generated Java code from WSDLs (small project with no dependencies).

*It will be the sole responsibility of users to manage logins, session ids, etc.*

## What Does Keraiai Mean?

Like all [Solenopsis] (https://github.com/solenopsis) themes, we wanted to choose a Latin or Greek word related to ants.  Since this is project is for SFDC communication, we considered an ant's antenna.  The word [keraiai] (http://dictionary.reference.com/browse/antennae) is actually Greek and refers to an insect's horns.