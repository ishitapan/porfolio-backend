package portfolio.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import portfolio.backend.dto.ContactRequest;

import java.util.*;

@Service
public class ContactService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void handleContact(ContactRequest request) {
        sendEmailViaApi(request);
    }

    private void sendEmailViaApi(ContactRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        // Prepare the JSON body for Brevo
        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", "Portfolio Visitor", "email", "panishita21@gmail.com"));
        body.put("to", List.of(Map.of("email", "panishita21@gmail.com")));
        body.put("subject", "New Portfolio Message from " + request.getName());
        body.put("textContent", "Sender Email: " + request.getEmail() + "\n\nMessage:\n" + request.getMessage());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(apiUrl, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Email delivery failed via API");
        }
    }
}