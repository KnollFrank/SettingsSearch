package de.KnollFrank.lib.preferencesearch.search;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import android.text.Spannable;
import android.text.SpannableString;
import android.util.Pair;

import androidx.preference.Preference;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.KnollFrank.lib.preferencesearch.search.PreferenceMatch.Type;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoAttribute;

class PreferenceMatchesHighlighter {

    public static void highlight(final List<PreferenceMatch> preferenceMatches,
                                 final Supplier<List<Object>> markupsFactory,
                                 final SearchableInfoAttribute searchableInfoAttribute) {
        PreferenceMatchesHighlighter
                .getIndexRangesByPreferenceAndType(preferenceMatches)
                .forEach(
                        (preferenceAndType, indexRanges) ->
                                highlight(
                                        preferenceAndType.first,
                                        preferenceAndType.second,
                                        indexRanges,
                                        markupsFactory,
                                        searchableInfoAttribute));
    }

    private static Map<Pair<Preference, Type>, List<IndexRange>> getIndexRangesByPreferenceAndType(
            final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .collect(
                        groupingBy(
                                preferenceMatch ->
                                        Pair.create(
                                                preferenceMatch.preference,
                                                preferenceMatch.type),
                                mapping(
                                        preferenceMatch -> preferenceMatch.indexRange,
                                        toList())));
    }

    private static void highlight(final Preference preference,
                                  final Type type,
                                  final List<IndexRange> indexRanges,
                                  final Supplier<List<Object>> markupsFactory,
                                  final SearchableInfoAttribute searchableInfoAttribute) {
        // FK-TODO: replace switch with inheritance?
        switch (type) {
            case TITLE:
                setTitle(preference, indexRanges, markupsFactory);
                break;
            case SUMMARY:
                setSummary(preference, indexRanges, markupsFactory);
                break;
            case SEARCHABLE_INFO:
                setSearchableInfo(preference, indexRanges, markupsFactory, searchableInfoAttribute);
                break;
        }
    }

    private static void setTitle(final Preference preference,
                                 final List<IndexRange> indexRanges,
                                 final Supplier<List<Object>> markupsFactory) {
        PreferenceTitle.setTitle(
                preference,
                createSpannableFromStrAndApplyMarkupsToIndexRanges(
                        preference.getTitle().toString(),
                        markupsFactory,
                        indexRanges));
    }

    private static void setSummary(final Preference preference,
                                   final List<IndexRange> indexRanges,
                                   final Supplier<List<Object>> markupsFactory) {
        PreferenceSummary.setSummary(
                preference,
                createSpannableFromStrAndApplyMarkupsToIndexRanges(
                        preference.getSummary().toString(),
                        markupsFactory,
                        indexRanges));
    }

    private static void setSearchableInfo(final Preference preference,
                                          final List<IndexRange> indexRanges,
                                          final Supplier<List<Object>> markupsFactory,
                                          final SearchableInfoAttribute searchableInfoAttribute) {
        searchableInfoAttribute.setSearchableInfo(
                preference,
                createSpannableFromStrAndApplyMarkupsToIndexRanges(
                        searchableInfoAttribute
                                .getSearchableInfo(preference)
                                .map(CharSequence::toString)
                                .orElse(""),
                        markupsFactory,
                        indexRanges));
    }

    private static SpannableString createSpannableFromStrAndApplyMarkupsToIndexRanges(
            final String str,
            final Supplier<List<Object>> markupsFactory,
            final List<IndexRange> indexRanges) {
        final SpannableString spannable = new SpannableString(str);
        applyMarkupsToIndexRanges(spannable, markupsFactory, indexRanges);
        return spannable;
    }

    private static void applyMarkupsToIndexRanges(final SpannableString spannable,
                                                  final Supplier<List<Object>> markupsFactory,
                                                  final List<IndexRange> indexRanges) {
        for (final IndexRange indexRange : indexRanges) {
            applyMarkupsToIndexRange(spannable, markupsFactory, indexRange);
        }
    }

    private static void applyMarkupsToIndexRange(final Spannable spannable,
                                                 final Supplier<List<Object>> markupsFactory,
                                                 final IndexRange indexRange) {
        for (final Object markup : markupsFactory.get()) {
            spannable.setSpan(
                    markup,
                    indexRange.startIndexInclusive,
                    indexRange.endIndexExclusive,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
