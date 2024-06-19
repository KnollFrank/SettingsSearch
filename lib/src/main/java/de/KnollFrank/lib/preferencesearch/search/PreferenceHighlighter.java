package de.KnollFrank.lib.preferencesearch.search;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;

import androidx.preference.Preference;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.search.matcher.PreferenceMatch;

class PreferenceHighlighter {

    public static void highlight(final List<PreferenceMatch> preferenceMatches) {
        preferenceMatches.forEach(PreferenceHighlighter::highlight);
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

    private static Spannable createSpannable(final PreferenceMatch preferenceMatch,
                                             final String str) {
        final SpannableString spannable = new SpannableString(str);
        spannable.setSpan(
                new BackgroundColorSpan(Color.GREEN),
                preferenceMatch.startInclusive,
                preferenceMatch.endExclusive,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private static void setTitle(final Preference preference, final Spannable title) {
        preference.setTitle(null);
        preference.setTitle(title);
    }

    private static void setSummary(final Preference preference, final Spannable summary) {
        preference.setSummary(null);
        preference.setSummary(summary);
    }
}
