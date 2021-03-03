package com.lk.cloud.role.persistence;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.lk.cloud.role.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends
    JpaRepository<UserGroup, UUID>, JpaSpecificationExecutor<UserGroup> {

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM rlmgmt.user_group_privilege WHERE user_group_id=?1")
    void deletePrivileges(UUID id);

    @Query(nativeQuery = true, value = "SELECT cast (module_privilege_id as varchar) id FROM rlmgmt.user_group_privilege WHERE user_group_id=?1")
    List<String> fetchPrivileges(UUID id);
}
