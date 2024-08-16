package de.KnollFrank.settingssearch.preference.custom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;

import java.util.Arrays;

public class ReversedListPreference extends ListPreference {

    private boolean summarySetterEnabled = false;

    public ReversedListPreference(@NonNull final Context context) {
        super(context);
    }

    public ReversedListPreference(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        setEntries(getEntries());
    }

    @Override
    public void setEntries(final CharSequence[] entries) {
        super.setEntries(reverse(entries));
    }

    public void setSummarySetterEnabled(final boolean summarySetterEnabled) {
        this.summarySetterEnabled = summarySetterEnabled;
    }

    public boolean isSummarySetterEnabled() {
        return summarySetterEnabled;
    }

    @Override
    public void setSummary(@Nullable final CharSequence summary) {
        if (isSummarySetterEnabled()) {
            super.setSummary(summary);
        }
    }

    @Nullable
    @Override
    public CharSequence getSummary() {
        return super.getSummary();
    }

    private static CharSequence[] reverse(final CharSequence[] charSequences) {
        return Arrays
                .stream(charSequences)
                .map(ReversedListPreference::getReverse)
                .toArray(CharSequence[]::new);
    }

    public static CharSequence getReverse(final CharSequence charSequence) {
        return TextUtils.getReverse(charSequence, 0, charSequence.length());
    }
}
