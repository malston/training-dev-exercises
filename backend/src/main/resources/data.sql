INSERT INTO tasks (title, description, status, priority, created_at, updated_at) VALUES
('Set up CI pipeline', 'Configure GitHub Actions to run tests on every push', 'TODO', 'HIGH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Add user authentication', 'Implement JWT-based login and registration endpoints', 'TODO', 'HIGH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Bug #4: Status value doesn't match the TaskStatus enum (should be IN_PROGRESS, not PROGRESS)
('Write API documentation', 'Document all REST endpoints with request/response examples', 'PROGRESS', 'MEDIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Fix date formatting', 'Dates display in ISO format but users want MM/DD/YYYY', 'TODO', 'LOW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Add task search', 'Search tasks by title substring', 'TODO', 'MEDIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
