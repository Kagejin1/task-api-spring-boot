# Task API - Spring Boot

Backend REST API CRUD para tareas, desarrollada con Java y Spring Boot.

## Stack
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (en memoria)
- Lombok

## Ejecutar
1. Configura `JAVA_HOME` con JDK 17.
2. Ejecuta `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`).
3. Base URL: `http://localhost:9090`

## Endpoints
- `GET /tasks` - Lista todas las tareas
- `POST /tasks` - Crea una tarea
- `GET /tasks/{id}` - Obtiene una tarea por id
- `PUT /tasks/{id}` - Actualiza una tarea
- `DELETE /tasks/{id}` - Elimina una tarea

## Ejemplo POST /tasks
```json
{
  "title": "Estudiar Spring",
  "description": "Revisar JPA y validaciones",
  "completed": false
}
```

## H2 Console
- URL: `http://localhost:9090/h2-console`
- JDBC URL: `jdbc:h2:mem:taskdb`
- User: `sa`
- Password: vacio
