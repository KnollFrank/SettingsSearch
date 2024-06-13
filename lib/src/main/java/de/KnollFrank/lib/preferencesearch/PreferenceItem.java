package de.KnollFrank.lib.preferencesearch;

import android.text.TextUtils;

import java.util.Optional;
import java.util.stream.Stream;

public class PreferenceItem {

    private final Optional<String> title;
    private final Optional<String> summary;
    // FK-TODO: remove key
    private final Optional<String> key;
    private final Optional<String> entries;

    public PreferenceItem(final Optional<String> title,
                          final Optional<String> summary,
                          final Optional<String> key,
                          final Optional<String> entries) {
        this.title = title;
        this.summary = summary;
        this.key = key;
        this.entries = entries;
    }

    public boolean matches(final String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return false;
        }
        return Stream
                .of(title, summary, entries)
                .anyMatch(haystack -> matches(haystack, keyword));
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
