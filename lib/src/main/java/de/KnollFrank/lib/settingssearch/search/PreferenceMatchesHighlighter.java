package de.KnollFrank.lib.settingssearch.search;

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

import de.KnollFrank.lib.settingssearch.search.PreferenceMatch.Type;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

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
                                                preferenceMatch.preference(),
                                                preferenceMatch.type()),
                                mapping(
                                        PreferenceMatch::indexRange,
                                        toList())));
    }

    private void highlight(final Preference preference,
                           final Type type,
                           final List<IndexRange> indexRanges) {
        switch (type) {
            case TITLE:
                highlightTitle(preference, indexRanges);
                break;
            case SUMMARY:
                highlightSummary(preference, indexRanges);
                break;
            case SEARCHABLE_INFO:
                highlightSearchableInfo(preference, indexRanges);
                break;
        }
    }

    private void highlightTitle(final Preference preference, final List<IndexRange> indexRanges) {
        PreferenceTitle.setTitle(
                preference,
                highlight(
                        preference.getTitle().toString(),
                        indexRanges));
    }

    private void highlightSummary(final Preference preference, final List<IndexRange> indexRanges) {
        PreferenceSummary.setSummary(
                preference,
                highlight(
                        preference.getSummary().toString(),
                        indexRanges));
    }

    private void highlightSearchableInfo(final Preference preference, final List<IndexRange> indexRanges) {
        searchableInfoAttribute.setSearchableInfo(
                preference,
                highlight(
                        searchableInfoAttribute
                                .getSearchableInfo(preference)
                                .map(CharSequence::toString)
                                .orElse(""),
                        indexRanges));
    }

    private SpannableString highlight(final String str, final List<IndexRange> indexRanges) {
        final SpannableString spannable = new SpannableString(str);
        highlight(spannable, indexRanges);
        return spannable;
    }

    private void highlight(final SpannableString spannable, final List<IndexRange> indexRanges) {
        for (final IndexRange indexRange : indexRanges) {
            highlight(spannable, indexRange);
        }
    }

    private void highlight(final Spannable spannable, final IndexRange indexRange) {
        for (final Object markup : markupsFactory.get()) {
            spannable.setSpan(
                    markup,
                    indexRange.startIndexInclusive(),
                    indexRange.endIndexExclusive(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
