package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public record SearchablePreferenceScreen(int id,
                                         String title,
                                         String summary,
                                         List<SearchablePreference> firstLevelPreferences,
                                         Set<SearchablePreference> allPreferences) {
    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreferenceScreen that = (SearchablePreferenceScreen) o;
        return id() == that.id();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id());
    }
}
