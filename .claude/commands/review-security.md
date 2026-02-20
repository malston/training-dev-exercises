Review the authentication and security code in this project for vulnerabilities.

Read these files:

- backend/src/main/java/com/example/tasktracker/security/AuthService.java
- backend/src/main/java/com/example/tasktracker/security/AuthController.java

Analyze for:

1. Password storage -- is the hashing algorithm appropriate? Are salts used correctly?
2. Session management -- are tokens generated securely? Can sessions be hijacked?
3. Input validation -- are usernames and passwords validated before use?
4. Error handling -- do error messages leak information to attackers?
5. Missing features -- what security features would a production system need?

Report your findings as a prioritized list with severity (Critical, High, Medium, Low) and a recommended fix for each issue.
