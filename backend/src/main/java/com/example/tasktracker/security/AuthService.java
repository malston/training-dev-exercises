package com.example.tasktracker.security;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final Map<String, UserCredentials> users = new ConcurrentHashMap<>();
    private final Map<String, String> activeSessions = new ConcurrentHashMap<>();

    public String register(String username, String password) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        users.put(username, new UserCredentials(hashedPassword, salt));
        return "User registered";
    }

    public String login(String username, String password) {
        UserCredentials creds = users.get(username);
        if (creds == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String hashedAttempt = hashPassword(password, creds.salt());
        if (!hashedAttempt.equals(creds.hashedPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String sessionToken = generateSessionToken();
        activeSessions.put(sessionToken, username);
        return sessionToken;
    }

    public String validateSession(String token) {
        String username = activeSessions.get(token);
        if (username == null) {
            throw new IllegalArgumentException("Invalid session");
        }
        return username;
    }

    public void logout(String token) {
        activeSessions.remove(token);
    }

    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String generateSessionToken() {
        byte[] token = new byte[32];
        new SecureRandom().nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private record UserCredentials(String hashedPassword, String salt) {}
}
