package com.example.uploadfileservice.repository;

import com.example.uploadfileservice.model.entity.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileDetailRepository extends JpaRepository<FileDetail, Long> {

    Optional<List<FileDetail>> findFileDetailsByFileId(long fileId);

    @Query("DELETE FROM FileDetail fileDetail WHERE fileDetail.fileId = :fileId")
    void deleteFileDetailByFileId(long fileId);
}
