# Quarkus - Japan JBoss User Group 2019

This document describes how to create your first `Quarkus` application.

## Preparation
- GraalVM Community Edition 19.2.1
    - https://github.com/oracle/graal/releases/tag/vm-19.2.1
    - Mac or Linux or Windows
    - Set environment valiable GRAALVM_HOME="Graal install-dir"
    - install Native Image by `gu install native-image`
    - NOTE: Quarkus1.0.1 does not support GraalVM Community Edition 19.3.0 for now.

- Maven
    - 3.5.3 or higher
    - This document is tested on Maven 3.6.2

## STEP1: Create "Quarkus" blank project

```
mvn io.quarkus:quarkus-maven-plugin:1.0.1.Final:create \
    -DprojectGroupId=sample \
    -DprojectArtifactId=quarkus-sample \
    -DprojectVersion=0.1
```

Blank project contains as the following files:

```
$ tree -a quarkus-sample
quarkus-sample
├── .dockerignore
├── .gitignore
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

11 directories, 12 files
```


## STEP2: Enjoy coding

Create new HelloResource.java file to src/main/java/sample/HelloResource.java

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

## STEP3: Start your first application on HotSpot JVM

```
java -jar target/quarkus-sample-0.1-runner.jar
2019-12-03 22:42:24,117 INFO  [io.quarkus] (main) quarkus-sample 0.1 (running on Quarkus 1.0.1.Final) started in 0.857s. Listening on: http://0.0.0.0:8080
2019-12-03 22:42:24,126 INFO  [io.quarkus] (main) Profile prod activated.
2019-12-03 22:42:24,126 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy]

curl localhost:8080/hello
Hello, World!
```

## STEP4: Create native images

```
./mvnw clean package -Pnative
```

It take about `90` seconds on my machine (Mac book pro late 2016).

## STEP5: Start your native application

You can experience `Supersonic Subatomic Java` by Quarkus.

```
cd target
./quarkus-sample-0.1-runner
2019-12-03 22:48:48,114 INFO  [io.quarkus] (main) quarkus-sample 0.1 (running on Quarkus 1.0.1.Final) started in 0.009s. Listening on: http://0.0.0.0:8080
2019-12-03 22:48:48,114 INFO  [io.quarkus] (main) Profile prod activated.
2019-12-03 22:48:48,114 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy]

curl localhost:8080/hello
Hello, World!
```

## Add extention to sample project (Optinal)

### Check available extensions

Quarkus extension is jar libraries that can compile by `native-image` command and can work on SubstrateVM.

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
urrent Quarkus extensions available:
Quarkus - Core                                     quarkus-core
JAXB                                               quarkus-jaxb
Jackson                                            quarkus-jackson
JSON-B                                             quarkus-jsonb
JSON-P                                             quarkus-jsonp
Agroal - Database connection pool                  quarkus-agroal
Artemis JMS                                        quarkus-artemis-jms
Elytron Security                                   quarkus-elytron-security
Properties File based Security                     quarkus-elytron-security-properties-file
Elytron Security OAuth 2.0                         quarkus-elytron-security-oauth2
...
```

### Add extension

In this example, add a extensions to pom.xml via `mvnw quarkus:add-extension -Dextensions=a, b, c`.

- quarkus-resteasy-jsonb

```
./mvnw quarkus:add-extension -Dextensions="quarkus-resteasy-jsonb"
...
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------------< sample:quarkus-sample >------------------------
[INFO] Building quarkus-sample 0.1
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- quarkus-maven-plugin:1.0.1.Final:add-extension (default-cli) @ quarkus-sample ---
✅ Adding extension io.quarkus:quarkus-resteasy-jsonb
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.976 s
[INFO] Finished at: 2019-12-03T23:12:13+09:00
[INFO] ------------------------------------------------------------------------
...
```

The dependency `quarkus-resteasy-jsonb` was added by `quarkus:add-extension`.

```xml
vim pom.xml
...
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-resteasy-jsonb</artifactId>
    </dependency>
...
```

Then, remove redundant extension from pom.xml
```
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-resteasy</artifactId>
    </dependency>
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
