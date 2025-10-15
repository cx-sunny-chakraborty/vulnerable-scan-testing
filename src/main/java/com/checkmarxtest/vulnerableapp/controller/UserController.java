package com.checkmarxtest.vulnerableapp.controller;

import com.checkmarxtest.vulnerableapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // SQL Injection Vulnerability
    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        String query = "SELECT * FROM users WHERE username='" + username +
                "' AND password='" + password + "'";
        return userService.authenticateUser(query);
    }

    // XSS Vulnerability
    @GetMapping("/search")
    public String searchUsers(@RequestParam String searchTerm) {
        return "<html><body><h1>Search Results for: " + searchTerm + "</h1></body></html>";
    }

    // Command Injection Vulnerability
    @GetMapping("/ping")
    public String pingServer(@RequestParam String host) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("ping -c 4 " + host);
            return "Pinging " + host;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Path Traversal Vulnerability
    @GetMapping("/profile")
    public String getUserProfile(@RequestParam String userId) {
        return userService.readUserFile("/users/" + userId + ".txt");
    }

    // Insecure Deserialization Risk
    @PostMapping("/import")
    public String importUsers(@RequestBody String serializedData) {
        return userService.deserializeUsers(serializedData);
    }
}
