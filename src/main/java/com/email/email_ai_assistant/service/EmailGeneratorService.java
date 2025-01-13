package com.email.email_ai_assistant.service;

import com.email.email_ai_assistant.dto.EmailRequest;

public interface EmailGeneratorService {
    public String generateEmailReply(EmailRequest emailRequest);
}
