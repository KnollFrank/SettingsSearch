package de.KnollFrank.settingssearch;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.common.task.LongRunningTask;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

// FK-TODO: suche nach etwas, scrolle im Suchergebnis nach unten, klicke ein Suchergebnis an, drücke den Back-Button, dann werden die Suchergebnisse erneut angezeigt und die vorherige Scrollposition (mit dem gerade angeklickten Suchergebnis) soll wiederhergestellt sein.
public class PreferenceSearchExample extends AppCompatActivity {

    private static final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = R.id.fragmentContainerView;
    private static final @IdRes int DUMMY_FRAGMENT_CONTAINER_VIEW_ID = View.generateViewId();

    private Optional<LongRunningTask<MergedPreferenceScreenData>> createSearchDatabaseTask = Optional.empty();

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
        final var createSearchDatabaseTask = _getCreateSearchDatabaseTask();
        this.createSearchDatabaseTask = Optional.of(createSearchDatabaseTask);
        createSearchDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    public Optional<LongRunningTask<MergedPreferenceScreenData>> getCreateSearchDatabaseTask() {
        return createSearchDatabaseTask;
    }

    private void show(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(FRAGMENT_CONTAINER_VIEW_ID, fragment)
                .commit();
    }

    private void showSearchPreferenceFragment() {
        createSearchPreferenceFragments().showSearchPreferenceFragment();
    }

    private SearchPreferenceFragments createSearchPreferenceFragments() {
        return SearchPreferenceFragmentsBuilderConfigurer
                .configure(
                        SearchPreferenceFragments.builder(
                                createSearchConfiguration(PrefsFragmentFirst.class),
                                getSupportFragmentManager(),
                                this,
                                OnUiThreadRunnerFactory.fromActivity(this)),
                        this::getCreateSearchDatabaseTask)
                .build();
    }

    private SearchConfiguration createSearchConfiguration(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        return new SearchConfiguration(
                FRAGMENT_CONTAINER_VIEW_ID,
                Optional.empty(),
                rootPreferenceFragment);
    }

    private LongRunningTask<MergedPreferenceScreenData> _getCreateSearchDatabaseTask() {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                findViewById(android.R.id.content),
                DUMMY_FRAGMENT_CONTAINER_VIEW_ID);
        // FK-FIXME: koordiniere diesen Task (1.) mit dem Task (2.) in SearchPreferenceFragment und mit (3.) SearchPreferenceFragments.rebuildSearchDatabase()
        return new LongRunningTask<>(
                getMergedPreferenceScreenData(Utils.geCurrentLocale(getResources())),
                mergedPreferenceScreenData -> {
                });
    }

    private Function<ProgressUpdateListener, MergedPreferenceScreenData> getMergedPreferenceScreenData(final Locale locale) {
        final SearchPreferenceFragments searchPreferenceFragments = createSearchPreferenceFragments();
        final DefaultFragmentInitializer preferenceDialogs =
                new DefaultFragmentInitializer(
                        getSupportFragmentManager(),
                        DUMMY_FRAGMENT_CONTAINER_VIEW_ID,
                        OnUiThreadRunnerFactory.fromActivity(this));
        return progressDisplayer ->
                searchPreferenceFragments
                        .createMergedPreferenceScreenDataRepository(
                                preferenceDialogs,
                                this,
                                progressDisplayer)
                        .getMergedPreferenceScreenData(locale);
    }
}
