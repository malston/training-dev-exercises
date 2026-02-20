# Task Tracker

A full-stack task tracking application used for Claude Code training exercises.

## Commands

- `cd backend && ./mvnw spring-boot:run` -- start the Spring Boot API on port 8080
- `cd backend && ./mvnw test` -- run backend tests
- `cd frontend && npm run dev` -- start the Next.js dev server on port 3000
- `cd frontend && npm test` -- run frontend tests
- `cd frontend && npm run build` -- production build

## Project Conventions

### Backend (Java/Spring Boot)

- REST endpoints under `src/main/java/com/example/tasktracker/controller/`
- Service layer under `src/main/java/com/example/tasktracker/service/`
- JPA entities under `src/main/java/com/example/tasktracker/model/`
- Tests mirror the source structure under `src/test/java/`
- Use constructor injection, not field injection
- Return `ResponseEntity` from controller methods
- H2 in-memory database for development

### Frontend (TypeScript/Next.js)

- App router with pages under `src/app/`
- API calls through `src/lib/api.ts`
- Components under `src/components/`
- Use TypeScript strict mode
- Tailwind CSS for styling

## Design Decisions

<!-- Record architectural decisions here during exercises -->
