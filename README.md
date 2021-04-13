# Blogger project
Functional Requirement: https://bitly.com.vn/h7f7td  

Blogger is splited into two part
1. Frontend (Angular): https://github.com/havinhphu188/blogger-client
2. Backend (Spring Boot): https://github.com/havinhphu188/blogger-demo-backend  

Live demo (Deployed in heroku): https://simple-blogger-3000.herokuapp.com/  

## Prerequisite
- Java 11
- nodejs

## Setup environment for frontend
- run `npm install` 
- After all package is installed, run `ng serve`
- Frontend is now served in http://localhost:4200/

## Setup environment for backend
### Database
+ Project is using postgres as relational database
+ script: 
  - Schema: src\main\resources\schema.sql
  - Data for integration test: src\test\resources\data-for-testing.sql
+ username/password: By default, project is configured with username, password as below. Modification may be necessary to adapt local database. 
  - username: postgres
  - password: asd123
+ Create 2 database with name below:
  - blogger-demo: import script `schema.sql`
  - blogger-integration-test: import `schema.sql`, `data-for-testing.sql`, respectively. 
### Run backend API
- run `mvnw spring-boot:run`. Backend API is now served at http://localhost:8080/
### Run unit test & integration test
- run `mvnw test`
### Setup IDE for development
- IntelliJ IDEA or Eclipse with @Lombok plugin. https://projectlombok.org/setup/intellij
