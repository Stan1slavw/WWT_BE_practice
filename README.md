# WWT_BE Practice Project

This project demonstrates two simple Spring Boot services (`auth-api` and `data-api`) running with PostgreSQL via Docker Compose. The goal is to show basic JWT authentication, internal service-to-service communication, and request logging.

## Architecture

- **auth-api** (port `8080`)  
  Provides user registration and login with JWT authentication.  
  Exposes a protected `/api/process` endpoint:  
  - Validates JWT  
  - Calls `data-api` with an internal token  
  - Logs the request/response in PostgreSQL  

- **data-api** (port `8081`)  
  Exposes `/api/transform` endpoint.  
  Requires a valid `X-Internal-Token` header.  
  Applies a simple text transformation: **reverse + uppercase**.  
  Returns:
  ```json
  { "result": "<TRANSFORMED_TEXT>" }
  ```

- **Postgres** (port `5433` on host в†’ `5432` in container)  
  Stores:  
  - `users` table  
  - `processing_log` table  
  Managed with **Flyway migrations**.

## Requirements

- Docker  
- Docker Compose  
- cURL (or Postman / HTTP client)  
- Optional: pgAdmin or psql for DB inspection  

## Running the stack

Clone the repository:
```bash
git clone https://github.com/Stan1slavw/WWT_BE_practice
cd WWT_BE_practice
```

Build and start with Docker Compose:
```bash
docker compose up -d --build
docker compose ps
```

You should see:  
- **auth-api** в†’ [http://localhost:8080](http://localhost:8080)  
- **data-api** в†’ [http://localhost:8081](http://localhost:8081)  
- **postgres** в†’ `localhost:5433`  

## Testing

### 1. Register a new user
```bash
curl -i -X POST http://localhost:8080/api/auth/register   -H "Content-Type: application/json"   -d '{"email":"a@a.com","password":"pass"}'
```
Response: `201 Created`

### 2. Login to get a JWT
```bash
curl -s -X POST http://localhost:8080/api/auth/login   -H "Content-Type: application/json"   -d '{"email":"a@a.com","password":"pass"}'
```
Example response:
```json
{"token":"<JWT_TOKEN>"}
```

### 3. Process text (protected endpoint)
```bash
curl -s -X POST http://localhost:8080/api/process   -H "Authorization: Bearer <JWT_TOKEN>"   -H "Content-Type: application/json"   -d '{"text":"hello"}'
```
Example response:
```json
{"result":"OLLEH"}
```

### 4. Direct call to data-api (should be rejected)
```bash
curl -i -X POST http://localhost:8081/api/transform   -H "Content-Type: application/json"   -d '{"text":"test"}'
```
Response:
```json
{"error":"forbidden"}
```

## Stopping the stack
```bash
docker compose down -v
```
