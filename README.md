# Training: Developer Exercises

Exercise materials for the [Developer Learning Path](https://malston.github.io/claude-code-wiki/training/developer-path/). Each module has hands-on exercises you complete using Claude Code.

## Project Structure

```
training-dev-exercises/
├── backend/          # Java/Spring Boot REST API (task tracker)
├── frontend/         # TypeScript/Next.js frontend
├── exercises/        # Exercise instructions per module
└── CLAUDE.md         # Project conventions for Claude Code
```

## Prerequisites

- Java 21+
- Maven 3.9+
- Node.js 20+
- npm 10+
- Claude Code CLI

## Getting Started

**Backend:**

```bash
cd backend
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`.

**Frontend:**

```bash
cd frontend
npm install
npm run dev
```

The frontend starts at `http://localhost:3000`.

## Exercises by Module

| Module                     | Directory                    | Focus                                |
| -------------------------- | ---------------------------- | ------------------------------------ |
| 1. Prompting Foundations   | `exercises/01-prompting/`    | Writing specific, actionable prompts |
| 2. Verification Loop       | `exercises/02-verification/` | Building feedback into every task    |
| 3. Test-Driven Development | `exercises/03-tdd/`          | Using tests as requirements          |
| 4. Debugging               | `exercises/04-debugging/`    | Systematic troubleshooting           |
| 5. Context Management      | `exercises/05-context/`      | Managing what Claude sees            |
| 6. Extensions              | `exercises/06-extensions/`   | Subagents, skills, MCP servers       |

Each exercise directory has a `README.md` with instructions and acceptance criteria.
