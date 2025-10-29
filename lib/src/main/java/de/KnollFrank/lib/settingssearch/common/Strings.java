package de.KnollFrank.lib.settingssearch.common;

import static de.KnollFrank.lib.settingssearch.common.IndexSearchResultConverter.minusOne2Empty;

import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;

public class Strings {

    public static OptionalInt indexOf(final String haystack, final String needle, final int fromIndex) {
        return minusOne2Empty(haystack.indexOf(needle, fromIndex));
    }

    public static Optional<String> toString(final Optional<CharSequence> charSequence) {
        return charSequence.map(CharSequence::toString);
    }

    public static String prefixIdWithLanguage(final String id, final Locale locale) {
        return locale.getLanguage() + "-" + id;
    }
}
