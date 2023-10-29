package com.example.uploadfileservice.repository;

import com.example.uploadfileservice.model.entity.FileDetail;
import com.example.uploadfileservice.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM UserEntity u INNER JOIN u.role c WHERE u.username = :username")
    Optional<UserEntity> findUserEntitiesWithPermission(String username);
}

