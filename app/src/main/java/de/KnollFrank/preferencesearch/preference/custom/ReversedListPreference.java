package de.KnollFrank.preferencesearch.preference.custom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;

import java.util.Arrays;

public class ReversedListPreference extends ListPreference {

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
