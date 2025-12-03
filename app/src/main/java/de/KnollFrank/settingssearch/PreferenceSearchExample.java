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
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.Tasks;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProviderManager;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

// FK-TODO: suche nach etwas, scrolle im Suchergebnis nach unten, klicke ein Suchergebnis an, drücke den Back-Button, dann werden die Suchergebnisse erneut angezeigt und die vorherige Scrollposition (mit dem gerade angeklickten Suchergebnis) soll wiederhergestellt sein.
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
        this
                .getDaoProviderManager()
                .initDAOProvider(
                        BuildConfig.GENERATE_DATABASE_FOR_ASSET ?
                                PreferencesDatabaseConfigFactory.createPreferencesDatabaseConfigForCreationOfPrepackagedDatabaseAssetFile() :
                                PreferencesDatabaseConfigFactory.createPreferencesDatabaseConfigUsingPrepackagedDatabaseAssetFile(),
                        configuration,
                        new ConfigurationBundleConverter(),
                        this);
        final PreferencesDatabase<Configuration> preferencesDatabase = getDaoProviderManager().getDAOProvider();
        final SearchPreferenceFragments<Configuration> searchPreferenceFragments = createSearchPreferenceFragments(preferencesDatabase, configuration);
        createSearchDatabaseTask =
                Optional.of(
                        CreateSearchDatabaseTaskProvider.getCreateSearchDatabaseTask(
                                searchPreferenceFragments,
                                this,
                                preferencesDatabase,
                                new ConfigurationBundleConverter().convertForward(configuration),
                                searchPreferenceFragments.searchDatabaseConfig.preferenceSearchablePredicate));
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
                        getDaoProviderManager().getDAOProvider(),
                        ConfigurationProvider.getActualConfiguration(this));
        searchPreferenceFragments.showSearchPreferenceFragment();
    }

    private SearchPreferenceFragments<Configuration> createSearchPreferenceFragments(final PreferencesDatabase<Configuration> preferencesDatabase,
                                                                                     final Configuration configuration) {
        return SearchPreferenceFragmentsFactory.createSearchPreferenceFragments(
                FRAGMENT_CONTAINER_VIEW_ID,
                this,
                () -> createSearchDatabaseTask,
                mergedPreferenceScreen -> {
                },
                preferencesDatabase,
                configuration);
    }

    private DAOProviderManager<Configuration> getDaoProviderManager() {
        return SettingsSearchApplication
                .getInstanceFromContext(this)
                .daoProviderManager;
    }
}
