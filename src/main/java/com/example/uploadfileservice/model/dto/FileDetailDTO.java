package com.example.uploadfileservice.model.dto;


import com.example.uploadfileservice.enumeration.Progress;
import com.example.uploadfileservice.model.entity.FileDetail;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FileDetailDTO {

    private long id;

    private String fileName;

    private String fileType;

    private Progress status;

    private String lastAccessBy;

    private LocalDateTime lastAccessTime;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private List<FileDetail> fileDetailList;
}
