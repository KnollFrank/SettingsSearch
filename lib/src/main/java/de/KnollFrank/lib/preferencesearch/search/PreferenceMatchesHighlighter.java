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

    private final Supplier<List<Object>> markupsFactory;
    private final SearchableInfoAttribute searchableInfoAttribute;

    public PreferenceMatchesHighlighter(final Supplier<List<Object>> markupsFactory,
                                        final SearchableInfoAttribute searchableInfoAttribute) {
        this.markupsFactory = markupsFactory;
        this.searchableInfoAttribute = searchableInfoAttribute;
    }

    public void highlight(final List<PreferenceMatch> preferenceMatches) {
        PreferenceMatchesHighlighter
                .getIndexRangesByPreferenceAndType(preferenceMatches)
                .forEach(
                        (preferenceAndType, indexRanges) ->
                                highlight(
                                        preferenceAndType.first,
                                        preferenceAndType.second,
                                        indexRanges));
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

    private void highlight(final Preference preference,
                           final Type type,
                           final List<IndexRange> indexRanges) {
        // FK-TODO: replace switch with inheritance?
        switch (type) {
            case TITLE:
                setTitle(preference, indexRanges);
                break;
            case SUMMARY:
                setSummary(preference, indexRanges);
                break;
            case SEARCHABLE_INFO:
                setSearchableInfo(preference, indexRanges);
                break;
        }
    }

    private void setTitle(final Preference preference, final List<IndexRange> indexRanges) {
        PreferenceTitle.setTitle(
                preference,
                createSpannableFromStrAndApplyMarkupsToIndexRanges(
                        preference.getTitle().toString(),
                        indexRanges));
    }

    private void setSummary(final Preference preference, final List<IndexRange> indexRanges) {
        PreferenceSummary.setSummary(
                preference,
                createSpannableFromStrAndApplyMarkupsToIndexRanges(
                        preference.getSummary().toString(),
                        indexRanges));
    }

    private void setSearchableInfo(final Preference preference, final List<IndexRange> indexRanges) {
        searchableInfoAttribute.setSearchableInfo(
                preference,
                createSpannableFromStrAndApplyMarkupsToIndexRanges(
                        searchableInfoAttribute
                                .getSearchableInfo(preference)
                                .map(CharSequence::toString)
                                .orElse(""),
                        indexRanges));
    }

    private SpannableString createSpannableFromStrAndApplyMarkupsToIndexRanges(
            final String str,
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
