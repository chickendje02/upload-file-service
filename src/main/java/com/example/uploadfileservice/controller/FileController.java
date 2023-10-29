package com.example.uploadfileservice.controller;

import com.example.uploadfileservice.model.dto.FileDetailDTO;
import com.example.uploadfileservice.model.entity.FileEntity;
import com.example.uploadfileservice.model.filter.FileFilter;
import com.example.uploadfileservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/v1/files")
public class FileController {

    private static final String UPLOAD_SUCCESS = "File uploaded successfully";

    @Autowired
    private FileService fileService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VIEW')")
    public ResponseEntity<Page<FileEntity>> listUploadedFile(FileFilter fileFilter) {
        return ResponseEntity.ok(fileService.listUploadedFile(fileFilter));
    }

    @GetMapping("{fileId}")
    @PreAuthorize("hasAnyAuthority('VIEW')")
    public ResponseEntity<FileDetailDTO> getFileDetailById(@PathVariable long fileId) {
        return ResponseEntity.ok(fileService.getFileById(fileId));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('UPLOAD')")
    public ResponseEntity<String> uploadFile(@RequestBody MultipartFile file) {
        fileService.uploadFile(file);
        return ResponseEntity.ok(UPLOAD_SUCCESS);
    }

    @DeleteMapping("{fileId}")
    @PreAuthorize("hasAnyAuthority('DELETE')")
    public ResponseEntity<Map<String, Object>> deleteFileById(@PathVariable long fileId) {
        return ResponseEntity.ok(fileService.deleteFileById(fileId));
    }
}
