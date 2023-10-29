package com.example.uploadfileservice.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "file")
@AttributeOverride(name = "id", column = @Column(name = "file_id", nullable = false))
public class FileEntity extends BaseEntity {

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "status")
    private String status;

    @Column(name = "last_access_by")
    private String lastAccessBy;

    @Column(name = "last_access_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastAccessTime;

}
