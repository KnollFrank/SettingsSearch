package de.KnollFrank.lib.settingssearch.db.preference.db;

public class DatabaseResetter {

    public static <C> void resetDatabase(final PreferencesDatabase<C> preferencesDatabase) {
        preferencesDatabase.searchablePreferenceScreenTreeRepository().removeAll();
    }
}
