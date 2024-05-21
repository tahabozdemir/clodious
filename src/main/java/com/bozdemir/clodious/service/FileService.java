package com.bozdemir.clodious.service;

import cn.hutool.core.io.FileTypeUtil;
import com.bozdemir.clodious.DAO.FileDAO;
import com.bozdemir.clodious.DAO.UserDAO;
import com.bozdemir.clodious.model.FileData;
import com.bozdemir.clodious.model.User;
import com.bozdemir.clodious.payload.response.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    private FileDAO fileDAO;
    @Autowired
    private UserDAO userDAO;

    public Boolean uploadFile(String username, MultipartFile file) throws IOException {
        Optional<User> user = userDAO.getUserByUsername(username);
        if (user.isPresent()) {
            byte[] fileDataBytes = file.getBytes();
            String fileType = FileTypeUtil.getType(file.getInputStream());
            FileData fileData = new FileData(file.getOriginalFilename(), fileType, user.get(), fileDataBytes);
            return fileDAO.saveOrUpdateFile(fileData);
        } else {
            return false;
        }
    }

    public FileData getFile(String username, UUID fileId) {
        Optional<FileData> optionalFileData = fileDAO.getFileById(fileId);
        if (optionalFileData.isPresent() && optionalFileData.get().getUser().getUsername().equals(username)) {
            return optionalFileData.get();
        }
        return null;
    }

    public List<FileResponse> listFiles(String username) {
        List<FileResponse> fileResponses = new ArrayList<>();
        Optional<User> user = userDAO.getUserByUsername(username);
        if (user.isPresent()) {
            List<FileData> userFiles = fileDAO.getAllFilesByUser(user.get());
            for (FileData fileData : userFiles) {
                fileResponses.add(new FileResponse(
                        fileData.getName(),
                        fileData.getType(),
                        fileData.getCreationDate(),
                        fileData.getId()
                ));
            }
        }
        return fileResponses;
    }

    public boolean deleteFile(String username, UUID fileId) {
        Optional<FileData> fileData = fileDAO.getFileById(fileId);
        if (fileData.isPresent() && fileData.get().getUser().getUsername().equals(username)) {
            return fileDAO.removeFile(fileData.get());
        } else {
            return false;
        }
    }

    public FileData getFileById(UUID fileId) {
        return fileDAO.getFileById(fileId).orElse(null);
    }

    public boolean isUserAuthorized(UUID fileId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        FileData fileData = getFileById(fileId);
        return fileData != null && fileData.getUser().getUsername().equals(username);
    }


}
