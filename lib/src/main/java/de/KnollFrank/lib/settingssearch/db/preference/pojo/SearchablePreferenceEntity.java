package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.codepoetics.ambivalence.Either;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
public record SearchablePreferenceEntity(@PrimaryKey @NonNull String id,
                                         String key,
                                         Optional<String> title,
                                         Optional<String> summary,
                                         Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
                                         @LayoutRes int layoutResId,
                                         @LayoutRes int widgetLayoutResId,
                                         Optional<String> fragment,
                                         Optional<String> classNameOfReferencedActivity,
                                         boolean visible,
                                         LazyPersistableBundle extras,
                                         Optional<String> searchableInfo,
                                         Optional<String> parentId,
                                         Optional<String> predecessorId,
                                         String searchablePreferenceScreenId) {

    public interface DbDataProvider {

        Set<SearchablePreferenceEntity> getChildren(SearchablePreferenceEntity preference);

        Optional<SearchablePreferenceEntity> getPredecessor(SearchablePreferenceEntity preference);

        SearchablePreferenceScreenEntity getHost(SearchablePreferenceEntity preference);
    }

    public SearchablePreferenceEntity {
        Objects.requireNonNull(key);
    }

    public Set<SearchablePreferenceEntity> getChildren(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getChildren(this);
    }

    public Optional<SearchablePreferenceEntity> getPredecessor(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getPredecessor(this);
    }

    public SearchablePreferenceScreenEntity getHost(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getHost(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreferenceEntity that = (SearchablePreferenceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
