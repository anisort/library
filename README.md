# Personal Library Platform

**Personal Library Platform** is a full-stack application for discovering books, managing your personal library, and interacting with an AI-powered assistant.
It combines an **Angular** web client with **Spring Boot microservices** (Auth, Books, and Assistant), backed by **PostgreSQL** databases and proxied by **Nginx** for local development via Docker.
Users can register, browse books, manage their own collections, and chat with an assistant that provides intelligent, context-aware recommendations.

---

## Stack

**Frontend:** Angular (served on port 4200; proxied as `/` via Nginx)

**Backend services (Spring Boot):**

* **Auth service** (port 8080; proxied as `/auth/` via Nginx)
* **Books service** (port 9090; proxied as `/books/` via Nginx)
* **Assistant service** (port 7070; proxied as `/assistant/` via Nginx)

**Database:** PostgreSQL (three instances)

* `pg-auth` → for user authentication data
* `pg-books` → for books, genres, and embeddings (pgvector enabled)
* `pg-assistant` → for assistant chat and context memory

**Reverse proxy:** Nginx (port 80 → proxies frontend and backends)

**Containerization:** Docker & Docker Compose

**Cloud storage and AI:** Google Cloud Platform (Vertex AI + GCS)

> GCP environment variables are required, and valid service account credentials must be provided to start the application.

---

## Functionality

### 👤 User Management

* User registration and authentication (JWT / OAuth2 / Google Login)
* Profile customization
* Token validation and role-based access control via the Auth service

### 📚 Books Interaction

* View popular books on the main page
* Filter books alphabetically or by author
* Search books by title, author, or genre
* Manage personal library:

    * Add books to personal collection by status (To Read, Reading, Read, Favorite)
    * Filter by reading status
    * Update or remove books
* Full CRUD operations for book entities
* Get detailed information about a specific book
* Automatic seeding of books from **Gutendex API**

### 🤖 AI Assistant

* Integrated **Assistant microservice** powered by **Google Vertex AI** and **Gemini**
* Features:

    * Conversational assistant for book recommendations and discovery
    * Multimodal prompt handling (text + images)
    * Retrieval-augmented generation (RAG) using **pgvector**
    * Persistent chat history (stored in `libdb-assistant`)
    * Cross-service embedding access (Assistant queries Books vector DB)
    * Contextual responses enhanced by metadata and book similarity

---

## Notes

* **Authentication / authorization** handled by Auth (OIDC, JWT).
  Configure `ISSUER_URI`, `JWK_SET_URI`, `CLIENT_ID`, `CLIENT_SECRET`, and `REDIRECT_URI`.
* **Books Service** supports automatic data seeding from Gutendex (set `BOOKS_API_SEEDING_URL`).
* **Assistant Service** requires valid GCP credentials for Vertex AI and Gemini access.
* Features like image upload or embeddings generation depend on GCP configuration.

---

## Repository structure

```
auth/          → Spring Boot Auth service sources
books/         → Spring Boot Books service sources
assistant/     → Spring Boot Assistant service sources
frontend/      → Angular application sources
dockerfiles/   → Dockerfiles for all services
nginx/         → Nginx config (default.conf)
ui-tests/      → Functional or UI test scaffolding
docker-compose.yaml → Orchestrates all services for local development
env.example    → Example environment variables for Compose
```

---

## Prerequisites

* Docker 20+ and Docker Compose v2
* (Optional) Node.js 18+ for standalone frontend
* (Optional) Java 21+ and Maven for standalone Spring Boot services
* (Required) Google Cloud service account key (JSON file)

---

## Quick start (Docker)

1. **Create `.env` file** at the repository root based on `env.example`:

   ```bash
   cp env.example .env
   ```

   Then edit `.env` values as needed.
   Minimum required:

    * DB_USER, DB_PASSWORD
    * DB_AUTH_DATABASE, DB_BOOKS_DATABASE, DB_ASSISTANT_DATABASE
    * DATASOURCE_AUTH_URL, DATASOURCE_BOOKS_URL, DATASOURCE_ASSISTANT_URL
    * CLIENT_ID, CLIENT_SECRET, REDIRECT_URI
    * ISSUER_URI, JWK_SET_URI
    * GCP_* variables (project ID, location, embedding & Gemini models, storage buckets, credentials)

2. **Build and start all services:**

   ```bash
   docker compose up -d
   ```

