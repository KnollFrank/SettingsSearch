package de.KnollFrank.preferencesearch;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;

public class ConstantListPreference extends ListPreference {

    public ConstantListPreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setEntriesAndEntryValues();
    }

    public ConstantListPreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setEntriesAndEntryValues();
    }

    public ConstantListPreference(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        setEntriesAndEntryValues();
    }

    public ConstantListPreference(@NonNull final Context context) {
        super(context);
        setEntriesAndEntryValues();
    }

    public void setEntriesAndEntryValues() {
        final CharSequence[] entries = {"first entry", "second entry"};
        super.setEntries(entries);
        super.setEntryValues(entries);
    }
}
