package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import android.os.Bundle;
import android.os.Looper;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.bytehamster.preferencesearch.R;
import com.bytehamster.preferencesearch.multiplePreferenceScreens.MultiplePreferenceScreensExample;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class PreferenceParserTest {

    @BeforeClass
    public static void beforeClass() {
        Looper.prepare();
    }

    @AfterClass
    public static void afterClass() {
        Looper.getMainLooper().quitSafely();
    }

    @Test
    public void shouldParseXmlResource() {
        // FK-TODO: do not use MultiplePreferenceScreensExample as an activity, create a new activity within the androidTest folder.
        try (final ActivityScenario<MultiplePreferenceScreensExample> scenario = ActivityScenario.launch(MultiplePreferenceScreensExample.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final PreferenceParser preferenceParser = new PreferenceParser(new PreferenceFragmentHelper(fragmentActivity, R.id.fragmentContainerView));
                final Class<PrefsFragment> preferenceScreen = PrefsFragment.class;

                // When
                final List<Preference> preferences = preferenceParser.parsePreferenceScreen(preferenceScreen);

                // Then
                assertThat(preferences, hasSize(1));
            });
        }
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences4);
        }
    }
}