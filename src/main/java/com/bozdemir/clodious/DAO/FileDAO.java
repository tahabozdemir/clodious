package com.bozdemir.clodious.DAO;

import com.bozdemir.clodious.model.FileMeta;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface FileDAO {
    Optional<FileMeta> getFileByName(String fileName);
    Boolean saveOrUpdateFile(FileMeta fileMeta);
    Boolean removeFile(FileMeta fileMeta);
}
