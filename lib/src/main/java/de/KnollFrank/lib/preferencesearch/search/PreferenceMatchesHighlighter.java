package de.KnollFrank.lib.preferencesearch.search;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;

import androidx.preference.PreferenceScreen;

import java.util.List;

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
        spannable.setSpan(
                new TextAppearanceSpan(context, R.style.SearchPreferenceResultTextAppearance),
                preferenceMatch.indexRange.startIndexInclusive,
                preferenceMatch.indexRange.endIndexExclusive,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
}
