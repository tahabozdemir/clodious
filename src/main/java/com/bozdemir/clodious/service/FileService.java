package com.bozdemir.clodious.service;

import cn.hutool.core.io.FileTypeUtil;
import com.bozdemir.clodious.DAO.FileDAO;
import com.bozdemir.clodious.DAO.UserDAO;
import com.bozdemir.clodious.model.FileMeta;
import com.bozdemir.clodious.model.User;
import com.bozdemir.clodious.payload.response.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    FileDAO fileDAO;
    @Autowired
    UserDAO userDAO;

    public File uploadFile(String username, MultipartFile file, String directoryPath) throws IOException {
        createFolder(directoryPath + username);
        File uploadedFile = new File(directoryPath + username + "//" + file.getOriginalFilename());
        Optional<User> user = userDAO.getUserByUsername(username);
        file.transferTo(uploadedFile);
        if (user.isPresent()) {
            FileMeta fileMeta = new FileMeta(file.getOriginalFilename(), FileTypeUtil.getType(uploadedFile), uploadedFile.getAbsolutePath(), user.get());
            fileDAO.saveOrUpdateFile(fileMeta);
        }
        return uploadedFile;
    }

    public Resource downloadFile(String username, String fileName, String directoryPath) throws MalformedURLException {
        Path path = Paths.get(directoryPath + username + File.separator + fileName);
        return new UrlResource(path.toUri());
    }

    public List<FileResponse> listFiles(String username, String directoryPath) throws IOException {
        List<FileMeta> fileMetaList = new ArrayList<>();
        List<FileResponse> fileResponses = new ArrayList<>();
        Optional<User> user = userDAO.getUserByUsername(username);
        Path path = Paths.get(directoryPath + username);
        if (user.isPresent()) {
            fileResponses = Files.list(path)
                    .map(fileMeta -> new FileResponse(
                            fileMeta.getFileName().toString(),
                            FileTypeUtil.getType(fileMeta.toFile()),
                            fileMeta.toUri().toString(),
                            user.get().getId()
                    ))
                    .toList();
        }
        return fileResponses;
    }

    public Boolean deleteFile(String username, String fileName, String directoryPath) throws IOException {
        Optional<User> user = userDAO.getUserByUsername(username);
        Boolean success = false;
        Path path = Paths.get(directoryPath + username + File.separator + fileName);
        Optional<FileMeta> fileMeta = fileDAO.getFileByName(fileName);
        if (fileMeta.isPresent()) {
            Files.delete(path);
            success = fileDAO.removeFile(fileMeta.get());
        }
        return success;
    }

    private void createFolder(String folderPath) {
        Path path = Paths.get(folderPath);
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
