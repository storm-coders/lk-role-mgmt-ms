package com.lk.cloud.role.persistence;

import java.util.List;
import java.util.UUID;

import com.lk.cloud.role.domain.ModulePrivilege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulePrivilegeRepository extends JpaRepository<ModulePrivilege, UUID> {
    
    List<ModulePrivilege> findByModuleId(UUID moduleId);
}
