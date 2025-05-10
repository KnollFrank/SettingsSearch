package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import de.KnollFrank.lib.settingssearch.db.preference.dao.AllPreferencesProvider;

@Entity
public final class SearchablePreferenceScreen {

    @Ignore
    private Optional<AllPreferencesProvider> dao = Optional.empty();

    @PrimaryKey
    private final int id;
    @Embedded
    private final HostWithArguments hostWithArguments;
    // FK-TODO: make title Optional<String>
    private final String title;
    // FK-TODO: make summary Optional<String>
    private final String summary;
    @Ignore
    private final Optional<Set<SearchablePreference>> allPreferences;
    private final Optional<Integer> parentId;

    public SearchablePreferenceScreen(final int id,
                                      final HostWithArguments hostWithArguments,
                                      final String title,
                                      final String summary,
                                      final Set<SearchablePreference> allPreferences,
                                      final Optional<Integer> parentId) {
        this(id, hostWithArguments, title, summary, Optional.of(allPreferences), parentId);
    }

    public SearchablePreferenceScreen(final int id,
                                      final HostWithArguments hostWithArguments,
                                      final String title,
                                      final String summary,
                                      final Optional<Integer> parentId) {
        this(id, hostWithArguments, title, summary, Optional.empty(), parentId);
    }

    private SearchablePreferenceScreen(final int id,
                                       final HostWithArguments hostWithArguments,
                                       final String title,
                                       final String summary,
                                       final Optional<Set<SearchablePreference>> allPreferences,
                                       final Optional<Integer> parentId) {
        this.id = id;
        this.hostWithArguments = hostWithArguments;
        this.parentId = parentId;
        this.title = title;
        this.summary = summary;
        this.allPreferences = allPreferences;
    }

    public void setDao(final AllPreferencesProvider dao) {
        this.dao = Optional.of(dao);
    }

    public int getId() {
        return id;
    }

    public HostWithArguments getHostWithArguments() {
        return hostWithArguments;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Optional<Integer> getParentId() {
        return parentId;
    }

    public Set<SearchablePreference> getAllPreferences() {
        return allPreferences.orElseGet(this::getAllPreferencesFromDao);
    }

    private Set<SearchablePreference> getAllPreferencesFromDao() {
        return dao
                .orElseThrow()
                .getAllPreferencesBySearchablePreferenceScreen()
                .get(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreferenceScreen that = (SearchablePreferenceScreen) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SearchablePreferenceScreen.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("hostWithArguments=" + hostWithArguments)
                .add("title='" + title + "'")
                .add("summary='" + summary + "'")
                .add("parentId=" + parentId)
                .toString();
    }
}
