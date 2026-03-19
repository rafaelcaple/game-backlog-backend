# Game Backlog - Backend

REST API for managing a personal game backlog. Uses [IGDB](https://api-docs.igdb.com/) as the default game data provider, with built-in support for switching to [RAWG](https://rawg.io/apidocs).

### [Frontend Repository](https://github.com/rafaelcaple/game-backlog)

## Tech Stack

- Java 17
- Spring Boot
- Spring Security + JWT (authentication & authorization)
- Spring Data JPA
- Spring WebFlux (WebClient for IGDB and RAWG API integration)
- PostgreSQL
- Docker

## Features

- JWT-based authentication (register & login)
- Search games by name via IGDB (or RAWG)
- Save games to a personal backlog
- Track and update game status (Backlog, Playing, Completed, Dropped)
- Per-user data isolation - each user only sees their own games
- Pluggable games provider - switch between IGDB and RAWG via config

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
| `GET` | `/games/search?query=` | Search games via IGDB API |
| `POST` | `/games/save/{gameId}` | Save a game from IGDB to the user's backlog |
| `PATCH` | `/games/{id}/status?status=` | Update the status of a game |
| `DELETE` | `/games/{id}` | Remove a game from the backlog |

## Game Status

| Status | Description |
|--------|-------------|
| `BACKLOG` | Games you plan to play |
| `PLAYING` | Games you are currently playing |
| `COMPLETED` | Games you have completed |
| `DROPPED` | Games you stopped playing |

## Games Provider

The provider used to search and fetch game data is configurable. Set `game.provider` in `application.properties` (or the `GAME_PROVIDER` env var) to one of the supported values:

| Value | Provider | Credentials needed |
|-------|----------|--------------------|
| `IGDB` | [IGDB API](https://api-docs.igdb.com/) (default) | `IGDB_CLIENT_ID` + `IGDB_CLIENT_SECRET` |
| `RAWG` | [RAWG Video Games Database](https://rawg.io/apidocs) | `RAWG_API_KEY` |

```properties
# application.properties
game.provider=IGDB   # change to RAWG to switch providers
```

Only the credentials for the active provider need to be set.

## How to Run

### Prerequisites

- Java 17+
- Maven
- PostgreSQL instance
- IGDB credentials (default) - register a Twitch app at [dev.twitch.tv](https://dev.twitch.tv/console) to obtain a Client ID and Client Secret
- **or** RAWG API key - get one at [rawg.io/apidocs](https://rawg.io/apidocs) if using the RAWG provider

### Environment Variables

| Variable | Description |
|----------|-------------|
| `IGDB_CLIENT_ID` | Your Twitch / IGDB client ID *(IGDB provider)* |
| `IGDB_CLIENT_SECRET` | Your Twitch / IGDB client secret *(IGDB provider)* |
| `RAWG_API_KEY` | Your RAWG API key *(RAWG provider)* |
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
export IGDB_CLIENT_ID=your_igdb_client_id
export IGDB_CLIENT_SECRET=your_igdb_client_secret
# export RAWG_API_KEY=your_rawg_key  # only if using RAWG provider
export JWT_SECRET=your_super_secret_key
export PGHOST=localhost
export PGPORT=5432
export PGDATABASE=gamebacklog
export PGUSER=postgres
export PGPASSWORD=your_db_password

# Windows (PowerShell)
$env:IGDB_CLIENT_ID="your_igdb_client_id"
$env:IGDB_CLIENT_SECRET="your_igdb_client_secret"
# $env:RAWG_API_KEY="your_rawg_key"  # only if using RAWG provider
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

> If using the RAWG provider, add `-e RAWG_API_KEY=your_rawg_key` and remove the IGDB variables.

```bash
docker run -p 8080:8080 \
  -e IGDB_CLIENT_ID=your_igdb_client_id \
  -e IGDB_CLIENT_SECRET=your_igdb_client_secret \
  -e JWT_SECRET=your_super_secret_key \
  -e PGHOST=your_db_host \
  -e PGPORT=5432 \
  -e PGDATABASE=gamebacklog \
  -e PGUSER=postgres \
  -e PGPASSWORD=your_db_password \
  game-backlog-backend
```
