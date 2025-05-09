package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.dao.AllPreferencesBySearchablePreferenceScreenProvider;

@Entity
public final class SearchablePreferenceScreen {

    @Ignore
    private Optional<AllPreferencesBySearchablePreferenceScreenProvider> dao = Optional.empty();

    @PrimaryKey
    private final int id;
    private final String title;
    private final String summary;
    @Ignore
    private final List<SearchablePreference> firstLevelPreferences;
    @Ignore
    private final Optional<Set<SearchablePreference>> allPreferences;

    public SearchablePreferenceScreen(final int id,
                                      final String title,
                                      final String summary,
                                      final List<SearchablePreference> firstLevelPreferences,
                                      final Set<SearchablePreference> allPreferences) {
        this(id, title, summary, firstLevelPreferences, Optional.of(allPreferences));
    }

    public SearchablePreferenceScreen(final int id,
                                      final String title,
                                      final String summary,
                                      final List<SearchablePreference> firstLevelPreferences) {
        this(id, title, summary, firstLevelPreferences, Optional.empty());
    }

    public SearchablePreferenceScreen(final int id,
                                      final String title,
                                      final String summary) {
        this(id, title, summary, List.of(), Optional.empty());
    }

    private SearchablePreferenceScreen(final int id,
                                       final String title,
                                       final String summary,
                                       final List<SearchablePreference> firstLevelPreferences,
                                       final Optional<Set<SearchablePreference>> allPreferences) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.firstLevelPreferences = firstLevelPreferences;
        this.allPreferences = allPreferences;
    }

    public void setDao(final AllPreferencesBySearchablePreferenceScreenProvider dao) {
        this.dao = Optional.of(dao);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public List<SearchablePreference> getFirstLevelPreferences() {
        return firstLevelPreferences;
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
        return "SearchablePreferenceScreen{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", firstLevelPreferences=" + firstLevelPreferences +
                '}';
    }
}
