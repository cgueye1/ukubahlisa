package com.wakana.realestateworks.services;

import java.time.LocalDateTime;
import java.util.List;

import com.wakana.realestateworks.model.RealEstateProperty;

public interface EmailService {

    public void sendSimpleMessage(String to, String subject, String text);

    public void sendHtmlMessage(String to, String subject, String html, List<String> ccEmails);

    public void    sendMeetingInvite(
        List<String> to, 
        List<String> cc, 
        List<String> bcc, 
        String subject, 
        String description, 
        LocalDateTime startTime, 
        LocalDateTime endTime, 
        String location);

    public String getPropertyHTML(RealEstateProperty property, String headerMessage, String message, String alertColor);
}
