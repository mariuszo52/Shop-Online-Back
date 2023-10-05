package com.shoponlineback.language;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;

    }

    List<String> getAllLanguagesNames(){
        return StreamSupport.stream(languageRepository.findAll().spliterator(), false)
                .map(Language::getName)
                .collect(Collectors.toList());
    }
}
