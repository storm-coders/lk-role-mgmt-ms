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

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@ApiModel(description = "Group")
public class UserGroupDTO{

	@ApiModelProperty(name = "id", value = "Group Id")
	@JsonProperty(access = Access.READ_ONLY)
	private UUID id;

	@ApiModelProperty(name = "name", value = "Group name")
	@NotEmpty
	private String name;

	@ApiModelProperty(name = "parentId", value = "Parent ID")
	private UUID parentId;

	@ApiModelProperty(name = "privileges", value = "Set privileges")
	@NotEmpty
	private List<UUID> privileges;

}
