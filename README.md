# DMS Auth-Only Backend

A minimal Spring Boot project extracted from your DMS backend that contains only **Authentication** (register + login + JWT).

## Endpoints
- `POST /api/auth/register` — body: `{{ "username": "...", "email": "...", "password": "..." }}`
- `POST /api/auth/login` — body: `{{ "usernameOrEmail": "...", "password": "..." }}`; returns JWT

## Setup
1. Update `src/main/resources/application.properties` with your DB user/pass and `jwt.secret`.
2. Create database `dms_auth_db` (or change the name).
3. Build & run:
   ```bash
   ./mvnw spring-boot:run
   ```

The project includes:
- Entities: `User`, `Role`
- Repositories: `UserRepository`, `RoleRepository`
- Security: `SecurityConfig`, `JwtAuthFilter`, `JwtUtils`, `UserDetailsServiceImpl`
- Service: `AuthService`
- Controller: `AuthController`
- Seeder: `RoleSeeder` (creates default roles on startup)
