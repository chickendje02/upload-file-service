package com.example.uploadfileservice.service;

import com.example.uploadfileservice.model.dto.FileDetailDTO;
import com.example.uploadfileservice.model.entity.FileEntity;
import com.example.uploadfileservice.model.filter.FileFilter;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileService {

    void uploadFile(MultipartFile file);

    Page<FileEntity> listUploadedFile(FileFilter fileFilter);

    FileDetailDTO getFileById(long fileId);

    Map<String, Object> deleteFileById(long fileId);
}
