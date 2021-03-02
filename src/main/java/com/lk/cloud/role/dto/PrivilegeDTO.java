package com.lk.cloud.role.dto;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import com.lk.cloud.role.dto.error.ErrorConstants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "Privilege")
public class PrivilegeDTO {
    
    @ApiModelProperty(name = "id", value = "Privilege ID")
    private UUID id;

    @ApiModelProperty(name = "code", value = "Privilege Code")
    @NotEmpty(message = ErrorConstants.INVALID_PRIVILEGE_CODE_CD)
    private String code;

    @ApiModelProperty(name = "description", value = "Privilege description")
    @NotEmpty(message = ErrorConstants.INVALID_MODULE_PRIVILEGES_CD)
    private String description;
}
