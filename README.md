# Quarkus - Glassfish User Group Japan 2019 Spring

This document describes how to create my first `Quarkus` application.

## Preparation
- GraalVM Community Edition 1.0 RC16
    - https://github.com/oracle/graal/releases/tag/vm-1.0.0-rc16
    - Mac or Linux (GraalVM CE 1.0 RC16 does not support Windows)
    - Set environment valiable GRAALVM_HOME="Graal install-dir"
    - NOTE: Quarkus0.14.0 does not support GraalVM Community Edition 19.0.0 for now.

- Maven
    - 3.5.3 or higher
    - This document is tested on Maven 3.6.1

## STEP1: Create "Quarkus" blank project

```
mvn io.quarkus:quarkus-maven-plugin:0.14.0:create \
    -DprojectGroupId=sample \
    -DprojectArtifactId=quarkus-sample \
    -DprojectVersion=0.1
```

Blank project contains as the following files:

```
$ tree -a quarkus-sample
quarkus-sample
├── .dockerignore
├── .mvn
│   └── wrapper
│       ├── MavenWrapperDownloader.java
│       ├── maven-wrapper.jar
│       └── maven-wrapper.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src
    ├── main
    │   ├── docker
    │   │   ├── Dockerfile.jvm
    │   │   └── Dockerfile.native
    │   ├── java
    │   └── resources
    │       ├── META-INF
    │       │   └── resources
    │       │       └── index.html
    │       └── application.properties
    └── test
        └── java

11 directories, 11 files
```


## STEP2: Enjoy coding

Create new EchoResource.java file to src/main/java/sample/HelloResource.java

```java
package sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
public class HelloResource {
    @GET
    public String helloWorld() {
        return "Hello, World!";
    }
}
```

If you want to hotswap while developing time, you can that by `mvn quarkus:dev` command. Hotswap will be executed at receving new http request.
```
./mvnw clean compile quarkus:dev
```

Example codes are in `quarkus-sample` and `quarkus_jpa-jaxrs-cdi-jta` directory on this project.

## STEP3: Start first your application on HosSpot JVM

```
java -jar target/quarkus-sample-0.1-runner.jar
2019-05-11 20:25:17,805 INFO  [io.quarkus] (main) Quarkus 0.14.0 started in 1.075s. Listening on: http://[::]:8080
2019-05-11 20:25:17,817 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy]

curl localhost:8080/hello
Hello, World!
```

## STEP4: Create native images

```
./mvnw clean package -Pnative
```

It take about `180` seconds on my machine (Mac book pro late 2016).

## STEP5: Start your native application

You can experience `Supersonic Subatomic Java` by Quarkus.

```
cd target
./quarkus-sample-0.1-runner
2019-05-11 20:29:52,809 INFO  [io.quarkus] (main) Quarkus 0.14.0 started in 0.014s. Listening on: http://[::]:8080
2019-05-11 20:29:52,839 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy]

curl localhost:8080/hello
Hello, World!
```

## Add extention to sample project (Optinal)

### Check available extensions

Quarkus extension is jar libraries that can compile by `native-image` command and can working on SubstrateVM.

For example original Undertow and those dependency library such as `xnio` can not create native-image with the following error message.
```
$ native-image -jar undertow-standalone-jar-with-dependencies.jar
Warning: Abort stand-alone image build. Unsupported features in 3 methods
Detailed message:
Error: com.oracle.graal.pointsto.constraints.UnsupportedFeatureException: Detected a direct/mapped ByteBuffer in the image heap. A direct ByteBuffer has a pointer to unmanaged C memory, and C memory from the image generator is not available at image run time. A mapped ByteBuffer references a file descriptor, which is no longer open and mapped at run time. The object was probably created by a class initializer and is reachable from a static field. By default, all class initialization is done during native image building.You can manually delay class initialization to image run time by using the option --delay-class-initialization-to-runtime=<class-name>. Or you can write your own initialization methods and call them explicitly from your main entry point.
Trace:
        at parsing io.undertow.server.protocol.ajp.AjpServerRequestConduit.read(AjpServerRequestConduit.java:195)
Call path from entry point to io.undertow.server.protocol.ajp.AjpServerRequestConduit.read(ByteBuffer):
        at io.undertow.server.protocol.ajp.AjpServerRequestConduit.read(AjpServerRequestConduit.java:183)
        at org.xnio.conduits.ConduitStreamSourceChannel.read(ConduitStreamSourceChannel.java:127)
```

One of Quarkus extension `Undertow (io.quarkus:quarkus-undertow)` can pass `native-image` command, because quarkus-undertow revises incompatible codes with SubstrateVM.

All extension list can check by `mvn quarkus:list-extensions` as the folowing:
```
cd quarku-sample
./mvnw quarkus:list-extensions
...
[INFO] --- quarkus-maven-plugin:0.14.0:list-extensions (default-cli) @ quarkus-sample ---
[INFO] Available extensions:
[INFO]   * Agroal - Database connection pool (io.quarkus:quarkus-agroal)
[INFO]   * Arc (io.quarkus:quarkus-arc)
[INFO]   * AWS Lambda (io.quarkus:quarkus-amazon-lambda)
[INFO]   * Camel Core (io.quarkus:quarkus-camel-core)
[INFO]   * Camel Infinispan (io.quarkus:quarkus-camel-infinispan)
[INFO]   * Camel Netty4 HTTP (io.quarkus:quarkus-camel-netty4-http)
[INFO]   * Camel Salesforce (io.quarkus:quarkus-camel-salesforce)
[INFO]   * Eclipse Vert.x (io.quarkus:quarkus-vertx)
[INFO]   * Flyway (io.quarkus:quarkus-flyway)
[INFO]   * Hibernate ORM (io.quarkus:quarkus-hibernate-orm)
...
```

### Add extension

In this example, add a extensions to pom.xml via `mvnw quarkus:add-extension -Dextensions=a, b, c`.

- io.quarkus:quarkus-resteasy-jsonb

```
./mvnw quarkus:add-extension -Dextensions="io.quarkus:io.quarkus:quarkus-resteasy-jsonb"
...
[INFO] --- quarkus-maven-plugin:0.14.0:add-extension (default-cli) @ quarkus-sample ---
Adding dependency io.quarkus:quarkus-resteasy-jsonb:jar
...
```

The dependency `quarkus-resteasy-jsonb` was added by `quarkus:add-extensions`.

Then, remove redundant extension from pom.xml
```
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-resteasy</artifactId>
    </dependency>
```

```xml
vim pom.xml
...
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-resteasy-jsonb</artifactId>
    </dependency>
...
```

Now, you can use JSONB on JAX-RS resource return type like the following:

./src/main/java/sample/HelloResource.java
```java
package sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/hello")
public class HelloResource {
    @GET
    public String helloWorld() {
        return "Hello, World!";
    }

    @GET
    @Path("/message")
    @Produces("application/json")
    public Message message() {
        Message msg = new Message();
        msg.setFrom("Norito Agetsuma");
        msg.setMessage("Welcome Quarkus world!");
        return msg;
    }
}
```

./src/main/java/sample/Message.java
```java
public class Message {
    private String from;
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```

Result:
```
curl localhost:8080/hello/message
{"from":"Norito Agetsuma","message":"Welcome Quarkus world!"}
```
