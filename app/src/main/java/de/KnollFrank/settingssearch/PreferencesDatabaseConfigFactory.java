package de.KnollFrank.settingssearch;

import android.content.res.AssetManager;

import java.io.File;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.preference.db.PrepackagedPreferencesDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.db.source.AssetDatabaseSourceProvider;
import de.KnollFrank.settingssearch.preference.fragment.SearchDatabaseRootedAtPrefsFragmentFifthAdapter;

class PreferencesDatabaseConfigFactory {

    public static final String SEARCHABLE_PREFERENCES_DB = "searchable_preferences.db";

    private PreferencesDatabaseConfigFactory() {
    }

    public static PreferencesDatabaseConfig<Configuration> createPreferencesDatabaseConfigForCreationOfPrepackagedDatabaseAssetFile() {
        return new PreferencesDatabaseConfig<>(
                SEARCHABLE_PREFERENCES_DB,
                Optional.empty(),
                PreferencesDatabaseConfig.JournalMode.TRUNCATE);
    }

    public static PreferencesDatabaseConfig<Configuration> createPreferencesDatabaseConfigUsingPrepackagedDatabaseAssetFile(final AssetManager assetManager) {
        return new PreferencesDatabaseConfig<>(
                SEARCHABLE_PREFERENCES_DB,
                Optional.of(
                        new PrepackagedPreferencesDatabase<>(
                                new AssetDatabaseSourceProvider(
                                        new File("database/searchable_preferences_prepackaged.db"),
                                        assetManager),
                                new SearchDatabaseRootedAtPrefsFragmentFifthAdapter())),
                PreferencesDatabaseConfig.JournalMode.AUTOMATIC);
    }
}
