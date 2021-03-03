package com.lk.cloud.role.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGroupTreeDTO {
    
    private UUID id;

    private String name;

    private List<PrivilegeDTO> privileges;

    private List<UserGroupTreeDTO> nodes;
}
