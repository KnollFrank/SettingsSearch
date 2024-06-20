package de.KnollFrank.lib.preferencesearch.search;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;

import androidx.preference.PreferenceScreen;

import java.util.List;

class PreferenceMatchesHighlighter {

    public static void highlight(final List<PreferenceMatch> preferenceMatches,
                                 final PreferenceScreen preferenceScreen) {
        PreferenceScreenUnhighlighter.unhighlight(preferenceScreen);
        highlight(preferenceMatches);
    }

    private static void highlight(final List<PreferenceMatch> preferenceMatches) {
        preferenceMatches.forEach(PreferenceMatchesHighlighter::highlight);
    }

    private static void highlight(final PreferenceMatch preferenceMatch) {
        switch (preferenceMatch.type) {
            case TITLE: {
                setTitle(preferenceMatch);
                break;
            }
            case SUMMARY: {
                setSummary(preferenceMatch);
                break;
            }
        }
    }

    private static void setTitle(final PreferenceMatch preferenceMatch) {
        PreferenceAttributes.setTitle(
                preferenceMatch.preference,
                createSpannable(
                        preferenceMatch,
                        preferenceMatch.preference.getTitle().toString()));
    }

    private static void setSummary(final PreferenceMatch preferenceMatch) {
        PreferenceAttributes.setSummary(
                preferenceMatch.preference,
                createSpannable(
                        preferenceMatch,
                        preferenceMatch.preference.getSummary().toString()));
    }

    private static Spannable createSpannable(final PreferenceMatch preferenceMatch, final String str) {
        final SpannableString spannable = new SpannableString(str);
        // FK-TODO: markiere den Text mit blau anstatt den Hintergrund des Textes mit grün, analog zu den Android Settings. Oder mache das ganze am besten konfigurierbar.
        spannable.setSpan(
                new BackgroundColorSpan(Color.GREEN),
                preferenceMatch.indexRange.startIndexInclusive,
                preferenceMatch.indexRange.endIndexExclusive,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
}
