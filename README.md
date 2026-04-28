# Task API

API REST para gestion de tareas con enfoque de backend profesional: validaciones, paginacion, filtros, versionado optimista, migraciones y pipeline CI.

## Stack
- Java 17
- Spring Boot 4
- Spring Data JPA
- Flyway
- H2 (desarrollo local)
- PostgreSQL (perfil `prod`)
- JUnit + MockMvc
- Docker / Docker Compose
- GitHub Actions

## Funcionalidades
- CRUD completo de tareas.
- `PATCH` para actualizar solo el estado de una tarea.
- Listado con paginacion, filtros y orden:
  - `completed` (`true/false`)
  - `q` (busqueda por `title` y `description`)
  - `page`, `size`
  - `sortBy`, `sortDir`
- Validaciones de entrada (`@NotBlank`, `@Size`, `@NotNull`).
- Manejo centralizado de errores (`400`, `404`, `500`).
- Metadatos de auditoria (`createdAt`, `updatedAt`) y control de concurrencia (`version`).

## Ejecutar en local (H2)
1. Configura `JAVA_HOME` con JDK 17.
2. Ejecuta:
   - Linux/macOS: `./mvnw spring-boot:run`
   - Windows: `mvnw.cmd spring-boot:run`
3. Base URL: `http://localhost:9090`

### H2 Console
- URL: `http://localhost:9090/h2-console`
- JDBC URL: `jdbc:h2:mem:taskdb`
- User: `sa`
- Password: vacio

## Ejecutar con Docker (PostgreSQL + perfil prod)
```bash
docker compose up --build
```

La API queda disponible en `http://localhost:9090`.

## Endpoints
- `GET /tasks`
- `GET /tasks/{id}`
- `POST /tasks`
- `PUT /tasks/{id}`
- `PATCH /tasks/{id}/status`
- `DELETE /tasks/{id}`

### Ejemplo crear tarea
```json
{
  "title": "Preparar release",
  "description": "Actualizar changelog y validar pipeline",
  "completed": false
}
```

### Ejemplo actualizar estado
```json
{
  "completed": true
}
```

## Tests
```bash
./mvnw test
```

## CI
Se incluye workflow en `.github/workflows/ci.yml` para ejecutar tests automaticamente en `push` y `pull_request` sobre `main`.
