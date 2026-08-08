# Student Management System

A full-stack, enterprise-ready **Student Management System** built with **Java 21**, **Spring Boot 3**, **Spring Data JPA**, **Thymeleaf**, and **Docker**.

## 🏗 Architecture Overview

The application follows the clean **Controller-Service-Repository-Entity** layered architecture pattern:

```
[ Web Browser / REST Client ]
           │
           ▼
[ Controller Layer ]
  ├── StudentRestController (REST Endpoints: /api/v1/students)
  └── StudentViewController (Thymeleaf UI: /)
           │
           ▼
[ Service Layer ]
  ├── StudentService (Interface)
  └── StudentServiceImpl (Business Logic & Validation)
           │
           ▼
[ Repository Layer ]
  └── StudentRepository (Spring Data JPA Data Access)
           │
           ▼
[ Entity Layer ]
  └── Student (JPA Entity mapped to MySQL / H2 Database)
```

---

## 🚀 Features

- **Full CRUD Operations**: Create, read, update, search, and delete student records.
- **RESTful API & Web UI**: Offers both JSON REST endpoints (`/api/v1/students`) and a modern, glassmorphic Thymeleaf dashboard UI.
- **Dual DB Support**: Runs out-of-the-box locally with H2 in-memory DB or with MySQL 8.0 in Docker.
- **Dockerized Environment**: Fully containerized using multi-stage Docker build and Docker Compose setup with database health checks.
- **Data Validation & Error Handling**: Jakarta Bean Validation on entities with global `@RestControllerAdvice` exception handling.
- **Auto Data Initializer**: Automatically seeds sample student records on initial application startup.

---

## 🛠 Prerequisites

- **Java 21** or **Docker & Docker Compose** installed on your system.

---

## 🐳 Running with Docker (Recommended)

To launch the complete application along with a MySQL 8.0 database using Docker Compose:

```bash
docker-compose up --build
```

Access the application:
- **Web UI Dashboard**: [http://localhost:8080](http://localhost:8080)
- **REST API Base URL**: `http://localhost:8080/api/v1/students`

To stop and remove containers:
```bash
docker-compose down -v
```

---

## 💻 Running Locally (without Docker)

Run using the Maven wrapper included in the project (uses H2 database by default):

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Access local endpoints:
- **Web Dashboard**: [http://localhost:8080](http://localhost:8080)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
  *(JDBC URL: `jdbc:h2:mem:studentdb`, Username: `sa`, Password: leave blank)*

---

## 📡 REST API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/students` | Get all students (supports `?search=` and `?department=` filters) |
| `GET` | `/api/v1/students/{id}` | Get student by ID |
| `POST` | `/api/v1/students` | Create a new student record |
| `PUT` | `/api/v1/students/{id}` | Update existing student record |
| `DELETE` | `/api/v1/students/{id}` | Delete student record |

### Sample JSON Request Body (`POST /api/v1/students`)

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@university.edu",
  "department": "Computer Science",
  "age": 22,
  "gpa": 3.90,
  "status": "ACTIVE"
}
```
