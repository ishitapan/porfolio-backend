package portfolio.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.backend.dto.ContactRequest;
import portfolio.backend.service.ContactService;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<String> submit(@RequestBody ContactRequest request) {
        contactService.handleContact(request);
        return ResponseEntity.ok("Message sent successfully");
    }
}
