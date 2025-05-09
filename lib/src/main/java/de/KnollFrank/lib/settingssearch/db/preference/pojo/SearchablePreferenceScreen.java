package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.dao.AllPreferencesAndChildrenProvider;

@Entity
public final class SearchablePreferenceScreen {

    @Ignore
    private Optional<AllPreferencesAndChildrenProvider> dao = Optional.empty();

    @PrimaryKey
    private final int id;
    private final Optional<Integer> parentId;
    private final String title;
    private final String summary;
    @Ignore
    private final List<SearchablePreference> firstLevelPreferences;
    @Ignore
    private final Optional<Set<SearchablePreference>> allPreferences;

    public SearchablePreferenceScreen(final int id,
                                      final Optional<Integer> parentId,
                                      final String title,
                                      final String summary,
                                      final List<SearchablePreference> firstLevelPreferences,
                                      final Set<SearchablePreference> allPreferences) {
        this(id, parentId, title, summary, firstLevelPreferences, Optional.of(allPreferences));
    }

    public SearchablePreferenceScreen(final int id,
                                      final Optional<Integer> parentId,
                                      final String title,
                                      final String summary,
                                      final List<SearchablePreference> firstLevelPreferences) {
        this(id, parentId, title, summary, firstLevelPreferences, Optional.empty());
    }

    public SearchablePreferenceScreen(final int id,
                                      final Optional<Integer> parentId,
                                      final String title,
                                      final String summary) {
        this(id, parentId, title, summary, List.of(), Optional.empty());
    }

    private SearchablePreferenceScreen(final int id,
                                       final Optional<Integer> parentId,
                                       final String title,
                                       final String summary,
                                       final List<SearchablePreference> firstLevelPreferences,
                                       final Optional<Set<SearchablePreference>> allPreferences) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.summary = summary;
        this.firstLevelPreferences = firstLevelPreferences;
        this.allPreferences = allPreferences;
    }

    public void setDao(final AllPreferencesAndChildrenProvider dao) {
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

    public Optional<Integer> getParentId() {
        return parentId;
    }

    public List<SearchablePreference> getFirstLevelPreferences() {
        return firstLevelPreferences;
    }

    public Set<SearchablePreference> getAllPreferences() {
        return allPreferences.orElseGet(this::getAllPreferencesFromDao);
    }

    // FK-TODO: return Set instead of List
    public List<SearchablePreferenceScreen> getChildren() {
        return dao
                .orElseThrow()
                .getChildrenBySearchablePreferenceScreen()
                .get(this);
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
                ", parentId=" + parentId +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", firstLevelPreferences=" + firstLevelPreferences +
                '}';
    }
}
