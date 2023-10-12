package com.shoponlineback.language;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/language")
public class LanguageController {
    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }
    @GetMapping("/all")
    ResponseEntity<List<String>> getAllLanguagesNames(){
       return ResponseEntity.ok(languageService.getAllLanguagesNames());
    }

    @GetMapping("/")
    ResponseEntity<List<String>> getLanguageById(@RequestParam Long id){
        return ResponseEntity.ok(languageService.getProductsLanguages(id));
    }
}
