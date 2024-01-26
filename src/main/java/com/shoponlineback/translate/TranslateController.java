package com.shoponlineback.translate;

import net.suuft.libretranslate.Language;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslateController {
    private final TranslateService translateService;

    public TranslateController(TranslateService translateService) {
        this.translateService = translateService;
    }

    @GetMapping("/translate")
    ResponseEntity<String> translate(@RequestParam String text, @RequestParam String langCode){
        try{
           return ResponseEntity.ok(translateService.translate(text, langCode));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
}
