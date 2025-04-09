package de.KnollFrank.lib.settingssearch.search;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.Database;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class InMemoryDatabase implements Database {

    private Optional<Set<SearchablePreference>> preferences = Optional.empty();

    @Override
    public void persist(final Set<SearchablePreference> preferences) {
        this.preferences = Optional.of(preferences);
    }

    @Override
    public Set<SearchablePreference> loadAll() {
        return preferences.orElseThrow();
    }

    @Override
    public boolean isInitialized() {
        return preferences.isPresent();
    }
}
