package de.KnollFrank.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;

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
import java.util.Optional;
import java.util.stream.Collectors;

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
                final PreferenceSearcher<PreferenceItem> preferenceSearcher = createPreferenceSearcher(preferenceScreen, fragmentActivity);

                // When
                final List<PreferenceItem> preferenceItems = preferenceSearcher.searchFor(keyword);

                // Then
                assertThat(getTitles(preferenceItems), titlesMatcher);
            });
        }
    }

    private static PreferenceSearcher<PreferenceItem> createPreferenceSearcher(
            final Class<? extends PreferenceFragmentCompat> preferenceScreen,
            final TestActivity fragmentActivity) {
        PreferenceProvider preferenceProvider =
                PreferenceProviderFactory.createPreferenceProvider(
                        fragmentActivity,
                        fragmentActivity.getSupportFragmentManager(),
                        TestActivity.FRAGMENT_CONTAINER_VIEW);
        return new PreferenceSearcher<>(
                PreferenceItems.getPreferenceItems(
                        preferenceProvider.getPreferences(preferenceScreen),
                        preferenceScreen));
    }

    private static List<String> getTitles(final List<PreferenceItem> preferenceItems) {
        return preferenceItems
                .stream()
                .map(result -> result.title)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}