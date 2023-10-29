package com.example.uploadfileservice.model.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "role")
@AttributeOverride(name = "id", column = @Column(name = "role_id", nullable = false))
public class RoleEntity extends BaseEntity {

    @Column(name = "role_name")
    private String roleName;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> permissions = new HashSet<>();

}
