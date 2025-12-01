package de.KnollFrank.lib.settingssearch.db.preference.db;

public class DatabaseResetter {

    public static <C> void resetDatabase(final DAOProvider<C> daoProvider) {
        daoProvider.searchablePreferenceScreenGraphRepository().removeAll();
    }
}
