package com.checkmarxtest.vulnerableapp.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/api/files")
public class FileController {

    // Path Traversal Vulnerability
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) {
        File file = new File("/var/uploads/" + filename);
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok().body(resource);
    }

    // Unrestricted File Upload
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "/var/uploads/";
            File dest = new File(uploadDir + file.getOriginalFilename());
            file.transferTo(dest);
            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            return "Upload failed: " + e.getMessage();
        }
    }

    // XXE(XML Eternal Entity) Vulnerability Risk
    @PostMapping("/parse-xml")
    public String parseXml(@RequestBody String xmlContent) {
        try {
            javax.xml.parsers.DocumentBuilderFactory factory =
                    javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(
                    new java.io.ByteArrayInputStream(xmlContent.getBytes())
            );
            return "XML parsed successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
