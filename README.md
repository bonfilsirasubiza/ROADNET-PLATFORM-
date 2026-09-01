# ROAD.NET

ROAD.NET human-connection platform — shared foundation.

This repository contains the **shared foundation** (Step 0) that all three
team tracks branch from. No portal-specific features are here yet.

## Repo layout

```
roadnet/
├── backend/    Spring Boot 3.4 (Java 17 target), Maven — API on :8080
├── frontend/   React 18 + Vite + Tailwind — dev server on :5173
└── docs/       API contract: docs/API-CONTRACT.md
```

## Workspace setup

Prerequisites: Java 17+ (JDK 21 works), Maven, Node 18+, PostgreSQL running.

1. **PostgreSQL** — database `roadnet` on `localhost:5432`
   - Create once if missing:
     `createdb -U postgres roadnet`
   - Default dev credentials (in `backend/src/main/resources/application.properties`):
     user `postgres`, password `postgres`.
   - Inspect data with `psql`, pgAdmin, or DBeaver:
     `psql -U postgres -d roadnet`

2. **Backend** — starts Spring Boot on :8080, creates tables (`ddl-auto=update`)
   and seeds 8 demo users on first run.
   ```bash
   cd backend
   mvn spring-boot:run
   ```

3. **Frontend** — Vite dev server on :5173, proxies `/api` to the backend.
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

4. Verify: open `http://localhost:5173` — the landing page calls
   `/api/health` and shows the result.

## Seed users

All sample users log in with password **`SeedPass123!`** (see
`docs/API-CONTRACT.md` for the full list, e.g. `amina.kane@example.com`).

## Branching

Team tracks branch from `develop`:
- `feature/individual-portal`
- `feature/provider-org-portal`
- `feature/admin-shared`

See `docs/API-CONTRACT.md` for the exact API shapes to build against.
