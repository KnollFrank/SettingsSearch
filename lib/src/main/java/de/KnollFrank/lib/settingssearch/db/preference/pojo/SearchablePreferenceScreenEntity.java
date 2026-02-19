package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.LanguageCode;

@Entity
public record SearchablePreferenceScreenEntity(@PrimaryKey @NonNull String id,
                                               @Embedded(prefix = "host_") PreferenceFragmentClassOfActivitySurrogate host,
                                               Optional<String> title,
                                               Optional<String> summary,
                                               LanguageCode graphId) {

    public interface DbDataProvider {

        Set<SearchablePreferenceEntity> getAllPreferencesOfPreferenceHierarchy(SearchablePreferenceScreenEntity screen);

        SearchablePreferenceScreenEntity getHost(SearchablePreferenceEntity preference);
    }

    public Set<SearchablePreferenceEntity> getAllPreferencesOfPreferenceHierarchy(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getAllPreferencesOfPreferenceHierarchy(this);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        final SearchablePreferenceScreenEntity that = (SearchablePreferenceScreenEntity) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
