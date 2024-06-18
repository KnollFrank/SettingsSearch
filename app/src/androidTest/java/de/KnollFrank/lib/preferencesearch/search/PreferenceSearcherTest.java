package de.KnollFrank.lib.preferencesearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static de.KnollFrank.lib.preferencesearch.PreferenceFragmentsFactory.createPreferenceFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;

import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.PreferenceScreensProvider;
import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;
import de.KnollFrank.lib.preferencesearch.PreferencesProvider;
import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceSearcherTest {

    @BeforeClass
    public static void beforeClass() {
        Looper.prepare();
    }

    @AfterClass
    public static void afterClass() {
        Looper.getMainLooper().quitSafely();
    }

    @Test
    public void shouldSearchAndFind() {
        final String keyword = "fourth";
        testSearch(PrefsFragment.class, keyword, hasItem(containsString(keyword)));
    }

    @Test
    public void shouldSearchAndNotFind() {
        final String keyword = "non_existing_keyword";
        testSearch(PrefsFragment.class, keyword, not(hasItem(containsString(keyword))));
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

            final CheckBoxPreference checkBoxPreference = new CheckBoxPreference(context);
            checkBoxPreference.setKey("fourthfile");
            checkBoxPreference.setSummary("This checkbox is a preference coming from a fourth file");
            checkBoxPreference.setTitle("Checkbox fourth file");

            screen.addPreference(checkBoxPreference);
            setPreferenceScreen(screen);
        }
    }

    private static void testSearch(final Class<? extends PreferenceFragmentCompat> preferenceScreen,
                                   final String keyword,
                                   final Matcher<Iterable<? super String>> titlesMatcher) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final List<PreferenceWithHost> preferenceWithHostList =
                        getPreferencesProvider(preferenceScreen, fragmentActivity)
                                .getPreferenceScreenWithHosts()
                                .preferenceWithHostList;
                final PreferenceSearcher preferenceSearcher = new PreferenceSearcher(preferenceWithHostList);

                // When
                final List<PreferenceWithHost> preferenceItems = preferenceSearcher.searchFor(keyword);

                // Then
                assertThat(getTitles(preferenceItems), titlesMatcher);
            });
        }
    }

    private static PreferencesProvider getPreferencesProvider(final Class<? extends PreferenceFragmentCompat> preferenceScreen, final TestActivity fragmentActivity) {
        return new PreferencesProvider(
                preferenceScreen.getName(),
                new PreferenceScreensProvider(createPreferenceFragments(fragmentActivity)),
                fragmentActivity);
    }

    private static List<String> getTitles(final List<PreferenceWithHost> preferences) {
        return preferences
                .stream()
                .map(preferenceWithHost -> preferenceWithHost.preference.getTitle())
                .filter(Objects::nonNull)
                .map(charSequence -> charSequence.toString())
                .collect(Collectors.toList());
    }
}