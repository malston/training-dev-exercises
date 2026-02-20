Run a security audit of the auth module using parallel subagent delegation.

Use three subagents to analyze different aspects of `backend/src/main/java/com/example/tasktracker/security/`:

**Subagent 1: Password Security**
Analyze `AuthService.java` for password storage issues:

- Hashing algorithm strength (SHA-256 vs bcrypt/Argon2)
- Salt generation and usage
- Timing attack vulnerability in password comparison

**Subagent 2: Session Management**
Analyze `AuthService.java` and `AuthController.java` for session issues:

- Token generation entropy
- Session expiration (or lack thereof)
- Token storage security (in-memory vs persistent)
- Logout completeness

**Subagent 3: Input Validation and Error Handling**
Analyze `AuthController.java` for input handling issues:

- Missing input validation on register/login
- Information leakage in error messages
- Missing rate limiting
- Authorization header parsing safety

After all three subagents complete, consolidate their findings into a single report organized by severity (Critical, High, Medium, Low).
