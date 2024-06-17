package de.KnollFrank.lib.preferencesearch.matcher;

import static de.KnollFrank.lib.preferencesearch.matcher.PreferenceAttributes.getEntries;
import static de.KnollFrank.lib.preferencesearch.matcher.PreferenceAttributes.getSummary;
import static de.KnollFrank.lib.preferencesearch.matcher.PreferenceAttributes.getTitle;

import android.text.TextUtils;

import androidx.preference.Preference;

import java.util.Optional;
import java.util.stream.Stream;

public class PreferenceMatcher {

    public static boolean matches(final Preference haystack, final String needle) {
        if (TextUtils.isEmpty(needle)) {
            return false;
        }
        return Stream
                .of(getTitle(haystack), getSummary(haystack), getEntries(haystack))
                .anyMatch(_haystack -> matches(_haystack, needle));
    }

    private static boolean matches(final Optional<String> haystack, final String needle) {
        return haystack
                .filter(_haystack -> matches(_haystack, needle))
                .isPresent();
    }

    private static boolean matches(final String haystack, final String needle) {
        return haystack.toLowerCase().contains(needle.toLowerCase());
    }
}
