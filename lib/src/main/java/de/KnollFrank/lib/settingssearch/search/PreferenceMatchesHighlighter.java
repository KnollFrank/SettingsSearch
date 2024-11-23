package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.common.Utils.memoize;

import android.text.Spannable;
import android.text.SpannableString;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PreferenceMatchesHighlighter {

    private final Supplier<List<Object>> markupsFactory;

    public PreferenceMatchesHighlighter(final Supplier<List<Object>> markupsFactory) {
        this.markupsFactory = markupsFactory;
    }

    public void highlight(final Set<PreferenceMatch> preferenceMatches) {
        preferenceMatches.forEach(this::highlight);
    }

    private void highlight(final PreferenceMatch preferenceMatch) {
        highlightTitle(preferenceMatch);
        highlightSummary(preferenceMatch);
        highlightSearchableInfo(preferenceMatch);
    }

    private void highlightTitle(final PreferenceMatch preferenceMatch) {
        highlight(
                preferenceMatch.preference()::setDisplayTitleProvider,
                preferenceMatch.preference()::getTitle,
                preferenceMatch.titleMatches());
    }

    private void highlightSummary(final PreferenceMatch preferenceMatch) {
        highlight(
                preferenceMatch.preference()::setDisplaySummaryProvider,
                preferenceMatch.preference()::getSummary,
                preferenceMatch.summaryMatches());
    }

    private void highlightSearchableInfo(final PreferenceMatch preferenceMatch) {
        highlight(
                preferenceMatch.preference()::setDisplaySearchableInfoProvider,
                preferenceMatch.preference()::getSearchableInfo,
                preferenceMatch.searchableInfoMatches());
    }

    private void highlight(
            final Consumer<Supplier<Optional<CharSequence>>> setDisplayProvider,
            final Supplier<Optional<String>> strSupplier,
            final Collection<IndexRange> indexRanges) {
        setDisplayProvider.accept(memoize(() -> strSupplier.get().map(str -> highlight(str, indexRanges))));
    }

    private SpannableString highlight(final String str, final Collection<IndexRange> indexRanges) {
        final SpannableString spannable = new SpannableString(str);
        highlight(spannable, indexRanges);
        return spannable;
    }

    private void highlight(final SpannableString spannable, final Collection<IndexRange> indexRanges) {
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
