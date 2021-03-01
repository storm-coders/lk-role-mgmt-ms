package com.vcgdev.demo.rest.exception;

public enum ValidationError {
    
    INVALID_ID(ErrorConstants.INVALID_ID_CODE, ErrorConstants.INVALID_ID_MESSAGE);
    
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
