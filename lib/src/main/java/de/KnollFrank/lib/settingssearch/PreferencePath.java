package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public record PreferencePath(List<SearchablePreference> preferences) {

    // FK-TODO: make non-Optional?
    public Optional<SearchablePreference> getStart() {
        return Lists.getElementAtIndex(preferences, 0);
    }

    // FK-TODO: make non-Optional?
    public Optional<SearchablePreference> getEnd() {
        return Lists.getLastElement(preferences);
    }

    public Optional<PreferencePath> getNonEmptyTail() {
        return preferences.size() > 1 ?
                Optional.of(new PreferencePath(preferences.subList(1, preferences.size()))) :
                Optional.empty();
    }

    public PreferencePath append(final SearchablePreference preference) {
        return new PreferencePath(
                ImmutableList
                        .<SearchablePreference>builder()
                        .addAll(preferences)
                        .add(preference)
                        .build());
    }
}
