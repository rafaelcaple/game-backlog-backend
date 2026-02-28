# Game Backlog - Backend

REST API for managing a personal game backlog, integrated with the [RAWG Video Games Database API](https://rawg.io/apidocs).

### [Live Demo](https://game-backlog-topaz.vercel.app/) 
### [Frontend Repository](https://github.com/rafaelcaple/game-backlog)

## Tech Stack

- Java 17
- Spring Boot
- Spring Security + JWT (authentication & authorization)
- Spring Data JPA
- Spring WebFlux (WebClient for RAWG API integration)
- PostgreSQL
- Docker

## Endpoints

### Auth (public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/register` | Register a new user and receive a JWT token |
| `POST` | `/auth/login` | Authenticate and receive a JWT token |

**Request body (both):**
```json
{
  "username": "your_username",
  "password": "your_password"
}
```


---

### Games (requires authentication)

All game routes require the `Authorization: Bearer <token>` header.

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/games` | List all games saved by the authenticated user |
| `GET` | `/games/search?query=` | Search games via RAWG API |
| `POST` | `/games/save/{rawgId}` | Save a game from RAWG to the user's backlog |
| `PATCH` | `/games/{id}/status?status=` | Update the status of a game |
| `DELETE` | `/games/{id}` | Remove a game from the backlog |

## Game Status

| Status | Description |
|--------|-------------|
| `BACKLOG` | Games you plan to play |
| `PLAYING` | Games you are currently playing |
| `COMPLETED` | Games you have completed |
| `DROPPED` | Games you stopped playing |

## How to Run

### Prerequisites

- Java 17+
- Maven
- PostgreSQL instance
- RAWG API key — get one at [rawg.io/apidocs](https://rawg.io/apidocs)

### Environment Variables

| Variable | Description |
|----------|-------------|
| `RAWG_API_KEY` | Your RAWG API key |
| `JWT_SECRET` | Secret key used to sign JWT tokens |
| `PGHOST` | PostgreSQL host |
| `PGPORT` | PostgreSQL port |
| `PGDATABASE` | Database name |
| `PGUSER` | Database user |
| `PGPASSWORD` | Database password |

### Running locally

**1. Clone the repository**

```bash
git clone https://github.com/rafaelcaple/game-backlog-backend
cd game-backlog-backend
```

**2. Set environment variables**

```bash
# Linux/macOS
export RAWG_API_KEY=your_rawg_key
export JWT_SECRET=your_super_secret_key
export PGHOST=localhost
export PGPORT=5432
export PGDATABASE=gamebacklog
export PGUSER=postgres
export PGPASSWORD=your_db_password

# Windows (PowerShell)
$env:RAWG_API_KEY="your_rawg_key"
$env:JWT_SECRET="your_super_secret_key"
$env:PGHOST="localhost"
$env:PGPORT="5432"
$env:PGDATABASE="gamebacklog"
$env:PGUSER="postgres"
$env:PGPASSWORD="your_db_password"
```

**3. Run the application**

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### Running with Docker

**1. Build the image**

```bash
docker build -t game-backlog-backend .
```

**2. Run the container**

```bash
docker run -p 8080:8080 \
  -e RAWG_API_KEY=your_rawg_key \
  -e JWT_SECRET=your_super_secret_key \
  -e PGHOST=your_db_host \
  -e PGPORT=5432 \
  -e PGDATABASE=gamebacklog \
  -e PGUSER=postgres \
  -e PGPASSWORD=your_db_password \
  game-backlog-backend
```
