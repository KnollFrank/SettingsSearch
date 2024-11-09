package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;

class PreferenceSearcherTestCaseTwoDifferentPreferencePaths {

    private static final String KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT = "some preference of connected fragment";
    private static final String KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT = "keyOfPreferenceOfConnectedFragment";

    public static void shouldSearchAndFindPreferenceWithTwoDifferentPreferencePaths() {
        testSearch(
                // Given a fragment with TWO connections to a connected fragment
                new FragmentWith2Connections(),
                // When searching for KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT of that connected fragment
                KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT,
                // Then there are TWO preference search results
                preferenceMatches ->
                        assertThat(
                                PreferenceSearcherTest.getKeys(preferenceMatches),
                                contains(KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT, KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT)));
    }

    public static class FragmentWith2Connections extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            screen.setTitle("screen with connection");
            {
                final Preference preference = createPreferenceConnectedTo(PreferenceFragmentWithSinglePreference.class, context);
                preference.setTitle("first preference connected to " + PreferenceFragmentWithSinglePreference.class.getSimpleName());
                preference.setKey("key1");
                screen.addPreference(preference);
            }
            {
                final Preference preference = createPreferenceConnectedTo(PreferenceFragmentWithSinglePreference.class, context);
                preference.setTitle("second preference connected to " + PreferenceFragmentWithSinglePreference.class.getSimpleName());
                preference.setKey("key2");
                screen.addPreference(preference);
            }
            setPreferenceScreen(screen);
        }

        private static Preference createPreferenceConnectedTo(final Class<? extends Fragment> connectedFragment,
                                                              final Context context) {
            final Preference preference = new Preference(context);
            preference.setFragment(connectedFragment.getName());
            return preference;
        }
    }

    public static class PreferenceFragmentWithSinglePreference extends PreferenceFragmentTemplate {

        public PreferenceFragmentWithSinglePreference() {
            super(context -> {
                final Preference preference = new Preference(context);
                preference.setKey(KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT);
                preference.setTitle(KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT);
                return List.of(preference);
            });
        }
    }

    private static void testSearch(final FragmentWith2Connections fragmentWith2Connections,
                                   final String keyword,
                                   final Consumer<List<PreferenceMatch>> checkPreferenceMatches) {
        PreferenceSearcherTest.testSearch(
                fragmentWith2Connections,
                (preference, host) -> true,
                (preference, host) -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                checkPreferenceMatches);
    }
}
