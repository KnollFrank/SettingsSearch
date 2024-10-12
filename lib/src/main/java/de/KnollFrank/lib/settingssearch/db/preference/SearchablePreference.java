package de.KnollFrank.lib.settingssearch.db.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceCategory;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchablePreference extends PreferenceCategory {

    private final Optional<String> searchableInfo;
    private final Optional<SearchablePreferencePOJO> origin;

    public SearchablePreference(@NonNull final Context context,
                                @Nullable final AttributeSet attrs,
                                final Optional<String> searchableInfo,
                                final Optional<SearchablePreferencePOJO> origin) {
        super(context, attrs);
        this.searchableInfo = searchableInfo;
        this.origin = origin;
    }

    public SearchablePreference(@NonNull final Context context,
                                final Optional<String> searchableInfo,
                                final Optional<SearchablePreferencePOJO> origin) {
        this(context, null, searchableInfo, origin);
    }

    public Optional<String> getSearchableInfo() {
        return searchableInfo;
    }

    public Optional<SearchablePreferencePOJO> getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        return "SearchablePreference{" +
                "searchableInfo=" + searchableInfo +
                ", origin=" + origin +
                "} " + super.toString();
    }
}
