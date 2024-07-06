package de.KnollFrank.lib.preferencesearch.search;

import android.text.TextUtils;

import androidx.preference.Preference;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Lists;
import de.KnollFrank.lib.preferencesearch.common.Strings;
import de.KnollFrank.lib.preferencesearch.search.PreferenceMatch.Type;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoGetter;

class PreferenceMatcher {

    public static List<PreferenceMatch> getPreferenceMatches(
            final Preference haystack,
            final String needle,
            final SearchableInfoGetter searchableInfoGetter) {
        if (TextUtils.isEmpty(needle)) {
            return Collections.emptyList();
        }
        return Lists.concat(
                getTitlePreferenceMatches(haystack, needle),
                getSummaryPreferenceMatch(haystack, needle),
                getSearchableInfoPreferenceMatch(haystack, needle, searchableInfoGetter));
    }

    private static List<PreferenceMatch> getTitlePreferenceMatches(final Preference haystack,
                                                                   final String needle) {
        return getPreferenceMatches(
                PreferenceAttributes.getOptionalTitle(haystack).map(CharSequence::toString),
                needle,
                indexRange -> new PreferenceMatch(haystack, Type.TITLE, indexRange));
    }

    private static List<PreferenceMatch> getSummaryPreferenceMatch(final Preference haystack,
                                                                   final String needle) {
        return getPreferenceMatches(
                PreferenceAttributes
                        .getOptionalSummary(haystack)
                        .map(CharSequence::toString),
                needle,
                indexRange -> new PreferenceMatch(haystack, Type.SUMMARY, indexRange));
    }

    private static List<PreferenceMatch> getSearchableInfoPreferenceMatch(
            final Preference haystack,
            final String needle,
            final SearchableInfoGetter searchableInfoGetter) {
        return getPreferenceMatches(
                searchableInfoGetter
                        .getSearchableInfo(haystack)
                        .map(CharSequence::toString),
                needle,
                indexRange -> new PreferenceMatch(haystack, Type.SEARCHABLE_INFO, indexRange));
    }

    static List<PreferenceMatch> getPreferenceMatches(
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
