# Sample application of JAX-RS/CDI/JPA with Panashe

## How to use 
1. Before you start application, you have to prepare PostgreSQL container:
```
docker run --rm --name quarkus -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:10.5
```

2. Build native image:
```
mvn clean package -Pnative
```

NOTE: native images build takes about `10` minutes on my machine.

3. Start native application:
```
target/quarkus_jpa-jaxrs-cdi-jta-0.1-runner
Hibernate:

    drop table if exists Employee cascade
...

2019-05-11 21:50:47,311 INFO  [io.quarkus] (main) Quarkus 0.14.0 started in 0.197s. Listening on: http://[::]:8080
2019-05-11 21:50:47,315 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, jdbc-postgresql, narayana-jta, resteasy, resteasy-jsonb]
```

## Included extensions

This application depends on the following Quarkus extensions:

- Hibernate ORM with Panache (io.quarkus:quarkus-hibernate-orm-panache)
- JDBC Driver - PostgreSQL (io.quarkus:quarkus-jdbc-postgresql)
- RESTEasy - JSON-B (io.quarkus:quarkus-resteasy-jsonb)
