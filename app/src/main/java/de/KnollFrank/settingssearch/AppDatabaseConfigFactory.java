package de.KnollFrank.settingssearch;

import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.preference.db.GraphProcessor;
import de.KnollFrank.lib.settingssearch.db.preference.db.PrepackagedAppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.settingssearch.preference.fragment.SearchDatabaseRootedAtPrefsFragmentFirstAdapter;

class AppDatabaseConfigFactory {

    public static final String SEARCHABLE_PREFERENCES_DB = "searchable_preferences.db";

    public static AppDatabaseConfig createDatabaseConfigForCreationOfPrepackagedDatabaseAssetFile() {
        return new AppDatabaseConfig(
                SEARCHABLE_PREFERENCES_DB,
                Optional.empty(),
                AppDatabaseConfig.JournalMode.TRUNCATE);
    }

    public static AppDatabaseConfig createAppDatabaseConfigUsingPrepackagedDatabaseAssetFile() {
        return new AppDatabaseConfig(
                SEARCHABLE_PREFERENCES_DB,
                Optional.of(
                        new PrepackagedAppDatabase(
                                new File("database/searchable_preferences_prepackaged.db"),
                                new GraphProcessor() {

                                    @Override
                                    public SearchablePreferenceScreenGraph processGraph(final SearchablePreferenceScreenGraph graph, final FragmentActivity activityContext) {
                                        return new SearchDatabaseRootedAtPrefsFragmentFirstAdapter()
                                                .getAdaptedGraph(graph, activityContext)
                                                .asProcessedGraph();
                                    }
                                })),
                AppDatabaseConfig.JournalMode.AUTOMATIC);
    }
}
