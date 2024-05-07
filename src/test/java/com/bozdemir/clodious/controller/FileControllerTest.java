package com.bozdemir.clodious.controller;
import com.bozdemir.clodious.payload.FileResponse;
import com.bozdemir.clodious.service.FileService;
import com.bozdemir.clodious.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.File;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;

class FileControllerTest {
    @Mock
    private FileService fileService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUpload() throws IOException {
        String username = "testUser";
        MultipartFile file = new MockMultipartFile("test.txt", "test.txt", "text/plain", "test content".getBytes());
        when(fileService.uploadFile(username, file, fileController.path)).thenReturn(new File("test.txt"));

        ResponseEntity<String> response = fileController.upload(username, file);
        String[] success = response.getBody().split(":");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Dosya başarıyla yüklendi", success[0]);
    }

    @Test
    void testDownload() throws IOException {
        String username = "testUser";
        String fileName = "test.txt";
        Resource resource = mock(Resource.class);
        when(resource.exists()).thenReturn(true);
        when(resource.isReadable()).thenReturn(true);
        when(fileService.downloadFile(username, fileName, fileController.path)).thenReturn(resource);

        ResponseEntity<Resource> response = fileController.download(username, fileName);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        Assertions.assertEquals(resource, response.getBody());
    }

    @Test
    void testList() throws IOException {
        String username = "testUser";
        List<FileResponse> fileMetaList = Collections.singletonList(new FileResponse("test.txt", "TEXT", "text/plain", 1L));
        when(fileService.listFiles(username, fileController.path)).thenReturn(fileMetaList);

        ResponseEntity<?> response = fileController.list(username);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(fileMetaList, response.getBody());
    }

    @Test
    void testDelete() throws IOException {
        String username = "testUser";
        String fileName = "test.txt";
        when(fileService.deleteFile(username, fileName, fileController.path)).thenReturn(true);

        ResponseEntity<?> response = fileController.delete(username, fileName);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(true, response.getBody());
    }
}
