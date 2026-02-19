package de.KnollFrank.lib.settingssearch.common;

import static de.KnollFrank.lib.settingssearch.common.IndexSearchResultConverter.minusOneToEmpty;

import java.util.Optional;
import java.util.OptionalInt;

public class Strings {

    private Strings() {
    }

    public static OptionalInt indexOf(final String haystack, final String needle, final int fromIndex) {
        return minusOneToEmpty(haystack.indexOf(needle, fromIndex));
    }

    public static Optional<String> toString(final Optional<CharSequence> charSequence) {
        return charSequence.map(CharSequence::toString);
    }

    public static String prefixIdWithLanguage(final String id, final LanguageCode languageCode) {
        return languageCode.code() + "-" + id;
    }
}
