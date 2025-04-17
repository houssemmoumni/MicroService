package com.PIDEV.Communication_Service.Controllers;

import com.PIDEV.Communication_Service.Services.GeminiAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatbotController {

    private final GeminiAIService geminiAIService;

    public ChatbotController(GeminiAIService geminiAIService) {
        this.geminiAIService = geminiAIService;
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askQuestion(@RequestBody Map<String, String> request) {
        try {
            String question = request.get("question");
            String response = geminiAIService.generateResponse(question);
            return ResponseEntity.ok(Collections.singletonMap("response", response));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Collections.singletonMap("error", "Erreur lors de la génération de la réponse"));
        }
    }

    @PostMapping("/reclamation-help")
    public ResponseEntity<Map<String, String>> getReclamationHelp(@RequestBody Map<String, String> request) {
        String userQuery = request.get("userQuery");

        String prompt = """
        Tu es un expert en gestion des réclamations.
        Règles :
        1. Réponds exclusivement aux questions sur les réclamations
        2. Sois précis et technique
        3. Formatte les réponses avec des points clairs
        4. Propose des solutions concrètes
        
        Question : %s
        """.formatted(userQuery);

        try {
            String response = geminiAIService.generateResponse(prompt);
            return ResponseEntity.ok(Collections.singletonMap("response", response));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Collections.singletonMap("error",
                            "Erreur technique. Veuillez contacter le service client par email : support@votresociete.com"));
        }
    }}