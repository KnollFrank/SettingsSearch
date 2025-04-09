package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

// FK-TODO: move to test folder
public class InMemoryDatabase implements Database {

    private Optional<Set<SearchablePreference>> preferences = Optional.empty();

    @Override
    public void persist(final Set<SearchablePreference> preferences) {
        this.preferences = Optional.of(preferences);
    }

    @Override
    public Set<SearchablePreference> load() {
        return preferences.orElseThrow();
    }

    @Override
    public boolean isInitialized() {
        return preferences.isPresent();
    }
}
