package de.KnollFrank.settingssearch;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class InteractiveDevelopmentTest {

    @Rule
    public ActivityScenarioRule<PreferenceSearchExample> activityRule = new ActivityScenarioRule<>(PreferenceSearchExample.class);

    // @Test
    public void runAppForDevelopment() throws InterruptedException {
        keepAppOpen();
    }

    private static void keepAppOpen() throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
        }
    }
}