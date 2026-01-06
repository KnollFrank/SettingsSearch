package de.KnollFrank.settingssearch;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.client.CreateSearchDatabaseTaskProvider;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.Tasks;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabaseManager;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

// FK-TODO: suche nach etwas, scrolle im Suchergebnis nach unten, klicke ein Suchergebnis an, drücke den Back-Button, dann werden die Suchergebnisse erneut angezeigt und die vorherige Scrollposition (mit dem gerade angeklickten Suchergebnis) soll wiederhergestellt sein.
// FK-TODO: keine echten Strings mehr speichern, sondern nur noch Resource-Ids für Mehrsprachigkeit?
public class PreferenceSearchExample extends AppCompatActivity {

    private static final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = R.id.fragmentContainerView;

    private Optional<AsyncTaskWithProgressUpdateListeners<Void, PreferencesDatabase<Configuration>>> createSearchDatabaseTask = Optional.empty();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_preference_screens_example);
        if (savedInstanceState == null) {
            show(new PrefsFragmentFirst());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Configuration configuration = ConfigurationProvider.getActualConfiguration(this);
        final SearchDatabaseConfig searchDatabaseConfig = SearchDatabaseConfigFactory.createSearchDatabaseConfig();
        this
                .getPreferencesDatabaseManager()
                .initPreferencesDatabase(
                        BuildConfig.GENERATE_DATABASE_FOR_ASSET ?
                                PreferencesDatabaseConfigFactory.createPreferencesDatabaseConfigForCreationOfPrepackagedDatabaseAssetFile() :
                                PreferencesDatabaseConfigFactory.createPreferencesDatabaseConfigUsingPrepackagedDatabaseAssetFile(),
                        configuration,
                        new ConfigurationBundleConverter(),
                        searchDatabaseConfig.computePreferencesListener,
                        this);
        final PreferencesDatabase<Configuration> preferencesDatabase = getPreferencesDatabaseManager().getPreferencesDatabase();
        final SearchPreferenceFragments<Configuration> searchPreferenceFragments =
                createSearchPreferenceFragments(
                        preferencesDatabase,
                        configuration,
                        searchDatabaseConfig);
        createSearchDatabaseTask =
                Optional.of(
                        CreateSearchDatabaseTaskProvider.getCreateSearchDatabaseTask(
                                searchPreferenceFragments,
                                this,
                                preferencesDatabase,
                                new ConfigurationBundleConverter().convertForward(configuration),
                                searchDatabaseConfig.preferenceSearchablePredicate));
        Tasks.executeTaskInParallelWithOtherTasks(createSearchDatabaseTask.orElseThrow());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if (item.getItemId() == R.id.search_action) {
            showSearchPreferenceFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void show(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(FRAGMENT_CONTAINER_VIEW_ID, fragment)
                .commit();
    }

    private void showSearchPreferenceFragment() {
        final SearchPreferenceFragments<Configuration> searchPreferenceFragments =
                createSearchPreferenceFragments(
                        getPreferencesDatabaseManager().getPreferencesDatabase(),
                        ConfigurationProvider.getActualConfiguration(this),
                        SearchDatabaseConfigFactory.createSearchDatabaseConfig());
        searchPreferenceFragments.showSearchPreferenceFragment();
    }

    private SearchPreferenceFragments<Configuration> createSearchPreferenceFragments(
            final PreferencesDatabase<Configuration> preferencesDatabase,
            final Configuration configuration,
            final SearchDatabaseConfig searchDatabaseConfig) {
        return SearchPreferenceFragmentsFactory.createSearchPreferenceFragments(
                FRAGMENT_CONTAINER_VIEW_ID,
                this,
                () -> createSearchDatabaseTask,
                mergedPreferenceScreen -> {
                },
                preferencesDatabase,
                configuration,
                searchDatabaseConfig);
    }

    private PreferencesDatabaseManager<Configuration> getPreferencesDatabaseManager() {
        return SettingsSearchApplication
                .getInstanceFromContext(this)
                .preferencesDatabaseManager;
    }
}
