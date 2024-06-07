package de.KnollFrank.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import android.os.Looper;

import androidx.preference.Preference;
import androidx.test.core.app.ActivityScenario;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.PreferenceSearcherTest.PrefsFragment;
import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceProviderTest {

    @BeforeClass
    public static void beforeClass() {
        Looper.prepare();
    }

    @AfterClass
    public static void afterClass() {
        Looper.getMainLooper().quitSafely();
    }

    @Test
    public void shouldGetPreferences() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final PreferenceProvider preferenceProvider =
                        new PreferenceProvider(
                                new PreferenceFragments(
                                        fragmentActivity,
                                        fragmentActivity.getSupportFragmentManager(),
                                        TestActivity.FRAGMENT_CONTAINER_VIEW));
                final Class<PrefsFragment> preferenceScreen = PrefsFragment.class;

                // When
                final List<Preference> preferences = preferenceProvider.getPreferences(preferenceScreen);

                // Then
                assertThat(preferences, hasSize(1));
            });
        }
    }
}