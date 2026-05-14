package com.example.file.controller;

import com.example.file.service.FileService;
import com.example.file.service.TokenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private TokenValidationService tokenValidationService;

    // Файл upload
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "Authorization", required = false) String token) {

        // SOAP Token шалгах
        if (token == null || !tokenValidationService.validateToken(token)) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Invalid or missing token"
            ));
        }

        // Файл хоосон эсэхийг шалгах
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "File is empty"
            ));
        }

        try {
            String fileUrl = fileService.uploadFile(file);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "url", fileUrl,
                "fileName", file.getOriginalFilename(),
                "size", file.getSize()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Upload failed: " + e.getMessage()
            ));
        }
    }

    // Файл устгах
    @DeleteMapping("/{fileName}")
    public ResponseEntity<?> deleteFile(
            @PathVariable String fileName,
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || !tokenValidationService.validateToken(token)) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Invalid or missing token"
            ));
        }

        try {
            fileService.deleteFile(fileName);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "File deleted"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Delete failed: " + e.getMessage()
            ));
        }
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "File Manager Service is running"));
    }
}
