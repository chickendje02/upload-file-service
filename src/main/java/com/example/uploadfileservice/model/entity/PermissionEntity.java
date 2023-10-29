package com.example.uploadfileservice.model.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "permisison")
@AttributeOverride(name = "id", column = @Column(name = "permission_id", nullable = false))
public class PermissionEntity extends BaseEntity {

    @Column(name = "permission_name")
    private String permissionName;

}
