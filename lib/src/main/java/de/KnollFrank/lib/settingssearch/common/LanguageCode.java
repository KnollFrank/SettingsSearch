package de.KnollFrank.lib.settingssearch.common;

import java.util.Locale;
import java.util.Set;

public record LanguageCode(String code) {

    private static final Set<String> ISO_LANGUAGES = Set.of(Locale.getISOLanguages());

    public LanguageCode {
        if (!ISO_LANGUAGES.contains(code)) {
            throw new IllegalArgumentException("'" + code + "' is not a valid or recognized ISO 639 language code.");
        }
        code = normalize(code);
    }

    public static LanguageCode from(final Locale locale) {
        return new LanguageCode(locale.getLanguage());
    }

    private static String normalize(final String languageCode) {
        return new Locale(languageCode).getLanguage();
    }
}
