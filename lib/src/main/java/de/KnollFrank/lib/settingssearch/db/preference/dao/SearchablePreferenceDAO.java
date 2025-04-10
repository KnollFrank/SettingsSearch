package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.search.PreferenceMatcher.getPreferenceMatch;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.db.Database;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;

public class SearchablePreferenceDAO {

    private final Database database;

    public SearchablePreferenceDAO(final Database database) {
        this.database = database;
    }

    public boolean isDatabaseInitialized() {
        return database.isInitialized();
    }

    public void persist(final Set<SearchablePreference> preferences) {
        database.persist(preferences);
    }

    public void updateSummary(final int idOfPreference, final String newSummaryOfPreference) {
        database.updateSummary(idOfPreference, newSummaryOfPreference);
    }

    public Set<PreferenceMatch> searchFor(final String needle,
                                          final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        return SearchablePreferences
                .getPreferencesRecursively(database.loadAll())
                .stream()
                .filter(includePreferenceInSearchResultsPredicate::includePreferenceInSearchResults)
                .map(searchablePreference -> getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::streamOfPresentElements)
                .collect(Collectors.toSet());
    }

    public SearchablePreference getPreferenceById(final int id) {
        return findPreferenceRecursively(preference -> preference.getId() == id);
    }

    public SearchablePreference getPreferenceByKeyAndHost(final String key,
                                                          final Class<? extends PreferenceFragmentCompat> host) {
        return findPreferenceRecursively(preference -> preference.getKey().equals(key) && preference.getHost().equals(host));
    }

    private SearchablePreference findPreferenceRecursively(final Predicate<SearchablePreference> predicate) {
        return SearchablePreferences.findPreferenceRecursivelyByPredicate(
                database.loadAll(),
                predicate);
    }
}
