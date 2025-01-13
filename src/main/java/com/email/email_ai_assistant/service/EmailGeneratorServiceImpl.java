package com.email.email_ai_assistant.service;

import com.email.email_ai_assistant.dto.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorServiceImpl implements EmailGeneratorService {

    @Value("${GEMINI-api-key}")
    private String apiKey;

    @Value("${GEMINI-api-url}")
    private String apiUrl;

    private WebClient webClient;
    public EmailGeneratorServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    private String buildPrompt(EmailRequest emailRequest){
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email");
        if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()){
            prompt.append("Use a ").append(emailRequest.getTone()).append("tone");
        }
        prompt.append("\nOriginal email is : ").append(emailRequest.getEmailContent());
        return prompt.toString();
    }

    @Override
    public String generateEmailReply(EmailRequest emailRequest) {
        //create a prompt
        String prompt = buildPrompt(emailRequest);

        //creating format for request;
        Map<String, Object> requestBody = Map.of("contents", new Object[]{
                Map.of("parts", new Object[]{
                        Map.of("text", prompt)
                })
        });

        // make an api call
        String response = webClient.post()
                .uri(apiUrl +  apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //return extracted Response
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();


        }catch (Exception e){
            return "Occurred error while extracting the response "+ e.getMessage();
        }
    }

}
