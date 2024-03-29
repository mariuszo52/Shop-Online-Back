package com.shoponlineback.language;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class LanguageController {
    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("language/all")
    ResponseEntity<List<String>> getAllLanguagesNames() {
        return ResponseEntity.ok(languageService.getAllLanguagesNames());
    }

    @GetMapping("/language")
    ResponseEntity<List<LanguageDto>> getLanguageById(@RequestParam Long id) {
        try {
            List<LanguageDto> productsLanguages = languageService.getProductsLanguages(id);
            return ResponseEntity.ok(productsLanguages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
