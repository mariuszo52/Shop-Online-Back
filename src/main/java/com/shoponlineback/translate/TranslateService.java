package com.shoponlineback.translate;

import net.suuft.libretranslate.Language;
import net.suuft.libretranslate.Translator;
import org.springframework.stereotype.Service;

@Service
public class TranslateService {
    String translate(String text, String langCode) {
        Language language = null;
        for (Language value : Language.values()) {
            if (langCode.equals(value.getCode())) {
                language = value;
            }
        }
        if (language != null) {
            if (language.equals(Language.POLISH)) {
                return Translator.translate(Language.ENGLISH, Language.POLISH, text);
            } else if (language.equals(Language.ENGLISH)) {
                return Translator.translate(Language.POLISH, Language.ENGLISH, text);

            } else {
                throw new RuntimeException("Wrong language code.");
            }
        } else {
            throw new RuntimeException("There is not language code.");
        }

    }
}
