package de.KnollFrank.preferencesearch;

import android.content.Context;
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
        super.setEntries(reverse(toStrings(entries)));
    }

    private static String[] toStrings(final CharSequence[] charSequences) {
        return Arrays
                .stream(charSequences)
                .map(CharSequence::toString)
                .toArray(String[]::new);
    }

    private static String[] reverse(final String[] strings) {
        return Arrays
                .stream(strings)
                .map(ReversedListPreference::reverse)
                .toArray(String[]::new);
    }

    public static String reverse(final String string) {
        return new StringBuilder(string).reverse().toString();
    }
}
