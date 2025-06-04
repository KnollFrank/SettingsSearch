package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

@Entity
public final class SearchablePreferenceScreenEntity {

    public interface DbDataProvider {

        Set<SearchablePreferenceEntity> getAllPreferences(SearchablePreferenceScreenEntity screen);

        SearchablePreferenceScreenEntity getHost(SearchablePreferenceEntity preference);
    }

    @PrimaryKey
    @NonNull
    private final String id;
    private final Class<? extends PreferenceFragmentCompat> host;
    private final Optional<String> title;
    private final Optional<String> summary;
    @Ignore
    private final Optional<Set<SearchablePreferenceEntity>> allPreferences;
    private final Optional<String> parentId;

    public SearchablePreferenceScreenEntity(final String id,
                                            final Class<? extends PreferenceFragmentCompat> host,
                                            final Optional<String> title,
                                            final Optional<String> summary,
                                            final Set<SearchablePreferenceEntity> allPreferences,
                                            final Optional<String> parentId) {
        this(id, host, title, summary, Optional.of(allPreferences), parentId);
    }

    public SearchablePreferenceScreenEntity(final String id,
                                            final Class<? extends PreferenceFragmentCompat> host,
                                            final Optional<String> title,
                                            final Optional<String> summary,
                                            final Optional<String> parentId) {
        this(id, host, title, summary, Optional.empty(), parentId);
    }

    public String getId() {
        return id;
    }

    public Class<? extends PreferenceFragmentCompat> getHost() {
        return host;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getSummary() {
        return summary;
    }

    public Optional<String> getParentId() {
        return parentId;
    }

    public Set<SearchablePreferenceEntity> getAllPreferences(final DbDataProvider dbDataProvider) {
        return allPreferences.orElseGet(() -> dbDataProvider.getAllPreferences(this));
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

    @Override
    public String toString() {
        return new StringJoiner(", ", SearchablePreferenceScreenEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("host=" + host)
                .add("title='" + title + "'")
                .add("summary='" + summary + "'")
                .add("parentId=" + parentId)
                .toString();
    }

    private SearchablePreferenceScreenEntity(final String id,
                                             final Class<? extends PreferenceFragmentCompat> host,
                                             final Optional<String> title,
                                             final Optional<String> summary,
                                             final Optional<Set<SearchablePreferenceEntity>> allPreferences,
                                             final Optional<String> parentId) {
        this.id = id;
        this.host = host;
        this.parentId = parentId;
        this.title = title;
        this.summary = summary;
        this.allPreferences = allPreferences;
    }
}
