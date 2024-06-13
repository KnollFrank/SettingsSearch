package de.KnollFrank.lib.preferencesearch;

import android.text.TextUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class PreferenceItem implements IPreferenceItem {

    public final Optional<String> title;
    private final Optional<String> summary;
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

    @Override
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

    @Override
    public String toString() {
        return "PreferenceItem: " + title + " " + summary + " " + key;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PreferenceItem that = (PreferenceItem) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(key, that.key) &&
                Objects.equals(entries, that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, summary, key, entries);
    }
}
