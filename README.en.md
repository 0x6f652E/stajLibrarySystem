## Staj Library System (Spring Boot)

[Türkçe README](README.md)

A simple Library Management System built with Spring Boot 3, Java 21, PostgreSQL, Spring Security (JWT), MapStruct, and Lombok.

### Features
- User registration and login (JWT)
- Library, shelf, author, and book CRUD
- Borrow/return processes
- API documentation with Swagger UI

### Requirements
- Java 21
- Maven 3.9+
- PostgreSQL 14+

### Setup
1) Create database and user (example):
```sql
CREATE DATABASE librarydb;
CREATE USER library_user WITH ENCRYPTED PASSWORD 'libpass';
GRANT ALL PRIVILEGES ON DATABASE librarydb TO library_user;
```

2) Check `src/main/resources/application.properties`:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/librarydb
spring.datasource.username=library_user
spring.datasource.password=libpass

spring.jpa.hibernate.ddl-auto=update
springdoc.swagger-ui.path=/swagger-ui.html

application.jwt.secret=changeThisSecretKey_DevOnly_ChangeThisToAtLeast32Chars!!
application.jwt.expiration=86400000
```
Replace `application.jwt.secret` with a strong value in secure environments.

3) Build and run:
```bash
mvn clean package
mvn spring-boot:run
```

App runs on `http://localhost:8080` by default.

### Swagger UI
`http://localhost:8080/swagger-ui.html`

Using JWT with Swagger:
- First obtain a token via `/api/auth/login`.
- Click "Authorize" on the top right.
- Enter: `Bearer <token>` (e.g., `Bearer eyJhbGciOiJI...`).
- Protected endpoints will now include the token.

### Default Admin User
Seeded on first startup by `com.ibb.library.config.DataSeeder`:
- Email: `admin@lms.local`
- Password: `Admin#123`

### Sample Domain Seeding
On first run, sample domain data (library, shelf, author, book) is created by `com.ibb.library.config.DomainSeeder` only when the DB is empty.

### Auth Endpoints
Base path: `/api/auth`

- POST `/register`
  - Body: `{ "firstName", "lastName", "email", "password" }`
- POST `/login`
  - Body: `{ "email", "password" }`
  - Response: `{ "token", ... }`

Use `Authorization: Bearer <token>` header for subsequent requests.

### Postman JWT Flow
1) Register (optional):
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "firstName": "Test",
  "lastName": "User",
  "email": "test@lms.local",
  "password": "Test#123"
}
```

2) Login (get token):
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@lms.local",
  "password": "Admin#123"
}
```
Response:
```json
{ "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
```

3) Protected request (e.g., search books):
```http
GET http://localhost:8080/api/books/search?title=Sapiens
Authorization: Bearer <token>
```

4) cURL examples:
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@lms.local","password":"Admin#123"}' | jq -r .token)

curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/books

curl -X POST http://localhost:8080/api/books \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"title":"New Book","bookshelfId":1,"authorIds":[1]}'
```

Postman collection: `postman/LibrarySystem.postman_collection.json`.
First run `Auth/Login` to auto-save `{{token}}`; other requests use `Authorization: Bearer {{token}}`.

### Export from Swagger to Postman
1) Get OpenAPI JSON from `http://localhost:8080/v3/api-docs` (or copy via Swagger UI Export → Raw OpenAPI).
2) Postman → Import → Link or Raw Text with that JSON.
3) Set collection-level Authorization as Bearer Token with `{{token}}` if needed.
4) Alternatively, use the ready-made collection in this repo.

### Internship Summary (Aug 25, 2025 - Sep 19, 2025)
This project was developed during an internship at Istanbul Metropolitan Municipality / Department of Information Technology / IT Branch Directorate.

The internship aimed to contribute to the municipality’s digital transformation by building a core Library Management System on Java Spring Boot and PostgreSQL, suitable for enterprise-scale use. It also served as a pre-prototype for a reusable core service architecture for future municipal services.

During the internship, the PostgreSQL environment was prepared, and the Library–Bookshelf–Book–Author entities and relations were modeled. DTO and Mapper (MapStruct) layers standardized data transfer. Spring Security with JWT enforced authentication and role-based access (ADMIN/USER). CRUD endpoints were built for user, library, and book management and tested via Postman and Swagger.

Borrow/return processes include multi-copy support, stock checks, and concurrency safety (pessimistic lock). Search endpoints provide pagination, filtering, and sorting for performance.

At the end, the project was delivered via GitHub to responsible staff. Both technical (Spring Boot, JPA/Hibernate, PostgreSQL, JWT, Exception Handling) and professional (enterprise development processes, team communication, documentation) skills were strengthened. The project largely achieved its goals and forms a solid base for future GUI, reporting, and penalty/notification features.

### License
Educational purposes.


