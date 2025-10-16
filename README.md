
# Vaudoise-test-technique

  

## Prerequisites

-  **Java 17+**
-  **Maven 3.9+**
-  **Docker Desktop** (or Docker Engine)
-  **Git** (to clone the repo)

> On Windows: make sure `java -version` and `mvn -v` work in your terminal.



  

## Quick start

```bash
# 1) Start Postgres with Docker
docker  compose  up  -d

# 2) Run the backend
mvn  spring-boot:run
```

The API starts on **http://localhost:8080**.

Postgres runs on **localhost:5432** with:
- user: `postgres`
- password: `postgres`
- database: `vaudoise_api`

## First run checklist

  

1)  **Create DB container**

```bash
docker  compose  up  -d

# verify it's up:
docker  ps
```

2)  **App configuration** (already set for local dev)

`src/main/resources/application.properties` should contain (or equivalent):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/vaudoise_api

spring.datasource.username=postgres

spring.datasource.password=postgres

  

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true

spring.jpa.open-in-view=false
```

> If you changed the DB credentials, update them here.

3)  **Run the app**

```bash
mvn  spring-boot:run
```
You should see logs ending with:
```
Tomcat started on port 8080 ... Started ApiFactoryApplication ...
```

## Testing

### Using Postman

- Import the **Postman collection** from `postman/Vaudoise API tests.postman_collection.json`.
- Run the requests in the Postman client to test the API.
---

 ### Optional testing interface
 - An **index.html** file is located in `frontend/index.html`.
 - Can be opened in a browser and used to test the functionality of the API directly.

## Common pitfalls

-  **Port 5432 already in use**: stop any local Postgres or change the port in `docker-compose.yml` and `application.properties`.
-  **Maven not found**: install Maven and add it to `PATH`.
-  **Java version**: ensure you’re on **Java 17+** (`java -version`).

## Architecture & Design Explanation

The project follows a **layered architecture (Controller → Service → Repository → Entity)**, ensuring clear separation of concerns and easy maintenance.  
- **Entities** represent database tables (Client, Person, Company, Contract).  
- **Repositories** use Spring Data JPA to handle persistence, providing clean, declarative data access.  
- **Services** contain business logic such as validation, contract updates, and client deletion (soft deletetion used, it seemed more logical with the rest of the architecture).  
- **Controllers** expose a REST API in JSON format, handling validation and mapping between DTOs and entities.

This modular approach enhances **testability**, **readability**, and **extensibility**. DTOs are used to control data exposure (e.g., hiding internal fields like `updateDate`).  
PostgreSQL is used for persistence, managed through **Docker Compose** for easy setup.  
Validation is done via **Jakarta Validation** annotations, ensuring robust data integrity.  
Overall, the design is clean, RESTful, and optimized for scalability and potential future developments.

## Proof of Functionality

The API’s correctness and reliability can be easily verified through multiple testing options:

1. **Postman Collection** – A ready-to-use collection (`Vaudoise-API.postman_collection.json`) is provided.  
   It includes all main endpoints (Create, Read, Update, Delete for both Clients and Contracts).  
   Users can import it into Postman, set the `baseUrl` to `http://localhost:8080`, and run each request in sequence to validate full workflow behavior.

2. **Web Interface** – A lightweight test interface (`index.html`) is available in the repository.  
   Opening it in any browser provides a clean and intuitive way to interact with the API, without needing additional tools.

3. **Automated Validation** – Input validation is enforced using **Jakarta Validation** annotations (email, phone, date, etc.), ensuring that invalid payloads are rejected with meaningful HTTP 400 responses.

4. **Data Consistency** – The system automatically updates contract end dates when a client is deleted and maintains accurate update timestamps internally.

Together, these layers of testing and validation demonstrate that the API behaves consistently, adheres to REST standards, and fulfills all functional requirements.

