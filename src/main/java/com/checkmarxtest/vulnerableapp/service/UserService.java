package com.checkmarxtest.vulnerableapp.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

@Service
public class UserService {

    // Hardcoded Credentials Vulnerability
    private static final String DB_URL = "jdbc:h2:mem:testdb";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "Admin@123456";
    private static final String API_KEY = "sk_live_51H3xYz9876543210abcdef";
    private static final String SECRET_TOKEN = "MySecretToken12345";

    // SQL Injection - Direct Query Execution
    public String authenticateUser(String query) {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return "Login successful for user: " + rs.getString("username");
            }
            return "Login failed";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    // Weak Cryptography - Using MD5
    public String hashPassword(String password) {
        return DigestUtils.md5Hex(password);
    }

    // Path Traversal - No Input Validation
    public String readUserFile(String filePath) {
        try {
            File file = new File(filePath);
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    // Insecure Deserialization
    public String deserializeUsers(String serializedData) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(serializedData.getBytes());
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            return "Users imported: " + obj.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Database Connection with Hardcoded Credentials
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Information Disclosure - Detailed Error Messages
    public String getUserDetails(String userId) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE id = " + userId
            );
            ResultSet rs = ps.executeQuery();
            return rs.toString();
        } catch (SQLException e) {
            // Exposing stack trace
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }
}
