# Personal Library Platform

Personal Library Platform is a full‑stack application for discovering books and managing your personal library. It combines an Angular web client with Spring Boot microservices (Auth and Books), backed by PostgreSQL and proxied by Nginx for local development via Docker. Users can register and authenticate, browse and search books, see popular items, and maintain their own library with reading statuses.

## Stack
- Frontend: Angular (served on port 4200; proxied as / via Nginx)
- Backend services (Spring Boot):
  - Auth service (served on port 8080; proxied as /auth/ via Nginx)
  - Books service (served on port 9090; proxied as /books/ via Nginx)
- Database: PostgreSQL (two instances for auth and books; ports 5435 and 5436 mapped to 5432 inside containers)
- Reverse proxy: Nginx (port 80 → proxies frontend and backends)
- Containerization: Docker & Docker Compose
- Cloud storage: Google Cloud Storage (for media/assets; controlled via GCP_* vars). Note: GCP environment variables are required and valid GCP credentials must be provided to start the application.

## Functionality
The platform provides a streamlined experience for exploring books and organizing a personal library:

1. User Management:
   - New user registration.
   - Authentication for existing users.
   - Profile customization.

2. Books Interaction:
   - Viewing popular books on the main page of the web application.
   - Getting all books or by letter filtering.
   - Searching books by title or author.
   - Managing own library:
     - adding books in system in own library by status (To Read, Read, Reading, Favorite),
     - getting all user’s books or by status filter,
     - updating status,
     - deleting book from own library.
   - CRUD operations to books.
   - Obtaining detailed information about a specific book.

Notes
- Authentication/authorization is handled by the Auth service (OpenID Connect/JWT). Configure ISSUER_URI, JWK_SET_URI, CLIENT_ID, CLIENT_SECRET, and REDIRECT_URI.
- The Books service can seed data from the Gutendex public API; configure BOOKS_API_SEEDING_URL.
- Some features depend on environment configuration (e.g., GCP for media) and may be limited in local dev.

## Repository structure
- auth/ — Spring Boot Auth service sources
- books/ — Spring Boot Books service sources
- frontend/ — Angular application sources
- dockerfiles/ — Dockerfiles for services (auth, books, frontend, etc.)
- nginx/ — Nginx config (default.conf)
- docker-compose.yaml — Services orchestration for local development
- env.example — Example environment variables for Compose
- ui-tests/ — Functional or UI test scaffolding (if applicable)

## Prerequisites
- Docker 20+ and Docker Compose v2
- (Optional) Node.js 18+ if you want to run the frontend outside Docker
- (Optional) Java 21+ and Maven if you want to run Spring Boot services outside Docker

## Quick start (Docker)
1. Create an .env file at the repository root based on env.example:
   cp env.example .env
   Then edit .env values as needed. At minimum, set:
   - DB_USER, DB_PASSWORD, DB_AUTH_DATABASE, DB_BOOKS_DATABASE
   - DATASOURCE_AUTH_URL, DATASOURCE_BOOKS_URL
   - SPRING_JPA_HIBERNATE_DDL_AUTO, SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT
   - CLIENT_ID, CLIENT_SECRET, REDIRECT_URI
   - ISSUER_URI and JWK_SET_URI (Books service JWT validation)
   - FRONTEND_URL_* (defaults in env.example are suitable for local dev)
   - GCP_* (REQUIRED). Valid Google Cloud credentials are needed to start the application. Set GCP_CREDENTIALS_PATH to a mounted service account JSON key and GCP_BUCKET_NAME to your bucket.
   - BOOKS_API_SEEDING_URL (defaults to Gutendex)

2. Build and start the stack:
   docker compose up -d

Note: If you plan to run Auth or Books outside Docker or need their JARs locally, build them with Maven first:
   cd auth && mvn -DskipTests clean package
   cd ../books && mvn -DskipTests clean package

3. Access the app:
   - Nginx entrypoint: http://localhost (proxies frontend and backend)
   - Frontend (direct): http://localhost:4200 (also via Nginx at http://localhost/)
   - Auth API (direct): http://localhost:8080/auth/ (also via Nginx at http://localhost/auth/)
   - Books API (direct): http://localhost:9090/ (also via Nginx at http://localhost/books/)
   - PostgreSQL (auth): localhost:5435 (container name: libdb-auth)
   - PostgreSQL (books): localhost:5436 (container name: libdb-books)

