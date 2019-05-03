# Unipos
Unipos is a free microservice-based point-of-solution usable in Austria.

## License
Unipos is distributed under the GNU General Public License. This means,
that you are free to use, modify and distribute this software. However,
you have to make your distribution available to anyone under the
same license. Refer to the **LICENSE** file for details.

## Prerequisites
In order to use Unipos, make sure to have the following software installed on your machine:
* Java 8+
* Maven 3+
* Tomcat 8+
* MongoDB 3+

## Build
To quickly get started, run the following command at the root folder:

`mvn clean install`

## Run Development
For development purposes you can use the Maven Cargo Plugin to bootstrap
a Tomcat instance and deploy the artifacts. To do so, run:

`mvn -P cargo.run`

The app will be available at:

http://localhost:8085/data/#/companies

Go to "Mit Passwort anmelden" and then enter:

* User: **service**
* Password: **secret**

## Run Production
Unzip the file created at 

`dist/target/distribution-1.0.0-bin.zip`

And copy the WAR files into your Tomcat `webapps` directory.


