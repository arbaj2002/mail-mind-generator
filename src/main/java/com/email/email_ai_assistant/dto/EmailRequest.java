package com.email.email_ai_assistant.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class EmailRequest {
    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    String emailContent;
    String tone;
}
