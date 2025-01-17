package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public record PreferencePath(List<SearchablePreference> preferences) {

    public Optional<SearchablePreference> getPreference() {
        return Lists.getLastElement(preferences);
    }

    public PreferencePath append(final SearchablePreference preference) {
        if (preference.getKey().isEmpty()) {
            throw new IllegalArgumentException("preference must have a key: " + preference);
        }
        return new PreferencePath(
                ImmutableList
                        .<SearchablePreference>builder()
                        .addAll(preferences)
                        .add(preference)
                        .build());
    }
}
