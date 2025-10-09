package de.KnollFrank.settingssearch;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseProcessor;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.db.preference.db.PrepackagedAppDatabase;
import de.KnollFrank.settingssearch.preference.fragment.SearchDatabaseRootedAtPrefsFragmentFirstAdapter;

class AppDatabaseConfigFactory {

    public static final String SEARCHABLE_PREFERENCES_DB = "searchable_preferences.db";

    public static AppDatabaseConfig createDatabaseConfigForCreationOfPrepackagedDatabaseAssetFile() {
        return new AppDatabaseConfig(
                SEARCHABLE_PREFERENCES_DB,
                Optional.empty(),
                AppDatabaseConfig.JournalMode.TRUNCATE);
    }

    public static AppDatabaseConfig createAppDatabaseConfigUsingPrepackagedDatabaseAssetFile(final Context context) {
        return new AppDatabaseConfig(
                SEARCHABLE_PREFERENCES_DB,
                Optional.of(
                        new PrepackagedAppDatabase(
                                new File("database/searchable_preferences_prepackaged.db"),
                                new AppDatabaseProcessor() {

                                    @Override
                                    public void processAppDatabase(final DAOProvider appDatabase, final FragmentActivity activityContext) {
                                        new SearchDatabaseRootedAtPrefsFragmentFirstAdapter().adaptSearchDatabaseRootedAtPrefsFragmentFirst(
                                                Utils.getCurrentLocale(context.getResources()),
                                                appDatabase,
                                                activityContext);
                                    }
                                })),
                AppDatabaseConfig.JournalMode.AUTOMATIC);
    }
}
