package de.KnollFrank.lib.settingssearch.search;

import android.text.TextUtils;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch.Type;

class PreferenceMatcher {

    public static List<PreferenceMatch> getPreferenceMatches(
            final SearchablePreferencePOJO haystack,
            final String needle) {
        if (TextUtils.isEmpty(needle)) {
            return Collections.emptyList();
        }
        return ImmutableList
                .<PreferenceMatch>builder()
                .addAll(getTitlePreferenceMatches(haystack, needle))
                .addAll(getSummaryPreferenceMatches(haystack, needle))
                .addAll(getSearchableInfoPreferenceMatches(haystack, needle))
                .build();
    }

    public static boolean matches(final String haystack, final String needle) {
        final List<PreferenceMatch> preferenceMatches =
                getPreferenceMatches(
                        Optional.of(haystack),
                        needle,
                        indexRange -> null);
        return !preferenceMatches.isEmpty();
    }

    private static List<PreferenceMatch> getTitlePreferenceMatches(
            final SearchablePreferencePOJO haystack,
            final String needle) {
        return getPreferenceMatches(
                haystack.title(),
                needle,
                indexRange -> new PreferenceMatch(haystack, Type.TITLE, indexRange));
    }

    private static List<PreferenceMatch> getSummaryPreferenceMatches(
            final SearchablePreferencePOJO haystack,
            final String needle) {
        return getPreferenceMatches(
                haystack.summary(),
                needle,
                indexRange -> new PreferenceMatch(haystack, Type.SUMMARY, indexRange));
    }

    private static List<PreferenceMatch> getSearchableInfoPreferenceMatches(
            final SearchablePreferencePOJO haystack,
            final String needle) {
        return getPreferenceMatches(
                haystack.searchableInfo(),
                needle,
                indexRange -> new PreferenceMatch(haystack, Type.SEARCHABLE_INFO, indexRange));
    }

    private static List<PreferenceMatch> getPreferenceMatches(
            final Optional<String> haystack,
            final String needle,
            final Function<IndexRange, PreferenceMatch> createPreferenceMatch) {
        return haystack
                .map(_haystack -> getPreferenceMatches(_haystack, needle, createPreferenceMatch))
                .orElse(Collections.emptyList());
    }

    private static List<PreferenceMatch> getPreferenceMatches(
            final String haystack,
            final String needle,
            final Function<IndexRange, PreferenceMatch> createPreferenceMatch) {
        return Strings
                .getIndicesOfNeedleWithinHaystack(haystack.toLowerCase(), needle.toLowerCase())
                .stream()
                .map(index -> createPreferenceMatch.apply(new IndexRange(index, index + needle.length())))
                .collect(Collectors.toList());
    }
}
