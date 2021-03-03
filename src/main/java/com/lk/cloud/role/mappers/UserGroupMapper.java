package com.lk.cloud.role.mappers;

import com.lk.cloud.role.dto.UserGroupDTO;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.lk.cloud.role.domain.ModulePrivilege;
import com.lk.cloud.role.domain.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface UserGroupMapper {
    
    UserGroupDTO fromEntity(UserGroup userGroup);

    @Mappings({
        @Mapping(target = "privileges", ignore = true)
    })
    UserGroupDTO fromEntitySummary(UserGroup userGroup);

    @Mappings({
        @Mapping(target = "privileges", ignore = true)
    })
    UserGroup fromDTO(UserGroupDTO userGroupDTO);

    
    default List<UUID> mapPrivileges(List<ModulePrivilege> privileges) {
        if (privileges == null)
            return Collections.emptyList();
        return privileges
                    .parallelStream()
                    .map(ModulePrivilege::getId)
                    .collect(Collectors.toList());
    }
}
