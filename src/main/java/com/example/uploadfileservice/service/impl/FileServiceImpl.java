package com.example.uploadfileservice.service.impl;

import com.example.uploadfileservice.enumeration.Progress;
import com.example.uploadfileservice.exception.CommonBusinessException;
import com.example.uploadfileservice.handler.JsonHandler;
import com.example.uploadfileservice.handler.UploadFileService;
import com.example.uploadfileservice.model.dto.FileDetailDTO;
import com.example.uploadfileservice.model.entity.FileEntity;
import com.example.uploadfileservice.model.entity.FileDetail;
import com.example.uploadfileservice.model.filter.FileFilter;
import com.example.uploadfileservice.repository.FileDetailRepository;
import com.example.uploadfileservice.repository.FileRepository;
import com.example.uploadfileservice.service.FileService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@Service
@Log4j2
public class FileServiceImpl implements FileService {

    @Autowired
    UploadFileService uploadFileService;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    FileDetailRepository fileDetailRepository;

    @Override
    @Transactional
    public void uploadFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new CommonBusinessException("Invalid File");
            }
            String fileName = file.getOriginalFilename();
            int index = fileName.lastIndexOf(".");
            String fileExtension = fileName.substring(index + 1);
            // insert to DB
            long fileId = this.handleInsertFile(fileName, fileExtension);
            // Create temp file to store the stream just in case the parent thread closed since upload file is asynchronous
            Path tempFile = Files.createTempFile(fileName, "." + fileExtension);
            file.transferTo(tempFile.toFile());
            CompletableFuture.runAsync(() -> this.handleUploadFileDetail(tempFile, fileExtension, fileId));
        } catch (Exception e) {
            log.error("Exception uploading file ", e);
            throw new CommonBusinessException("Error when uploading file");
        }
    }

    @Override
    public Page<FileEntity> listUploadedFile(FileFilter fileFilter) {
        Pageable pageable = PageRequest.of(fileFilter.getPage(), fileFilter.getSize());
        return fileRepository.findAll(pageable);
    }

    @Override
    public FileDetailDTO getFileById(long fileId) {
        Optional<FileEntity> file = fileRepository.findById(fileId);
        if (file.isPresent()) {
            FileEntity fileData = file.get();
            CompletableFuture.runAsync(() -> {
                FileEntity entity = JsonHandler.deepCloneObject(fileData, new TypeReference<FileEntity>() {
                });
                entity.setLastAccessBy("Trung");
                entity.setLastAccessTime(LocalDateTime.now());
                fileRepository.save(entity);
            });
            Optional<List<FileDetail>> listFileDetail = fileDetailRepository.findFileDetailsByFileId(fileId);
            return FileDetailDTO.builder()
                    .fileName(fileData.getFileName())
                    .fileType(fileData.getFileType())
                    .status(Progress.valueOf(fileData.getStatus()))
                    .lastAccessBy(fileData.getLastAccessBy())
                    .lastAccessTime(fileData.getLastAccessTime())
                    .fileDetailList(listFileDetail.orElse(new ArrayList<>()))
                    .build();
        } else {
            throw new CommonBusinessException("File not Found", HttpStatus.NOT_FOUND.value());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> deleteFileById(long fileId) {
        Map<String, Object> result = new HashMap<>();
        try {
            fileRepository.deleteById(fileId);
            fileDetailRepository.deleteFileDetailByFileId(fileId);
            result.put("result", "Success");
            result.put("code", 200);
            return result;
        } catch (Exception e) {
            log.error("Exception when delete file ", e);
            throw new CommonBusinessException("Failed to delete file");
        }
    }

    private long handleInsertFile(String fileName, String fileExtension) {
        FileEntity entity = new FileEntity();
        entity.setFileName(fileName);
        entity.setStatus(Progress.IN_PROGRESS.name());
        entity.setFileType(fileExtension);
        entity.setLastAccessBy("System");
        entity.setLastAccessTime(LocalDateTime.now());
        entity.setCreatedDate(LocalDateTime.now());
        FileEntity entitySaved = fileRepository.saveAndFlush(entity);
        return entitySaved.getId();
    }

    private void handleUploadFileDetail(Path tempFile, String fileExtension, long fileId) {
        uploadFileService.uploadFile(tempFile, fileExtension, fileId);
        Optional<FileEntity> savedFiled = fileRepository.findById(fileId);
        savedFiled.ifPresent(file -> {
            file.setStatus(Progress.DONE.name());
            fileRepository.save(file);
        });
    }
}
