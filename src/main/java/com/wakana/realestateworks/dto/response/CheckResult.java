package com.wakana.realestateworks.dto.response;

public class CheckResult {
    private boolean success;
    private String message;

    public CheckResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
