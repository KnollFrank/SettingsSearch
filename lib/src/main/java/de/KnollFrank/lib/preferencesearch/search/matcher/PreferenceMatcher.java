package de.KnollFrank.lib.preferencesearch.search.matcher;

import android.text.TextUtils;

import androidx.preference.Preference;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Lists;
import de.KnollFrank.lib.preferencesearch.common.Strings;
import de.KnollFrank.lib.preferencesearch.search.matcher.PreferenceMatch.Type;

public class PreferenceMatcher {

    public static List<PreferenceMatch> getPreferenceMatches(final Preference haystack,
                                                             final String needle) {
        if (TextUtils.isEmpty(needle)) {
            return Collections.emptyList();
        }
        return Lists.concat(
                getTitleMatches(haystack, needle),
                getSummaryMatches(haystack, needle));
    }

    private static List<PreferenceMatch> getTitleMatches(final Preference haystack, final String needle) {
        return getPreferenceMatches(haystack, needle, PreferenceAttributes::getTitle, Type.TITLE);
    }

    private static List<PreferenceMatch> getSummaryMatches(final Preference haystack, final String needle) {
        return getPreferenceMatches(haystack, needle, PreferenceAttributes::getSummary, Type.SUMMARY);
    }

    private static List<PreferenceMatch> getPreferenceMatches(
            final Preference haystack,
            final String needle,
            final Function<Preference, Optional<String>> getAttribute,
            final Type type) {
        return getAttribute
                .apply(haystack)
                .map(_haystack -> getPreferenceMatches(_haystack, needle, createPreferenceMatch(haystack, type)))
                .orElse(Collections.emptyList());
    }

    private static Function<IndexRange, PreferenceMatch> createPreferenceMatch(
            final Preference preference,
            final Type type) {
        return indexRange -> new PreferenceMatch(preference, type, indexRange);
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
