package com.wakana.realestateworks.enums;

public enum StudyRequestStatus {
    PENDING,       // Created but not yet started
    IN_PROGRESS,   // BET working on it
    DELIVERED,     // BET delivered report(s)
    VALIDATED,     // Approved by client
    REJECTED       // Refused by client
}