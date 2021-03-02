package com.lk.cloud.role.persistence;

import java.util.UUID;

import com.lk.cloud.role.domain.ApplicationModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationModuleRepository extends
JpaRepository<ApplicationModule, UUID>, JpaSpecificationExecutor<ApplicationModule> {}
