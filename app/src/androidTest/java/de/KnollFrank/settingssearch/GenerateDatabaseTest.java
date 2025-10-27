package de.KnollFrank.settingssearch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static de.KnollFrank.settingssearch.PreferenceSearchExampleTest.searchButton;
import static de.KnollFrank.settingssearch.PreferenceSearchExampleTest.searchView;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.KnollFrank.lib.settingssearch.common.EspressoIdlingResource;

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

    @Test
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

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry
                .getInstance()
                .unregister(EspressoIdlingResource.getCountingIdlingResource());
    }
}
