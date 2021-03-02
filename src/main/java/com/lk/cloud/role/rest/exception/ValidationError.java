package com.lk.cloud.role.rest.exception;

import com.lk.cloud.role.dto.error.ErrorConstants;

public enum ValidationError {
    
    INVALID_ID(ErrorConstants.INVALID_ID_CD, ErrorConstants.INVALID_ID_MSG),
    INVALID_MODULE_NM(ErrorConstants.INVALID_MODULE_NAME_CD, ErrorConstants.INVALID_MODULE_NAME_MSG),
    INVALID_MODULE_DESC(ErrorConstants.INVALID_MODULE_DESC_CD, ErrorConstants.INVALID_MODULE_DESC_MSG),
    INVALID_MODULE_ACT(ErrorConstants.INVALID_MODULE_ACTIVE_CD, ErrorConstants.INVALID_MODULE_ACTIVE_MSG),
    INVALID_PRIVILEGE_LIST(ErrorConstants.INVALID_MODULE_PRIVILEGES_CD, ErrorConstants.INVALID_MODULE_PRIVILEGES_MSG),
    INVALID_PRIVILEGE_CD(ErrorConstants.INVALID_PRIVILEGE_CODE_CD, ErrorConstants.INVALID_PRIVILEGE_CODE_MSG),
    INVALID_PRIVILEGE_DESC(ErrorConstants.INVALID_PRIVILEGE_DESC_CD, ErrorConstants.INVALID_PRIVILEGE_DESC_MSG)
    ;
    
    private final String code;
    private final String message;

    ValidationError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ValidationError parse(String code) {
        for(ValidationError field : ValidationError.values()) {
            if(field.getCode().equals(code)) 
                return field;
        }
        return null;
    }
}
