package com.shoponlineback.language;

import com.shoponlineback.product.Product;

import java.util.Collections;
import java.util.List;

public class LanguageMapper {
    private final LanguageRepository languageRepository;

    public LanguageMapper(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public static LanguageDto map(Language language) {
        LanguageDto languageDto = new LanguageDto();
        languageDto.setId(language.getId());
        languageDto.setName(language.getName());
        languageDto.setIconUrl(language.getIconUrl());
        return languageDto;
    }

    public static Language map(LanguageDto languageDto) {
        return new Language(languageDto.getId(), languageDto.getName(),
                languageDto.getIconUrl(), Collections.emptyList());

    }
}
