package com.example.uploadfileservice.repository;

import com.example.uploadfileservice.model.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {


}
