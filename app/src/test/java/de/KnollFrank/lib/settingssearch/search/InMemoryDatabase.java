package de.KnollFrank.lib.settingssearch.search;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.db.Database;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class InMemoryDatabase implements Database {

    private Optional<Set<SearchablePreference>> preferences = Optional.empty();

    @Override
    public void persist(final Set<SearchablePreference> preferences) {
        this.preferences = Optional.of(preferences);
    }

    @Override
    public void updateSummary(final int idOfPreference, final String newSummaryOfPreference) {
        getPreferenceById(idOfPreference).setSummary(newSummaryOfPreference);
    }

    @Override
    public Set<SearchablePreference> loadAll() {
        return preferences.orElseThrow();
    }

    @Override
    public boolean isInitialized() {
        return preferences.isPresent();
    }

    private SearchablePreference getPreferenceById(final int id) {
        return SearchablePreferences.findUniquePreferenceRecursivelyByPredicate(
                loadAll(),
                preference -> preference.getId() == id);
    }
}
