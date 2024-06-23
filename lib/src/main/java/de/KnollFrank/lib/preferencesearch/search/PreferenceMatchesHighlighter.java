package de.KnollFrank.lib.preferencesearch.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.TextAppearanceSpan;

import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.R;

class PreferenceMatchesHighlighter {

    public static void highlight(final List<PreferenceMatch> preferenceMatches,
                                 final PreferenceScreen preferenceScreen,
                                 final Context context) {
        PreferenceScreenUnhighlighter.unhighlight(preferenceScreen);
        highlight(preferenceMatches, context);
    }

    private static void highlight(final List<PreferenceMatch> preferenceMatches, final Context context) {
        preferenceMatches.forEach(preferenceMatch -> highlight(preferenceMatch, context));
    }

    private static void highlight(final PreferenceMatch preferenceMatch, final Context context) {
        switch (preferenceMatch.type) {
            case TITLE: {
                setTitle(preferenceMatch, context);
                break;
            }
            case SUMMARY: {
                setSummary(preferenceMatch, context);
                break;
            }
        }
    }

    private static void setTitle(final PreferenceMatch preferenceMatch, final Context context) {
        PreferenceAttributes.setTitle(
                preferenceMatch.preference,
                createSpannable(
                        preferenceMatch,
                        preferenceMatch.preference.getTitle().toString(),
                        context));
    }

    private static void setSummary(final PreferenceMatch preferenceMatch, final Context context) {
        PreferenceAttributes.setSummary(
                preferenceMatch.preference,
                createSpannable(
                        preferenceMatch,
                        preferenceMatch.preference.getSummary().toString(),
                        context));
    }

    private static Spannable createSpannable(final PreferenceMatch preferenceMatch,
                                             final String str,
                                             final Context context) {
        final SpannableString spannable = new SpannableString(str);
        // FK-TODO: make List<Object> markups a parameter
        PreferenceMatchesHighlighter
                .getMarkups(context)
                .forEach(
                        markup ->
                                spannable.setSpan(
                                        markup,
                                        preferenceMatch.indexRange.startIndexInclusive,
                                        preferenceMatch.indexRange.endIndexExclusive,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE));
        return spannable;
    }

    private static List<Object> getMarkups(final Context context) {
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
