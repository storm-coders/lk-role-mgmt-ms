package com.lk.cloud.role.dto.error;

public class ErrorConstants {
    
    private ErrorConstants() {}

    public static final String INVALID_ID_CD = "4000";
    public static final String INVALID_MODULE_NAME_CD = "4001";
    public static final String INVALID_MODULE_DESC_CD = "4002";
    public static final String INVALID_MODULE_ACTIVE_CD = "4003";
    public static final String INVALID_MODULE_PRIVILEGES_CD = "4004";
    public static final String INVALID_PRIVILEGE_CODE_CD = "4005";
    public static final String INVALID_PRIVILEGE_DESC_CD = "4005";
    public static final String INVALID_GROUP_NAME_CD = "4006";
    public static final String INVALID_GROUP_PRIVILEGES_CD = "4007"; 

    public static final String INVALID_ID_MSG = "Invalid id";
    public static final String INVALID_MODULE_NAME_MSG = "Invalid module name";
    public static final String INVALID_MODULE_DESC_MSG = "Invalid module description";
    public static final String INVALID_MODULE_ACTIVE_MSG = "Invalid active flag";
    public static final String INVALID_MODULE_PRIVILEGES_MSG = "Module should contain at least a privilege";
    public static final String INVALID_PRIVILEGE_CODE_MSG = "Invalid privilege code";
    public static final String INVALID_PRIVILEGE_DESC_MSG = "Invalid privilege description";
    public static final String INVALID_GROUP_NAME_MSG = "The name is mandatory";
    public static final String INVALID_GROUP_PRIVILEGES_MSG = "Privileges should not be empty"; 

    
}
