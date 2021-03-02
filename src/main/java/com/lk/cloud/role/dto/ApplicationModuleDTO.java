package com.lk.cloud.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.lk.cloud.role.dto.error.ErrorConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@ApiModel(description = "Module") 
public class ApplicationModuleDTO{
    
	@ApiModelProperty(name = "id", value = "Module ID")
	@JsonProperty(access = Access.READ_ONLY)
	private UUID id;

	@ApiModelProperty(name = "name", value = "Module name")
	@NotEmpty(message = ErrorConstants.INVALID_MODULE_NAME_CD)
	private String name;

	@ApiModelProperty(name = "description", value = "Module description")
	@NotEmpty(message = ErrorConstants.INVALID_MODULE_DESC_CD)
	private String description;

	@ApiModelProperty(name = "isActive", value = "Flag to know if module is active")
	@NotNull(message = ErrorConstants.INVALID_MODULE_ACTIVE_CD)
	private Boolean isActive;

	@ApiModelProperty(name = "privileges", value = "Privileges of module")
	@NotEmpty(message = ErrorConstants.INVALID_MODULE_PRIVILEGES_CD)
	@Valid
	private List<PrivilegeDTO> privileges;

}
