package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferenceFragmentClassOfActivity;

public record SearchablePreferenceScreen(String id,
                                         PreferenceFragmentClassOfActivity host,
                                         Optional<String> title,
                                         Optional<String> summary,
                                         Set<SearchablePreference> allPreferencesOfPreferenceHierarchy) {

    @Override
    public boolean equals(final Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        final SearchablePreferenceScreen that = (SearchablePreferenceScreen) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
