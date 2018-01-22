README
====

Initialisation
----
The application uses a PostGreSQL database, located at localhost:5432 with user postgres/passw0rd and schema form3.

To run the database you can run the docker-compose (or have your own PostGreSQL at localhost:5432) :

```
$ docker-compose up
```

NB : this works on Linux and MacOS, not sure with Windows Docker Toolbox. On Windows, please use your own PostGreSQL instance.

Running the application
----
To run the application :
  * mvn clean install
  * java -jar target/challenge-1.0.0.jar
  * open your browser at http://localhost:8080
  
Flyway will handle the schema and database creation and insert some data.
  
Specifications
----
This application runs :
  * Spring Boot 1.5.9 (Spring core 4.3.13)
  * Spring web with undertow instead of Tomcat (lighter, faster, well fit for our needs)
  * Spring actuator
  * Spring Data JPA over Hibernate
  * HikariCP for the database connection pool
  * Flyway to handle the database schema migrations and creations
  * A PostGreSQL database
  * Jackson to handle JSON
  * Lombok to write less code
  * MapStruct to map DTO to DAO and reverse
  * Swagger for the API documentation
  * HATEOAS to include the REST links in the JSON results
  
Architecture details
----
As a Spring Boot application, we have the following architecture :
  * web : contains the Controllers, the endpoints of our API
  * service : contains the business service, handling transactions
  * repository : contains the Spring Data JPA interface to query the database
  * entity : the database model
  * dto : the representation view of what is exposed to the client
  * exceptions : the exceptions thrown by the application
  * mapper : the MapStruct mappers
  * config : the Swagger configuration
  * util : the PostGreSQL JSON datatype handler

The web/RestExceptionHandler handles the REST exception, to give some feedback to the client in case of error.

This API follows the Spring Boot architecture standards :
  1. The request enters by the RestController
  1. The controller calls the service
  1. The service does the business checks, and calls the repository
  1. The repository does the necessary requests to the database
  1. The response sent to the client is data in JSON format, with HATEOAS links

Using the API
----
This API exposes the following payment's endpoints :
  * GET http://localhost:8080/v1/payments : the list of all payments in database
  * GET http://localhost:8080/v1/payments/{id} : retrieve a specific payment
  * PUT http://localhost:8080/v1/payments/{id} : update a specific payment (Payment data in the body)
  * POST http://localhost:8080/v1/payments : create a specific payment (Payment data in the body)
  * DELETE http://localhost:8080/v1/payments/{id} : delete a specific payment
  
The swagger documentation can be seen here : http://localhost:8080/swagger-ui.html

Run the tests
----
To run the tests :
  * mvn clean test

The tests are made with Cucumber for Spring and Java8 (lambdas). The features are written in src/test/resources/features/*.feature files.

The step definitions are in *StepDefs.java files.

The CucumberTest.java is the main file.

The CommonRestCallStepDefs.java could be used for every integration tests of every futures controllers. It defines all common given/when/then steps for REST endpoints testing.

The PaymentMapperStepDefs.java is a unit test of the mapping of Payment DTO and DAO.

The PaymentServiceAndRepositoryStepDefs.java contains all given/when/then steps to unit test repositories and services. I put these in a common file because a lot of steps are the same between the service and the repository.

A sql file is used to populate the database with data for the integration tests. We also empty the database (with truncate query) at the end of every test to be sure that the tests are not colliding.