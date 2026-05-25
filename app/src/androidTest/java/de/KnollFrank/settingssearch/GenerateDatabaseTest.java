package de.KnollFrank.settingssearch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static de.KnollFrank.settingssearch.PreferenceSearchExampleTest.searchButton;
import static de.KnollFrank.settingssearch.PreferenceSearchExampleTest.searchView;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.EspressoIdlingResource;
import de.KnollFrank.lib.settingssearch.common.Locales;
import de.KnollFrank.lib.settingssearch.common.uicontroller.Fragments;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceToSearchablePreferenceConverterFactory;
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenTreeRepository;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogsFactory;
import de.KnollFrank.lib.settingssearch.graph.UiCrawler;

@RunWith(AndroidJUnit4.class)
public class GenerateDatabaseTest {

    @Rule
    public ActivityScenarioRule<PreferenceSearchExample> activityRule = new ActivityScenarioRule<>(PreferenceSearchExample.class);

    @Before
    public void registerIdlingResource() {
        IdlingRegistry
                .getInstance()
                .register(EspressoIdlingResource.getCountingIdlingResource());
    }

    // @Test
    public void generateDatabaseAndWaitForCompletion() {
        // Because of onStart(), the background task is already running.
        // Espresso will AUTOMATICALLY PAUSE HERE until the IdlingResource counter is 0.
        System.out.println("Espresso is waiting for the initial DB task to complete...");

        // This click action will only be performed AFTER the initial task is done.
        System.out.println("Clicking the search icon...");
        onView(searchButton()).perform(click());

        // Verify the result.
        System.out.println("Verifying that the search input view is displayed...");
        onView(searchView()).check(matches(isDisplayed()));

        System.out.println("Database generation and search fragment display were successful.");
    }

    // FK-TODO: refactor
    @Test
    public void generateDatabaseViaUiCrawler() {
        final AtomicReference<FragmentActivity> activityRef = new AtomicReference<>();
        activityRule.getScenario().onActivity(activityRef::set);
        final FragmentActivity activity = activityRef.get();
        final SearchDatabaseConfig<Configuration> searchDatabaseConfig = SearchDatabaseConfigFactory.createSearchDatabaseConfig();
        final UiCrawler crawler =
                new UiCrawler(
                        createConverter(activity, searchDatabaseConfig),
                        new EspressoUiNavigator(activity),
                        searchablePreference -> false);

        System.out.println("Starting UI Crawl...");
        final var tree = crawler.crawl();
        System.out.println("UI Crawl completed.");

        // Persist the tree
        GenerateDatabaseTest
                .getTreeRepository(activity)
                .persistOrReplace(
                        new SearchablePreferenceScreenTree<>(
                                tree,
                                Locales.getCurrentLocale(activity.getResources().getConfiguration().getLocales()),
                                new ConfigurationBundleConverter().convertForward(ConfigurationProvider.getActualConfiguration(activity))));
        System.out.println("Database generated via UiCrawler and persisted successfully.");
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry
                .getInstance()
                .unregister(EspressoIdlingResource.getCountingIdlingResource());
    }

    private static PreferenceScreenToSearchablePreferenceScreenConverter createConverter(final FragmentActivity activity,
                                                                                          final SearchDatabaseConfig<Configuration> searchDatabaseConfig) {
        return new PreferenceScreenToSearchablePreferenceScreenConverter(
                PreferenceToSearchablePreferenceConverterFactory.createPreferenceToSearchablePreferenceConverter(
                        searchDatabaseConfig,
                        PreferenceDialogsFactory.createPreferenceDialogs(
                                activity,
                                R.id.fragmentContainerView,
                                searchDatabaseConfig.preferenceSearchablePredicate)));
    }

    private static SearchablePreferenceScreenTreeRepository<Configuration> getTreeRepository(final FragmentActivity activity) {
        return SettingsSearchApplication
                .getInstanceFromContext(activity)
                .preferencesDatabaseManager
                .getPreferencesDatabase()
                .searchablePreferenceScreenTreeRepository();
    }

    private static class EspressoUiNavigator implements UiCrawler.UiNavigator {

        private final Context context;

        public EspressoUiNavigator(final Context context) {
            this.context = context;
        }

        @Override
        public void click(final de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference preference) {
            final String id = preference.getId();
            try {
                final String[] parts = id.split("-");
                final int index = Integer.parseInt(parts[parts.length - 1]);
                Espresso
                        .onView(isAssignableFrom(androidx.recyclerview.widget.RecyclerView.class))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(index, ViewActions.click()));
            } catch (final Exception e) {
                // Fallback: This should not happen if the id is correctly formatted
                final Matcher<View> titleMatcher = allOf(
                        anyOf(withId(android.R.id.title), withId(R.id.title)),
                        withText(preference.getTitle().get()));

                final Matcher<View> itemMatcher;
                if (preference.getSummary().isPresent()) {
                    final Matcher<View> summaryMatcher = allOf(
                            anyOf(withId(android.R.id.summary), withId(R.id.summary)),
                            withText(preference.getSummary().get()));
                    itemMatcher = allOf(hasDescendant(titleMatcher), hasDescendant(summaryMatcher));
                } else {
                    itemMatcher = hasDescendant(titleMatcher);
                }

                Espresso
                        .onView(isAssignableFrom(androidx.recyclerview.widget.RecyclerView.class))
                        .perform(RecyclerViewActions.actionOnItem(itemMatcher, ViewActions.click()));
            }
        }

        @Override
        public void goBack() {
            pressBack();
        }

        @Override
        public void waitUntilIdle() {
            // Espresso handles this automatically
        }

        @Override
        public Optional<de.KnollFrank.lib.settingssearch.graph.PreferencesOfFragment> extractPreferences() {
            return Fragments
                    .findEitherVisibleFragmentOnCurrentActivityOrError()
                    .join(
                            fragment -> GraphicalPreferenceExtractor.extract(context, fragment),
                            errorMessage -> Optional.empty());
        }
    }
}
