package src.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.io.Console;
import java.util.HashMap;

@Service
public class LLMService {
    @Value("${llm.api.key:}")
    private String apiKey;

    @Value("${llm.api.url:}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public LLMService() {
        this.restTemplate = new RestTemplate();
    }

    public String enhanceText(String text, String documentType, String interventionType, String infractionType) {
        try {
            String prompt = buildPrompt(text, documentType, interventionType, infractionType);
            String enhancedText = callLLMApi(prompt);
            return enhancedText;
        } catch (Exception e) {
            // Log the error with more details
            System.err.println("Error enhancing text: " + e.getMessage());
            // Rethrow the exception to allow proper handling at the controller level
            throw new RuntimeException("Failed to enhance text: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(String text, String documentType, String interventionType, String infractionType) {

        
        if ("3,MAD".equals(interventionType)) {
            interventionType = "Mise à disposition d'une personne à l'OPJTC";
        }

        String basePrompt;
        String docType = documentType.toLowerCase();
        String policeContext = "En tant qu'équipage de Police Municipale agissant dans le cadre de nos prérogatives légales et réglementaires. L'agent de Police municipale n'effectue pas de contrôle d'identité et pas d'enquête. Il constate uniquement les faits" + 
            (infractionType != null ? ", concernant une infraction de type " + infractionType : "") + ", ";
        
        if ("rapport".equals(docType) || "procès-verbal".equals(docType)) {
            basePrompt = String.format(
                policeContext + "spécialisé dans les interventions de type %s, améliorez et formatez ce rapport de police en:\n" +
                "1. Structurant le document selon le modèle officiel avec les sections suivantes:\n" +
                "   - En-tête avec République Française, Police Municipale et références\n" +
                "   - Titre 'RAPPORT' en majuscules et centré\n" +
                "   - Date et lieu de rédaction\n" +
                "   - Objet du rapport clairement identifié\n" +
                "   - Corps du rapport avec les faits numérotés chronologiquement\n" +
                "   - Section de conclusion et signature\n" +
                "2. Utilisant un vocabulaire juridique approprié au type d'intervention\n" +
                "3. Ajoutant les références aux articles de loi spécifiques à ce type d'intervention et aux prérogatives de la Police Municipale\n" +
                "4. Utilisant le pronom personnel 'nous' pour décrire les actions des agents\n" +
                "5. Incluant les informations administratives comme les matricules des agents\n\n" +
                "Type d'intervention spécifique: %s\n" +
                "Texte original:\n%s", documentType,interventionType, infractionType, text);
        } else if ("main courante".equals(docType)) {
            basePrompt = String.format(
                policeContext + "améliorez cette main courante en utilisant un langage professionnel et fluide, tout en conservant les informations essentielles.\n\n" +
                "Type d'intervention spécifique: %s\n" +
                "Texte original:\n%s", interventionType, text);
        } else {
            basePrompt = text;
        }
        return basePrompt;
    }

    private String callLLMApi(String prompt) {
        if (apiKey == null || apiKey.isEmpty() || apiUrl == null || apiUrl.isEmpty()) {
            throw new IllegalStateException("API key or URL not configured properly");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistral-small-latest");
        requestBody.put("messages", java.util.Arrays.asList(
            Map.of("role", "system", "content", "Vous êtes un assistant spécialisé dans l'amélioration des documents des forces de l'ordre. Formatez votre réponse avec des sauts de ligne et des espacements appropriés pour une meilleure lisibilité. Répondez toujours en français en utilisant une terminologie professionnelle adaptée aux forces de l'ordre."),
            Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000);
        requestBody.put("top_p", 1.0);
        requestBody.put("frequency_penalty", 0.0);
        requestBody.put("presence_penalty", 0.0);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(apiUrl, request, Map.class);
            if (response != null && response.containsKey("choices")) {
                java.util.List<?> choices = (java.util.List<?>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = (Map<String, Object>) choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    String enhancedText = (String) message.get("content");
                    if (enhancedText != null) {
                        // Format the response text for better readability
                        enhancedText = enhancedText.trim()
                            .replaceAll("\\n\\s*\\n\\s*\\n+", "\n\n")  // Replace multiple blank lines with double line break
                            .replaceAll("(?m)^\\s+$", "")  // Remove lines containing only whitespace
                            .replaceAll("\\s+$", "");  // Remove trailing whitespace from each line
                        
                        // Ensure proper spacing around sections and lists
                        enhancedText = enhancedText
                            .replaceAll("(?m)^(\\d+\\.)\\s", "\n$1 ")  // Add line break before numbered lists
                            .replaceAll("(?m)^(-|\\*)\\s", "\n$0 ")  // Add line break before bullet points
                            .replaceAll("(?m)^([A-Z][^.!?]*:)", "\n$1");  // Add line break before section headers
                            
                        return enhancedText;
                    }
                }
            }
            throw new RuntimeException("Invalid API response structure: " + response);
        } catch (Exception e) {
            throw new RuntimeException("Error calling Mistral AI API: " + e.getMessage(), e);
        }
    }
}