To stop:
   docker compose down

## How it works (local dev via Docker)
- Nginx listens on port 80 and proxies
  - / -> frontend:4200
  - /auth/ -> auth:8080/auth/
  - /books/ -> books:9090/
- The frontend service mounts the local frontend/ directory as a volume for live code iteration (Angular dev server with HMR).
- The backend services connect to their respective Postgres containers using DB_* and DATASOURCE_* variables from .env. In Docker, JDBC URLs should reference pg-auth and pg-books (the Compose service names) as in env.example.
- Auth issues tokens; Books validates JWT via ISSUER_URI and JWK_SET_URI.

## Seeding
- The Books service can seed initial data (popular books/genres) from Gutendex on startup.
- Control via environment variables:
  - BOOKS_API_SEEDING_URL: the source endpoint for seeding (default: https://gutendex.com/books/).
- Seeding should be idempotent (only inserts missing items). If you rerun containers, it won’t duplicate existing data.
- You can disable or change seeding by unsetting or modifying BOOKS_API_SEEDING_URL in .env and rebuilding.

## Environment variables
See env.example. Key variables:
- DB_USER, DB_PASSWORD: credentials for both Postgres instances.
- DB_AUTH_DATABASE, DB_BOOKS_DATABASE: database names.
- DATASOURCE_AUTH_URL, DATASOURCE_BOOKS_URL: JDBC URLs (inside Docker use pg-auth and pg-books hostnames as provided).
- SPRING_JPA_HIBERNATE_DDL_AUTO, SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT: JPA/Hibernate configuration.
- ISSUER_URI: OIDC issuer used by both services.
- JWK_SET_URI: JWKS endpoint for JWT validation (used by Books service).
- CLIENT_ID, CLIENT_SECRET, REDIRECT_URI: OAuth2 client for the frontend.
- FRONTEND_URL_PROXY, FRONTEND_URL_HOST, FRONTEND_URL_CONTAINER: CORS/redirect origins for proxy, host, and container addresses.
- GCP_CREDENTIALS_PATH, GCP_BUCKET_NAME: Google Cloud Storage configuration (REQUIRED). The application will not start without valid GCP credentials. Point GCP_CREDENTIALS_PATH to a readable service account JSON key (inside the container or host), and set GCP_BUCKET_NAME to an existing bucket the service account can access.
- BOOKS_API_SEEDING_URL: external API endpoint for initial books/genres seeding.

Copy env.example to .env at the repository root; docker-compose.yaml references these values.

## Development tips
- Frontend runs with: npm run start -- --host 0.0.0.0 (exposed on 4200; mounted volume for live reload).
- Auth and Books services are Spring Boot 3.x (Java 21); you can run them locally with Maven if you set corresponding environment variables.
- To build backend JARs (Auth and Books) with Maven, run the following in each module directory:
  - cd auth && mvn -DskipTests clean package
  - cd ../books && mvn -DskipTests clean package
  This produces target/*.jar which you can run locally or use for image builds.
- Database data is persisted in the pg-data-auth and pg-data-books Docker volumes.
- When changing .env values, recreate containers to apply changes: docker compose down && docker compose up -d

## Troubleshooting
- GCP credentials missing: Ensure GCP_CREDENTIALS_PATH points to a valid service account JSON and the file is mounted/readable by containers; also set GCP_BUCKET_NAME. Without these, services depending on GCS will fail to start.
- Port conflicts: Ensure ports 80, 4200, 8080, 9090, 5435, 5436 are free or adjust mappings in docker-compose.yaml.
- ENV hasn't been applied: Confirm you created .env at repo root and values are present. Recreate containers after changes.
- DB connection errors: In Docker, JDBC hosts must be pg-auth and pg-books. Check DATASOURCE_* and DB_* values and that the pg-* services are healthy.
- Proxy: Use Nginx at http://localhost to exercise both frontend and backend together; direct service ports also available for debugging.
- Auth/redirect issues: Ensure FRONTEND_URL_* and REDIRECT_URI match your actual dev URLs; tokens are validated in the Books service using ISSUER_URI/JWK_SET_URI.
