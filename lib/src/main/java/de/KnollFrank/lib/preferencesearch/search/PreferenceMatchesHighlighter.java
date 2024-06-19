package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.matcher.PreferenceAttributes.getSummary;
import static de.KnollFrank.lib.preferencesearch.search.matcher.PreferenceAttributes.getTitle;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.matcher.PreferenceMatch;

class PreferenceMatchesHighlighter {

    public static void highlight(final List<PreferenceMatch> preferenceMatches,
                                 final PreferenceScreen preferenceScreen) {
        unhighlight(preferenceScreen);
        highlight(preferenceMatches);
    }

    private static void unhighlight(final PreferenceScreen preferenceScreen) {
        Preferences
                .getAllPreferences(preferenceScreen)
                .forEach(PreferenceMatchesHighlighter::unhighlight);
    }

    private static void unhighlight(final Preference preference) {
        setTitle(preference, getTitle(preference).orElse(null));
        setSummary(preference, getSummary(preference).orElse(null));
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
        setTitle(
                preferenceMatch.preference,
                createSpannable(
                        preferenceMatch,
                        preferenceMatch.preference.getTitle().toString()));
    }

    private static void setSummary(final PreferenceMatch preferenceMatch) {
        setSummary(
                preferenceMatch.preference,
                createSpannable(
                        preferenceMatch,
                        preferenceMatch.preference.getSummary().toString()));
    }

    private static Spannable createSpannable(final PreferenceMatch preferenceMatch, final String str) {
        final SpannableString spannable = new SpannableString(str);
        spannable.setSpan(
                new BackgroundColorSpan(Color.GREEN),
                preferenceMatch.indexRange.startIndexInclusive,
                preferenceMatch.indexRange.endIndexExclusive,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private static void setTitle(final Preference preference, final CharSequence title) {
        preference.setTitle(null);
        preference.setTitle(title);
    }

    private static void setSummary(final Preference preference, final CharSequence summary) {
        preference.setSummary(null);
        preference.setSummary(summary);
    }
}
