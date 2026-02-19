# Game Backlog

REST API for managing a personal game backlog, integrated with the RAWG Video Games Database API.

# Live Demo

Live demo: [game-backlog-topaz.vercel.app](https://game-backlog-topaz.vercel.app)

![Backlog App Demo](demo.png)

## Frontend Repository

[game-backlog](https://github.com/rafaelcaple/game-backlog) — React frontend integrated with this API.

## Tech Stack

- Java 17
- Spring Boot 4
- Spring Data JPA
- Spring WebFlux (WebClient)
- H2 (in-memory database)
- Lombok

## Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/games` | List all saved games |
| GET | `/games/search?query=` | Search games via RAWG API |
| POST | `/games/save/{rawgId}` | Save a game from RAWG to the list |
| PATCH | `/games/{id}/status` | Update game status |
| DELETE | `/games/{id}` | Delete a game |

## Game Status

- `BACKLOG` - Games you plan to play
- `PLAYING` - Games you are currently playing
- `COMPLETED` - Games you have completed
- `DROPPED` - Games you stopped playing
  
## How to run

### Prerequisites
- Java 17+
- Maven
- RAWG API key (get one at [rawg.io](https://rawg.io/apidocs))

### Setup

1. Clone the repository
```bash
git clone https://github.com/rafaelcaple/game-backlog-backend.git
```

2. Set your RAWG API key as an environment variable
```bash
export RAWG_API_KEY=your_api_key_here
```

3. Run the application
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

The H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`)

## Future Implementations

- User authentication with Spring Security and JWT
- PostgreSQL database for persistent storage
- Individual game lists per user
- Game rating system
- Change to IGDB API for better search mechanic

