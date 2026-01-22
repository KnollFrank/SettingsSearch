package de.KnollFrank.settingssearch;

import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.preference.db.PrepackagedPreferencesDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
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

    public static PreferencesDatabaseConfig<Configuration> createPreferencesDatabaseConfigUsingPrepackagedDatabaseAssetFile() {
        return new PreferencesDatabaseConfig<>(
                SEARCHABLE_PREFERENCES_DB,
                Optional.of(
                        new PrepackagedPreferencesDatabase<>(
                                new File("database/searchable_preferences_prepackaged.db"),
                                new SearchablePreferenceScreenTreeTransformer<>() {

                                    @Override
                                    public SearchablePreferenceScreenTree transformTree(final SearchablePreferenceScreenTree tree,
                                                                                        final Configuration targetConfiguration,
                                                                                        final FragmentActivity activityContext) {
                                        return new SearchDatabaseRootedAtPrefsFragmentFifthAdapter().adaptGraphAtPrefsFragmentFifth(
                                                tree,
                                                targetConfiguration,
                                                SearchDatabaseConfigFactory.createSearchDatabaseConfig(),
                                                activityContext);
                                    }
                                })),
                PreferencesDatabaseConfig.JournalMode.AUTOMATIC);
    }
}