3. **Access the app:**

    * Nginx entrypoint: [http://localhost](http://localhost)
    * Frontend (direct): [http://localhost:4200](http://localhost:4200)
    * Auth API: [http://localhost/auth/](http://localhost/auth/)
    * Books API: [http://localhost/books/](http://localhost/books/)
    * Assistant API: [http://localhost/assistant/](http://localhost/assistant/)

4. **Stop containers:**

   ```bash
   docker compose down
   ```

---

## How it works (local dev via Docker)

* **Nginx** proxies:

    * `/` → Angular frontend
    * `/auth/` → Auth service (port 8080)
    * `/books/` → Books service (port 9090)
    * `/assistant/` → Assistant service (port 7070)

* **Frontend** mounts the local directory for live reload (HMR).

* **Auth** issues JWT tokens, **Books** validates them via `ISSUER_URI` and `JWK_SET_URI`.

* **Books service** connects to Postgres (`pg-books`) with **pgvector** enabled for semantic embeddings.

* **Assistant service** queries this same vector store to provide context-aware AI responses.

---

## Seeding

The Books service automatically seeds initial data (books and genres) from **Gutendex** on startup.

**Configuration:**

* `BOOKS_API_SEEDING_URL=https://gutendex.com/books`
* Data is idempotent — existing items are not duplicated.
* To disable seeding, remove or unset the variable in `.env`.

---

## Environment variables

See `env.example`.

### Key variables:

#### Databases

| Variable                                                            | Description          |
| ------------------------------------------------------------------- | -------------------- |
| DB_USER, DB_PASSWORD                                                | Database credentials |
| DB_AUTH_DATABASE, DB_BOOKS_DATABASE, DB_ASSISTANT_DATABASE          | Database names       |
| DATASOURCE_AUTH_URL, DATASOURCE_BOOKS_URL, DATASOURCE_ASSISTANT_URL | JDBC connection URLs |

#### Auth configuration

| Variable                                                      | Description               |
| ------------------------------------------------------------- | ------------------------- |
| ISSUER_URI, JWK_SET_URI                                       | JWT issuer and keys       |
| CLIENT_ID, CLIENT_SECRET, REDIRECT_URI                        | OAuth2 configuration      |
| FRONTEND_URL_PROXY, FRONTEND_URL_HOST, FRONTEND_URL_CONTAINER | CORS and redirect origins |

#### Google Cloud

| Variable                          | Description                                      |
| --------------------------------- | ------------------------------------------------ |
| GCP_PROJECT_ID, GCP_LOCATION      | GCP environment settings                         |
| GCP_EMBEDDING_MODEL               | Vertex AI embedding model (`text-embedding-004`) |
| GCP_GEMINI_MODEL                  | Gemini model for assistant (`gemini-2.0-flash`)  |
| GCP_STORAGE_BUCKET_NAME_BOOKS     | Storage bucket for book covers                   |
| GCP_STORAGE_BUCKET_NAME_ASSISTANT | Storage bucket for assistant data                |
| GOOGLE_APPLICATION_CREDENTIALS    | Path to mounted GCP service account JSON         |

#### Seeding

| Variable              | Description                        |
| --------------------- | ---------------------------------- |
| BOOKS_API_SEEDING_URL | Gutendex endpoint for initial data |

---

## Development tips

* **Frontend:**

  ```bash
  npm run start -- --host 0.0.0.0
  ```

  Exposed on port 4200 with live reload.

* **Backend:**
  Auth, Books, and Assistant are Spring Boot 3.x (Java 21).
  You can run each locally with Maven if `.env` variables are set:

  ```bash
  cd auth && mvn -DskipTests clean package
  cd ../books && mvn -DskipTests clean package
  cd ../assistant && mvn -DskipTests clean package
  ```

* **Data persistence:**
  Stored in Docker volumes:

    * `pg-data-auth`
    * `pg-data-books`
    * `pg-data-assistant`

* **Reapply .env changes:**

  ```bash
  docker compose down && docker compose up -d
  ```

---

## Troubleshooting

* **GCP credentials missing:**
  Ensure `GOOGLE_APPLICATION_CREDENTIALS` points to a valid JSON file and `GCP_STORAGE_BUCKET_NAME_*` are set.
  Without these, Assistant and Books will fail to start.

* **Database connection errors:**
  Verify hostnames in JDBC URLs use Docker service names (`pg-auth`, `pg-books`, `pg-assistant`).

* **Ports conflict:**
  Ensure ports 80, 4200, 7070, 8080, 9090, 5435–5437 are available.

* **Auth redirect errors:**
  Make sure `FRONTEND_URL_*` and `REDIRECT_URI` match your running URLs.

* **Embeddings not generated:**
  Verify `GCP_EMBEDDING_MODEL` and `GCP_PROJECT_ID` are correctly set, and that Vertex AI API is enabled.
