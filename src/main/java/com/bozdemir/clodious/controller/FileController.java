package com.bozdemir.clodious.controller;

import com.bozdemir.clodious.model.FileMeta;
import com.bozdemir.clodious.payload.FileResponse;
import com.bozdemir.clodious.service.FileService;
import com.bozdemir.clodious.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class FileController {
    @Autowired
    private FileService fileService;
    String path = "C:\\cloudi-users\\";
    @Autowired
    private UserService userService;

    @PostMapping("/{username}/upload")
    public ResponseEntity<String> upload(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dosya seçilmedi.");
        }
        try {
            File uploadedFile = fileService.uploadFile(username, file, path);
            return ResponseEntity.ok("Dosya başarıyla yüklendi: " + uploadedFile.getAbsolutePath());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dosya yüklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @GetMapping("/{username}/file/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String username, @PathVariable String fileName) throws MalformedURLException {
        Resource resource = fileService.downloadFile(username, fileName, path);
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{username}/files")
    public ResponseEntity<?> list(@PathVariable String username) throws IOException {
        List<FileResponse> fileMetaList = null;
        try {
            fileMetaList = fileService.listFiles(username, path);
        } catch (IOException e) {
        }
        return ResponseEntity
                .ok()
                .body(fileMetaList);
    }

    @DeleteMapping("/{username}/file/{fileName}")
    public ResponseEntity<?> delete(@PathVariable String username, @PathVariable String fileName) {
        boolean result = false;
        try {
            result = fileService.deleteFile(username, fileName, path);
            return ResponseEntity
                    .ok()
                    .body(result);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(result);
        }
    }

}
