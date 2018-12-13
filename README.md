Example Hello Service with JWT authentication
=============================================

This simple service demonstrates how to authenticate spring boot services with JWT.


Build
-----

The project builds with maven.

`./mvnw clean package`

Run
---

The build produces an executable jar. It can be started with `java -DjwtSignKey=secretkey -jar target/hello-0.0.1-SNAPSHOT.jar`.

*secretkey* needs to match the secret key used in the authentication service.

Docker
------

A docker image can be build with `docker build --tag helloservice .`

The jwt signing key needs to be specified when starting the container as an environment variable `JWTSIGNINGKEY`.

`docker run -e JWTSIGNINGKEY=secretkey helloservice`