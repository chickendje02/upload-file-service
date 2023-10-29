package com.example.uploadfileservice.repository;

import com.example.uploadfileservice.model.entity.RoleEntity;
import com.example.uploadfileservice.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

}
