package com.lk.cloud.role.mappers;

import com.lk.cloud.role.dto.ApplicationModuleDTO;
import com.lk.cloud.role.domain.ApplicationModule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface ApplicationModuleMapper {
    ApplicationModuleDTO fromEntity(ApplicationModule applicationModule);

    @Mappings({
        @Mapping(target = "privileges", ignore = true)
    })
    ApplicationModuleDTO fromEntitySummary(ApplicationModule applicationModule);
    
    ApplicationModule fromDTO(ApplicationModuleDTO applicationModuleDTO);
}
