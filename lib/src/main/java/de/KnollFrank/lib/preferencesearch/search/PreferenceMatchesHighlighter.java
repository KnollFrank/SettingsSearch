package de.KnollFrank.lib.preferencesearch.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.TextAppearanceSpan;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.R;

class PreferenceMatchesHighlighter {

    public static void highlight(final List<PreferenceMatch> preferenceMatches,
                                  final Map<Preference, Optional<CharSequence>> summaryByPreference,
                                  final Context context) {
        final List<Object> markups = createMarkups(context);
        for (final PreferenceMatch preferenceMatch : preferenceMatches) {
            highlight(preferenceMatch, markups, summaryByPreference);
        }
    }

    private static void highlight(final PreferenceMatch preferenceMatch,
                                  final List<Object> markups,
                                  final Map<Preference, Optional<CharSequence>> summaryByPreference) {
        switch (preferenceMatch.type) {
            case TITLE:
                setTitle(preferenceMatch, markups);
                break;
            case SUMMARY:
                setSummary(preferenceMatch, markups);
                break;
            case ENTRY: {
                if (preferenceMatch.preference instanceof final ListPreference preference) {
                    final String entries = String.join(", ", preference.getEntries());
                    final Optional<CharSequence> summary = summaryByPreference.get(preference);
                    final String newSummary =
                            summary
                                    .map(_summary -> String.format("%s\n%s", _summary, entries))
                                    .orElse(entries);
                    PreferenceAttributes.setSummary(preferenceMatch.preference, newSummary);
                    break;
                }
            }
        }
    }

    private static void setTitle(final PreferenceMatch preferenceMatch, final List<Object> markups) {
        PreferenceAttributes.setTitle(
                preferenceMatch.preference,
                createSpannableFromStrAndApplyMarkupsToIndexRange(
                        preferenceMatch.preference.getTitle().toString(),
                        markups,
                        preferenceMatch.indexRange));
    }

    private static void setSummary(final PreferenceMatch preferenceMatch, final List<Object> markups) {
        PreferenceAttributes.setSummary(
                preferenceMatch.preference,
                createSpannableFromStrAndApplyMarkupsToIndexRange(
                        preferenceMatch.preference.getSummary().toString(),
                        markups,
                        preferenceMatch.indexRange));
    }

    private static Spannable createSpannableFromStrAndApplyMarkupsToIndexRange(
            final String str,
            final List<Object> markups,
            final IndexRange indexRange) {
        final SpannableString spannable = new SpannableString(str);
        applyMarkupsToIndexRange(spannable, markups, indexRange);
        return spannable;
    }

    private static void applyMarkupsToIndexRange(final SpannableString spannable,
                                                 final List<Object> markups,
                                                 final IndexRange indexRange) {
        for (final Object markup : markups) {
            spannable.setSpan(
                    markup,
                    indexRange.startIndexInclusive,
                    indexRange.endIndexExclusive,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private static List<Object> createMarkups(final Context context) {
        final ImmutableList.Builder<Object> markupsBuilder = ImmutableList.builder();
        markupsBuilder.add(new TextAppearanceSpan(context, R.style.SearchPreferenceResultTextAppearance));
        PreferenceMatchesHighlighter
                .getBackgroundColor(context)
                .map(BackgroundColorSpan::new)
                .ifPresent(markupsBuilder::add);
        return markupsBuilder.build();
    }

    private static Optional<Integer> getBackgroundColor(final Context context) {
        try (final TypedArray typedArray = context.obtainStyledAttributes(R.style.SearchPreferenceResultBackgroundColor, R.styleable.SearchPreferenceResultBackgroundColor)) {
            final int backgroundColorAttr = R.styleable.SearchPreferenceResultBackgroundColor_backgroundColor;
            return typedArray.hasValue(backgroundColorAttr) ?
                    Optional.of(typedArray.getColor(backgroundColorAttr, Color.GREEN)) :
                    Optional.empty();
        }
    }
}
