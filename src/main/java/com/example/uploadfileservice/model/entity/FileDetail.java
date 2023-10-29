package com.example.uploadfileservice.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "file_detail")
@AttributeOverride(name = "id", column = @Column(name = "file_detail_id", nullable = false))
public class FileDetail extends BaseEntity {

    @Column(name = "row_data")
    private String rowData;

    @Column(name = "is_header")
    private Boolean header;

    @Column(name = "file_id")
    private Long fileId;
}
