# FRAPPE – Backend (Spring Boot API)

This is the backend component of the **FRAPPE** appointment booking platform.  
It is built with **Spring Boot** and provides REST APIs for student-supervisor interactions, calendar synchronization, and follow-up tracking.

## 📦 Technologies

- Java 17
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA (PostgreSQL)
- Gradle (Kotlin DSL)
- Docker

## 📁 Project Structure

```
src/
├── main/
│   ├── java/fr/imt_atlantique/frappe/
│   │   ├── controllers/      # REST controllers
│   │   ├── services/         # Business logic
│   │   ├── entities/         # JPA entities
│   │   ├── repositories/     # Spring Data JPA interfaces
│   │   ├── dtos/             # Data Transfer Objects
│   │   ├── configs/          # App and security configs
│   │   ├── exceptions/       # Centralized error handling
│   │   ├── filters/          # JWT authentication filter
│   │   ├── events/           # Domain events (e.g. MeetingRequestCreatedEvent)
│   │   └── specifications/   # Custom JPA queries
│   └── resources/
│       └── application.yml   # Configuration (uses environment variables)
```

## ⚙️ Configuration

The app uses environment variables, typically passed through Docker Compose.

Example configuration (in `application.yml`):

```yaml
spring:
  datasource:
    url: ${FRAPPE_DB_URL}
    username: ${FRAPPE_DB_USERNAME}
    password: ${FRAPPE_DB_PASSWORD}
  mail:
    host: ${FRAPPE_MAIL_SMTP_HOST}
    port: ${FRAPPE_MAIL_SMTP_PORT}
    username: ${FRAPPE_MAIL_USERNAME}
    password: ${FRAPPE_MAIL_PASSWORD}

frappe:
  mail:
    from: ${FRAPPE_MAIL_FROM}
    host: ${FRAPPE_MAIL_IMAP_HOST}
  security:
    jwt:
      secret-key: ${FRAPPE_JWT_SECRET_KEY}
    encryption:
      secret-key: ${FRAPPE_ENCRYPTION_SECRET_KEY}
  availability:
    api:
      url: ${FRAPPE_AVAILABILITY_API_URL}
  frontend:
    url: ${FRAPPE_FRONTEND_URL}
```

> 💡 Tip: `FRAPPE_JWT_SECRET_KEY` must be Base64-encoded.

## 🛠️ Development

### Prerequisites

- Java 17
- Docker (optional, for DB/maildev)
- PostgreSQL running on port 5432

### Run locally

```bash
# Start local DB and maildev
cd docker
docker compose up -d

# Then run the app
./gradlew bootRun
```

### Run tests

```bash
./gradlew test
```

## 📋 API Documentation

The Swagger UI is available at:  
`http://localhost:8080/api/v1/swagger-ui/index.html`

## 📤 Deployment

The backend is designed to be deployed via Docker using the `Dockerfile` in the root directory.  
For full-stack deployment, see the [`deployment`](https://gitlab-df.imt-atlantique.fr/frappe/deployment) repository.