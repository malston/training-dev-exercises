INSERT INTO tasks (title, description, status, priority, created_at, updated_at) VALUES
('Set up CI pipeline', 'Configure GitHub Actions to run tests on every push', 'TODO', 'HIGH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Add user authentication', 'Implement JWT-based login and registration endpoints', 'TODO', 'HIGH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Write API documentation', 'Document all REST endpoints with request/response examples', 'IN_PROGRESS', 'MEDIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Fix date formatting', 'Dates display in ISO format but users want MM/DD/YYYY', 'TODO', 'LOW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Add task search', 'Search tasks by title substring', 'TODO', 'MEDIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
