package com.bozdemir.clodious.controller;

import com.bozdemir.clodious.model.FileData;
import com.bozdemir.clodious.payload.response.FileResponse;
import com.bozdemir.clodious.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    @Autowired
    private FileService fileService;
    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> upload(MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (file.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("File not selected.");
        }
        boolean success = fileService.uploadFile(username, file);
        if (success) {
            logger.info("POST upload(MultipartFile file) | File uploaded by {}", username);
            return ResponseEntity
                    .ok("File successfully uploaded.");
        } else {
            logger.error("POST upload(MultipartFile file) | File upload failed for {}", username);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while uploading the file.");
        }
    }

    @GetMapping("/download")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> download(UUID fileId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        FileData fileData = fileService.getFile(username, fileId);
        if (fileData != null) {
            logger.info("GET download(UUID fileId) | File downloaded by {}", username);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getName() + "\"")
                    .body(fileData.getData());
        } else {
            logger.error("GET download(UUID fileId) | File not found: {} for {}", fileId, username);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("File not found or unauthorized access.");
        }
    }

    @GetMapping("/files")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FileResponse>> list() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<FileResponse> fileResponses = fileService.listFiles(username);
        if (!fileResponses.isEmpty()) {
            logger.info("GET list() | Files listed by {}", username);
            return ResponseEntity.ok(fileResponses);
        } else {
            logger.info("GET list() | Files not listed by {}", username);
        }
        return ResponseEntity
                .ok(fileResponses);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> delete(UUID fileId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean success = fileService.deleteFile(username, fileId);
        if (success) {
            logger.info("GET delete() | File deleted by {}", username);
            return ResponseEntity
                    .ok("File deleted successfully.");
        } else {
            logger.error("GET delete() | File deletion failed: {} by {}>", fileId, username);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("File not found or unauthorized access.");
        }
    }
}
