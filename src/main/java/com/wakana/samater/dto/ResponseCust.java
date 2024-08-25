package com.wakana.samater.dto;

import lombok.Data;

@Data
public class ResponseCust {
    private boolean success;
    private String message;

    // Constructeur
    public ResponseCust(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters et setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
