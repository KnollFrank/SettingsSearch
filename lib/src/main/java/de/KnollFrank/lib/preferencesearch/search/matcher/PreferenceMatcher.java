package de.KnollFrank.lib.preferencesearch.search.matcher;

import static de.KnollFrank.lib.preferencesearch.search.matcher.PreferenceAttributes.getSummary;
import static de.KnollFrank.lib.preferencesearch.search.matcher.PreferenceAttributes.getTitle;

import android.text.TextUtils;

import androidx.preference.Preference;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Lists;
import de.KnollFrank.lib.preferencesearch.common.Strings;
import de.KnollFrank.lib.preferencesearch.search.matcher.Match.Type;

public class PreferenceMatcher {

    public static List<Match> getMatches(final Preference haystack, final String needle) {
        if (TextUtils.isEmpty(needle)) {
            return Collections.emptyList();
        }
        return Lists.concat(
                getTitleMatches(haystack, needle),
                getSummaryMatches(haystack, needle));
    }

    private static List<Match> getTitleMatches(final Preference haystack, final String needle) {
        return getMatches(getTitle(haystack), needle, createMatch(Type.TITLE));
    }

    private static List<Match> getSummaryMatches(final Preference haystack, final String needle) {
        return getMatches(getSummary(haystack), needle, createMatch(Type.SUMMARY));
    }

    private static BiFunction<Integer, Integer, Match> createMatch(final Type type) {
        return (startInclusive, endExclusive) -> new Match(type, startInclusive, endExclusive);
    }

    private static List<Match> getMatches(final Optional<String> haystack,
                                          final String needle,
                                          final BiFunction<Integer, Integer, Match> createMatch) {
        return haystack
                .map(_haystack -> getMatches(_haystack, needle, createMatch))
                .orElse(Collections.emptyList());
    }

    private static List<Match> getMatches(final String haystack,
                                          final String needle,
                                          final BiFunction<Integer, Integer, Match> createMatch) {
        return Strings
                .getIndices(haystack.toLowerCase(), needle.toLowerCase())
                .stream()
                .map(index -> createMatch.apply(index, index + needle.length()))
                .collect(Collectors.toList());
    }
}
