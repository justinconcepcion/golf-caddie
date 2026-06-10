# Golf Caddie

AI-powered golf caddie. Given your position on a hole, it infers your lie, computes distance to the pin, and asks Claude for per-shot club and strategy advice based on your bag.

Monorepo: `backend/` (Spring Boot) today, `frontend/` (Vue 3 SPA) in progress.

## Architecture

Layered Spring Boot backend (Controller → Service → Repository), one bounded package per domain:

| Domain | Responsibility |
| --- | --- |
| `advice` | Assembles shot context and proxies it to Claude via the `anthropic-java` SDK. API key stays server-side. |
| `course` | Read-only seeded course data (holes, tees, pins, typed lie polygons). |
| `profile` | The golfer's handedness and per-club carry distances. |
| `round` | Starts rounds and logs shots within them. |
| `geo` | Distance (haversine) and lie inference (JTS point-in-polygon). |

Cross-cutting: `shared` holds the `@RestControllerAdvice` global handler (`ResourceNotFoundException` → 404, `ConstraintViolationException`/validation → 400, fallback → 500).

## Tech stack

- Java 21, Spring Boot 3 (web, data-jpa, validation)
- H2 in-memory (`create-drop`) — swap to Postgres + Flyway once the loop is proven
- JTS `jts-core` for geometry
- `com.anthropic:anthropic-java` for advice
- Lombok, Maven wrapper

## Prerequisites

- JDK 21 (`JAVA_HOME` set; this repo is developed against Amazon Corretto 21)
- No global Maven needed — use the bundled `mvnw`

## Run locally

```bash
cd backend
# PowerShell: $env:JAVA_HOME = "T:\Java\jdk21.0.10_7"
export JAVA_HOME="T:\Java\jdk21.0.10_7"

./mvnw spring-boot:run
# or
./mvnw -DskipTests package && java -jar target/*.jar
```

App starts on **http://localhost:8080** (~4s). The H2 console is at `/h2-console` (JDBC URL `jdbc:h2:mem:golfcaddie`, user `sa`, no password).

The app builds and runs **without** an API key — only `POST /api/v1/advice` requires it.

## Configuration

| Property | Env var | Default | Notes |
| --- | --- | --- | --- |
| `golfcaddie.claude.api-key` | `ANTHROPIC_API_KEY` | _(empty)_ | Paid Anthropic API key from console.anthropic.com. Separate from any Claude.ai subscription. |
| `golfcaddie.claude.model` | — | `claude-haiku-4-5` | Haiku for cost; swap for a quality A/B test. |
| `golfcaddie.claude.max-tokens` | — | `512` | Output cap per advice call. |

Set the key (Windows, User scope), then relaunch:

```powershell
setx ANTHROPIC_API_KEY "sk-ant-..."
```

> Cost: Haiku is ~$1 / $5 per million input / output tokens. Each advice call is capped at 512 output tokens.

## API

Base path `/api/v1`. All bodies are JSON.

### Courses

```
GET /courses/{courseId}
```

Returns the course with its holes (par, yards, tee, pin, lie polygons). Seeded course: `pebble-creek`.

### Profile

```
GET /profile
PUT /profile
```

`PUT` body:

```json
{
  "handedness": "RIGHT",
  "clubDistancesYards": { "DRIVER": 250, "SEVEN_IRON": 150, "PITCHING_WEDGE": 110 }
}
```

`handedness` ∈ `RIGHT|LEFT`. Club keys are the `Club` enum (`DRIVER`, `THREE_WOOD`, … `PUTTER`).

### Rounds & shots

```
POST /rounds                 -> 201 { id, courseId, createdAt }
POST /rounds/{roundId}/shots -> 201 { id, holeNumber, shotNumber, lat, lng, lie, distanceToPinYards }
```

`POST /rounds` body: `{ "courseId": "pebble-creek" }`
`POST /rounds/{id}/shots` body: `{ "holeNumber": 1, "lat": 33.501, "lng": -111.900 }`

### Advice

```
POST /advice -> 200 { holeNumber, lie, distanceToPinYards, advice }
```

Body:

```json
{ "courseId": "pebble-creek", "holeNumber": 1, "lat": 33.501, "lng": -111.900, "shotNumber": 2 }
```

`lie` ∈ `TEE|FAIRWAY|ROUGH|BUNKER|GREEN|UNKNOWN`. `advice` is Claude's natural-language recommendation. `shotNumber` is optional.

## Roadmap

Tracked as GitHub issues. Near-term: backend test coverage, Vue frontend, Postgres + Flyway, request auth.

## License

Private project — not yet licensed for redistribution.
