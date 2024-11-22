package de.KnollFrank.lib.settingssearch.search;

import static java.util.stream.Collectors.toList;
import static de.KnollFrank.lib.settingssearch.common.Utils.memoize;

import android.text.Spannable;
import android.text.SpannableString;
import android.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch.Type;

public class PreferenceMatchesHighlighter {

    private final Supplier<List<Object>> markupsFactory;

    public PreferenceMatchesHighlighter(final Supplier<List<Object>> markupsFactory) {
        this.markupsFactory = markupsFactory;
    }

    public void highlight(final List<PreferenceMatch> preferenceMatches) {
        this
                .getIndexRangesByPreferenceAndType(preferenceMatches)
                .forEach(
                        (preferenceAndType, indexRanges) ->
                                highlight(
                                        preferenceAndType.first,
                                        preferenceAndType.second,
                                        indexRanges));
    }

    private Map<Pair<SearchablePreferencePOJO, Type>, List<IndexRange>> getIndexRangesByPreferenceAndType(
            final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .collect(
                        Collectors.groupingBy(
                                preferenceMatch ->
                                        Pair.create(
                                                preferenceMatch.preference(),
                                                preferenceMatch.type()),
                                Collectors.mapping(
                                        PreferenceMatch::indexRange,
                                        toList())));
    }

    private void highlight(final SearchablePreferencePOJO preference,
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

    // FK-TODO: DRY highlightTitle(), highlightSummary(), highlightSearchableInfo()
    private void highlightTitle(final SearchablePreferencePOJO preference, final List<IndexRange> indexRanges) {
        preference.setDisplayTitleProvider(
                memoize(
                        () -> Optional.of(
                                highlight(
                                        preference.title().orElse(""),
                                        indexRanges))));
    }

    private void highlightSummary(final SearchablePreferencePOJO preference, final List<IndexRange> indexRanges) {
        preference.setDisplaySummaryProvider(
                memoize(
                        () -> Optional.of(
                                highlight(
                                        preference.summary().orElse(""),
                                        indexRanges))));
    }

    private void highlightSearchableInfo(final SearchablePreferencePOJO preference, final List<IndexRange> indexRanges) {
        preference.setDisplaySearchableInfo(
                Optional.of(
                        highlight(
                                preference.searchableInfo().orElse(""),
                                indexRanges)));
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
