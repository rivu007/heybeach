# HeyBeach - Picture market

## About
HeyBeach is a picture marketplace where users can upload, showcase and sell their original beach pictures.
The most liked pictures can be then be purchased by other users.
Only the most liked pictures are available for purchase by the community.
When a user purchases their desired picture, they will receive a physical print delivered to their home.

## Technology Stack
 - Java8
 - Spring security
 - json web token
 - H2 as dev/test db
 - Liquibase for database migrations. Change can be found under `resources/migrations`
 - Swagger for Api documentation
 - lombok(https://projectlombok.org/)

## Getting it running
1. Clone this project. Use `git clone https://github.com/rivu007/heybeach.git`
2. Run `mvn spring-boot:run` at the root of the repository, where the pom.xml file is located.
3. The application will be running at [http://localhost:8080](http://localhost:8080)!

There is one admin user accounts present to demonstrate the different levels of access to the endpoints in
the API:
```
Admin - admin:admin
```

Refer to the Swagger UI for the API endpoints:
```
http://localhost:8080/swagger-ui.html
```

Sample request:
```
CURL -X GET -H "Authorization:<JsonWebToken>" "http://localhost:8080/users/me"
```
replace `<JsonWebToken>` with the token you get after successful login.

## Database Schema Design
![heybeach-dbschema](https://cloud.githubusercontent.com/assets/5902213/24600400/c45c854a-1854-11e7-8327-b4d71f32effc.jpg)

### Using another database

Actually this demo is using an embedded H2 database that is automatically configured by Spring Boot. If you want to connect to another database you have to specify the connection in the *application.yml* in the resource directory. Here is an example for a MySQL DB:

```
spring:
  jpa:
    hibernate:
      # possible values: validate | update | create | create-drop | none
      ddl-auto: create-drop
  datasource:
    url: jdbc:mysql://localhost/myDatabase
    username: myUser
    password: myPassword
    driver-class-name: com.mysql.jdbc.Driver
```

*Hint: For other databases like MySQL sequences don't work for ID generation. So you have to change the GenerationType in the entity beans to 'AUTO' or 'IDENTITY'.*

You can find a reference of all application properties [here](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).

## Creator

**Abhilash Ghosh**

* <https://github.com/rivu007>


