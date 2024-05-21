package com.bozdemir.clodious.DAO;

import com.bozdemir.clodious.model.FileData;
import com.bozdemir.clodious.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface FileDAO {
    Optional<FileData> getFileByName(String fileName);
    Boolean saveOrUpdateFile(FileData fileMeta);
    Boolean removeFile(FileData fileMeta);
    List<FileData> getAllFilesByUserName(String userName);
    Optional<FileData> getFileById(UUID fileId);
    List<FileData> getAllFilesByUser(User user);
}
