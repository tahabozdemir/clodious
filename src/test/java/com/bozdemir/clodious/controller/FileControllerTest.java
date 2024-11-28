/*package com.bozdemir.clodious.controller;

import com.bozdemir.clodious.model.FileData;
import com.bozdemir.clodious.payload.response.FileResponse;
import com.bozdemir.clodious.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileControllerTest {
    @Mock
    private FileService fileService;

    @Mock
    private MultipartFile file;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void testUpload_Success() throws IOException {
        when(file.isEmpty()).thenReturn(false);
        when(fileService.uploadFile(anyString(), any(MultipartFile.class))).thenReturn(true);

        ResponseEntity<String> response = fileController.upload(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File successfully uploaded.", response.getBody());
    }

    @Test
    void testUpload_FileNotSelected() throws IOException {
        when(file.isEmpty()).thenReturn(true);
        ResponseEntity<String> response = fileController.upload(file);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("File not selected.", response.getBody());
    }

    @Test
    void testUpload_Failure() throws IOException {
        when(file.isEmpty()).thenReturn(false);
        when(fileService.uploadFile(anyString(), any(MultipartFile.class))).thenReturn(false);

        ResponseEntity<String> response = fileController.upload(file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while uploading the file.", response.getBody());
    }

    @Test
    void testDownload_Success() {
        UUID fileId = UUID.randomUUID();
        FileData fileData = new FileData();
        fileData.setName("testfile.txt");
        fileData.setData(new byte[]{1, 2, 3});

        when(fileService.getFile(anyString(), eq(fileId))).thenReturn(fileData);

        ResponseEntity<?> response = fileController.download(fileId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("attachment; filename=\"testfile.txt\"", response.getHeaders().getContentDisposition().toString());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertEquals(fileData.getData(), response.getBody());
    }

    @Test
    void testDownload_FileNotFound() {
        UUID fileId = UUID.randomUUID();

        when(fileService.getFile(anyString(), eq(fileId))).thenReturn(null);

        ResponseEntity<?> response = fileController.download(fileId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("File not found or unauthorized access.", response.getBody());
    }

    @Test
    void testListFiles() {
        List<FileResponse> fileResponses = List.of(new FileResponse("testfile.txt","TEXT",new Date(), UUID.randomUUID()));

        when(fileService.listFiles(anyString())).thenReturn(fileResponses);

        ResponseEntity<List<FileResponse>> response = fileController.list();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileResponses, response.getBody());
    }

    @Test
    void testDeleteFile_Success() {
        UUID fileId = UUID.randomUUID();

        when(fileService.deleteFile(anyString(), eq(fileId))).thenReturn(true);

        ResponseEntity<String> response = fileController.delete(fileId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File deleted successfully.", response.getBody());
    }

    @Test
    void testDeleteFile_FileNotFound() {
        UUID fileId = UUID.randomUUID();

        when(fileService.deleteFile(anyString(), eq(fileId))).thenReturn(false);

        ResponseEntity<String> response = fileController.delete(fileId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("File not found or unauthorized access.", response.getBody());
    }
}
*/
