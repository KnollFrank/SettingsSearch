package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;
import java.util.Set;

@Entity
public record SearchablePreferenceScreenGraphEntity(@PrimaryKey @NonNull Locale id) {

    public interface DbDataProvider {

        Set<SearchablePreferenceScreenEntity> getNodes(SearchablePreferenceScreenGraphEntity graph);
    }

    public Set<SearchablePreferenceScreenEntity> getNodes(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getNodes(this);
    }
}
