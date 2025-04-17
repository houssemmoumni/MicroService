package com.PIDEV.Communication_Service.Services;

import com.PIDEV.Communication_Service.DTO.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiAIService {

    @Value("${gemini.api.key}")
    private String apiKey;

  //  private final String GEMINI_API_URL = "private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent";
  private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent";
    private final RestTemplate restTemplate;

    public GeminiAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateResponse(String prompt) {
        try {
            String url = GEMINI_API_URL + "?key=" + apiKey;

            String requestBody = String.format("""
        {
            "contents": [{
                "parts": [{"text": "%s"}]
            }]
        }
        """, prompt.replace("\"", "\\\""));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    GeminiResponse.class
            );

            if (response.getBody() != null
                    && response.getBody().getCandidates() != null
                    && !response.getBody().getCandidates().isEmpty()
                    && response.getBody().getCandidates().get(0).getContent() != null
                    && response.getBody().getCandidates().get(0).getContent().getParts() != null
                    && !response.getBody().getCandidates().get(0).getContent().getParts().isEmpty()) {

                return response.getBody().getCandidates().get(0)
                        .getContent().getParts().get(0).getText();
            }

            return "Aucune réponse valide reçue";
        } catch (Exception e) {
            throw new RuntimeException("Erreur API Gemini: " + e.getMessage(), e);
        }
    }
}