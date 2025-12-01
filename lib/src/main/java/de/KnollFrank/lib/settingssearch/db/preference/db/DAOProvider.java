package de.KnollFrank.lib.settingssearch.db.preference.db;

// FK-TODO: rename to SearchablePreferenceScreenGraphRepositoryProvider
public interface DAOProvider<C> {

    SearchablePreferenceScreenGraphRepository<C> searchablePreferenceScreenGraphRepository();
}
