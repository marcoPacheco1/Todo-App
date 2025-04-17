## Breakable Toy I - Todo App Backend

This is the backend for the Todo List Manager, built with Spring Boot 3. It exposes a REST API that allows full CRUD operations for managing todos. It stores data in memory using ArrayList, so no database setup is required.


### Features

- RESTful API with 7 endpoints
- In-memory data storage using ArrayList
- Input validation with @Valid
- CORS support for frontend integration
- Health check endpoint
- Filter, sort, and paginate todos

### Stack

- Java 21
- Spring Boot 3.4
- Spring Web
- Spring Validation
- Spring DevTools

### Endpoints

- GET /health: Health check
- GET /todos: List todos with optional filters: done, name, priority, sortBy, sortDirection, page
- GET /todos/{id}: Get todo by ID
- POST /todos: Create a new todo
- PUT /todos/{id}: Update a todo
- POST /todos/{id}/done: Mark a todo as done
- PUT /todos/{id}/undone: Mark a todo as not done
- DELETE /todos/{id}: Delete a todo

### Installation

1. Clone the repo: 
```
git clone https://github.com/marcoPacheco1/Todo-App.git
cd backend-todoapp
```

2. Run the application:
```
mvn spring-boot:run
```

3. The backend will be available at:
```
http://localhost:9090
```
You can test if it's up by going to http://localhost:9090/health.

### Configuration
Default port is set in src/main/resources/application.properties:
```
server.port=9090
```

### Running Tests
```
mvn test
```
