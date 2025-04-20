package de.KnollFrank.lib.settingssearch.db.preference.db.file;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class PredecessorSetter {

    private final Map<SearchablePreference, Optional<SearchablePreference>> predecessorByPreference;

    public PredecessorSetter(final Map<SearchablePreference, Optional<SearchablePreference>> predecessorByPreference) {
        this.predecessorByPreference = predecessorByPreference;
    }

    public void setPredecessors(final Set<SearchablePreference> preferences) {
        preferences.forEach(this::setPredecessorIncludingChildren);
    }

    private void setPredecessorIncludingChildren(final SearchablePreference preference) {
        SearchablePreferences
                .getPreferencesRecursively(preference)
                .forEach(_preference -> setPredecessor(_preference, predecessorByPreference.get(_preference)));
    }

    private void setPredecessor(final SearchablePreference preference,
                                final Optional<SearchablePreference> predecessor) {
        try {
            FieldUtils.writeField(
                    SearchablePreference.class.getDeclaredField("predecessor"),
                    preference,
                    predecessor,
                    true);
        } catch (final IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
