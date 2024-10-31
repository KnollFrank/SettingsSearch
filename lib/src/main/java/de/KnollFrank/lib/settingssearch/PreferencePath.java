package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public record PreferencePath(List<SearchablePreferencePOJO> preferences) {

    public Optional<SearchablePreferencePOJO> getPreference() {
        return Lists.getLastElement(preferences);
    }

    public PreferencePath append(final SearchablePreferencePOJO preference) {
        return new PreferencePath(
                ImmutableList
                        .<SearchablePreferencePOJO>builder()
                        .addAll(preferences)
                        .add(preference)
                        .build());
    }
}
