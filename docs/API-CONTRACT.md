# ROAD.NET — Shared Foundation API Contract

> Reference for the 4 team tracks. This document describes **only** the
> endpoints built in Step 0 (shared foundation). Portal-specific endpoints
> (Individual / Provider-Org / Admin) will be added on top of this base in
> their own tracks and documented separately.

- Base URL (backend): `http://localhost:8080`
- Frontend dev server proxies `/api/**` to the backend, so via the frontend
  use `http://localhost:5173/api/**`
- Auth: stateless JWT. Send `Authorization: Bearer <token>` on protected routes.
- All DTOs, never raw entities. `passwordHash` is **never** exposed.
- CORS restricted to `http://localhost:5173`.

---

## Health

### `GET /api/health`
Public (no auth).

**Response `200`**
```json
{ "status": "ok" }
```

---

## Authentication

### `POST /api/auth/register`
Public (no auth). Creates a user (enforces age >= 18).

**Request body**
```json
{
  "email": "user@example.com",
  "password": "Password123!",
  "displayName": "Jane Doe",
  "dob": "1995-05-20",
  "gender": "FEMALE",
  "country": "Rwanda",
  "city": "Kigali",
  "languages": ["English", "Kinyarwanda"],
  "maritalStatus": "SINGLE",
  "profession": "Engineer",
  "bio": "Hello world"
}
```
- `email` (required, must be valid)
- `password` (required, 8–100 chars)
- `displayName` (required, ≤120)
- `dob` (required, must imply age ≥ 18)
- `gender` (required): `MALE | FEMALE | NON_BINARY | UNDISCLOSED`
- `country`, `city` (optional)
- `languages` (optional, list of strings, ≤10)
- `maritalStatus` (optional): `SINGLE | MARRIED | DIVORCED | WIDOWED | SEPARATED | UNDISCLOSED`
- `profession` (optional, ≤150), `bio` (optional, ≤2000)

**Response `201`**
```json
{
  "token": "<jwt>",
  "user": {
    "id": 9,
    "email": "user@example.com",
    "displayName": "Jane Doe",
    "dob": "1995-05-20",
    "gender": "FEMALE",
    "country": "Rwanda",
    "city": "Kigali",
    "languages": ["English", "Kinyarwanda"],
    "maritalStatus": "SINGLE",
    "profession": "Engineer",
    "bio": "Hello world",
    "verified": false,
    "createdAt": "2026-09-01T05:52:05"
  }
}
```
> Note: no `passwordHash` field. Never serialize entities directly.

**Errors**
- `400` under 18: `"You must be at least 18 years old to register"`
- `400` validation failure (field errors object)
- `409` duplicate email: `"An account with this email already exists"`

---

### `POST /api/auth/login`
Public (no auth). Verifies credentials, returns JWT + user info.

**Request body**
```json
{ "email": "user@example.com", "password": "Password123!" }
```

**Response `200`** — same shape as register (token + user).

**Errors**
- `401` invalid credentials: `"Invalid email or password"`

---

## Current user (protected)

### `GET /api/me`
Protected (JWT required). Returns the authenticated user's info.

**Response `200`** — `UserResponse` shape (same as above, no password).

**Errors**
- `401` missing/invalid token:
  ```json
  { "status": 401, "error": "Unauthorized", "message": "Authentication is required" }
  ```

---

## Entities (persisted via JPA, `ddl-auto=update`)

| Entity | Table | Key fields |
|--------|-------|-----------|
| `User` | `users` | email (unique), passwordHash, displayName, dob, gender, country, city, languages, maritalStatus, profession, bio, verified, createdAt |
| `Preference` | `preferences` (+ `preference_intentions`, `interests`, `lifestyle`) | userId (unique), intentions[], geoScope, interests[], lifestyle[] |
| `IntroductionRequest` | `introduction_requests` | senderId, recipientId, message, status, createdAt |
| `Connection` | `connections` | connectionCode `RD-{year}-{4 hex}` (generated server-side), userAId, userBId, createdAt |

**Enums**
- `Intention`: `MARRIAGE, SERIOUS_RELATIONSHIP, DATING, FRIENDSHIP, COMPANIONSHIP, LONG_DISTANCE, DIASPORA_CONNECTION, CULTURAL_EXCHANGE, SHARED_EXPERIENCES`
- `GeoScope`: `LOCAL, NATIONAL, REGIONAL, DIASPORA, CROSS_BORDER, INTERCONTINENTAL, GLOBAL`
- `RequestStatus`: `PENDING, ACCEPTED, DECLINED, MAYBE_LATER`

---

## Seed data

On first startup the backend seeds **8 sample users** (login password: `SeedPass123!`):

| Email | Name | Country |
|-------|------|---------|
| amina.kane@example.com | Amina Kane | Senegal |
| david.okanlawon@example.com | David Okanlawon | Nigeria |
| sofia.moretti@example.com | Sofia Moretti | Italy |
| james.otieno@example.com | James Otieno | Kenya |
| mei.tanaka@example.com | Mei Tanaka | Japan |
| kwame.mensah@example.com | Kwame Mensah | Ghana |
| elena.silva@example.com | Elena Silva | Brazil |
| ravi.sharma@example.com | Ravi Sharma | India |

Seeding is skipped if users already exist (idempotent).

---

## Run commands

```bash
# Backend (PostgreSQL at localhost:5432/roadnet, user postgres / password postgres)
cd backend && mvn spring-boot:run     # starts on :8080

# Frontend
cd frontend && npm install && npm run dev   # starts on :5173
```

Backend DB config is in `backend/src/main/resources/application.properties`.
