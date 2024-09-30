package de.KnollFrank.lib.settingssearch.db.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceCategory;

import java.util.Optional;

public class SearchablePreference extends PreferenceCategory {

    private final Optional<String> searchableInfo;

    public SearchablePreference(@NonNull final Context context,
                                @Nullable final AttributeSet attrs,
                                final Optional<String> searchableInfo) {
        super(context, attrs);
        this.searchableInfo = searchableInfo;
    }

    public SearchablePreference(@NonNull final Context context,
                                final Optional<String> searchableInfo) {
        this(context, null, searchableInfo);
    }

    public Optional<String> getSearchableInfo() {
        return searchableInfo;
    }
}
