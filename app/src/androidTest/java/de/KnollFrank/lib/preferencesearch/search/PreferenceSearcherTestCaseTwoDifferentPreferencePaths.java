package de.KnollFrank.lib.preferencesearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Optional;

class PreferenceSearcherTestCaseTwoDifferentPreferencePaths {

    private static final String KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT = "some preference of connected fragment";
    private static final String KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT = "keyOfPreferenceOfConnectedFragment";

    public static void shouldSearchAndFindPreferenceWithTwoDifferentPreferencePaths() {
        final String keyword = KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT;
        final String keyOfPreference = KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT;
        final FragmentWith2Connections fragmentWith2ConnectionsToPreferenceFragmentWithSinglePreference =
                FragmentWith2Connections.createFragmentWith2ConnectionsTo(PreferenceFragmentWithSinglePreference.class);
        PreferenceSearcherTest.testSearch(
                fragmentWith2ConnectionsToPreferenceFragmentWithSinglePreference,
                (preference, host) -> true,
                keyword,
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                PreferenceSearcherTest.getKeys(preferenceMatches),
                                contains(keyOfPreference, keyOfPreference)));
    }

    public static class FragmentWith2Connections extends PreferenceFragmentCompat {

        private final Class<? extends PreferenceFragmentCompat> connectedFragment;

        public static FragmentWith2Connections createFragmentWith2ConnectionsTo(final Class<? extends PreferenceFragmentCompat> connectedFragment) {
            return new FragmentWith2Connections(connectedFragment);
        }

        public FragmentWith2Connections(final Class<? extends PreferenceFragmentCompat> connectedFragment) {
            this.connectedFragment = connectedFragment;
        }

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            screen.setTitle("screen with connection");
            {
                final Preference preference = createPreferenceConnectedTo(connectedFragment, context);
                preference.setTitle("first preference connected to " + connectedFragment.getSimpleName());
                preference.setKey("key1");
                screen.addPreference(preference);
            }
            {
                final Preference preference = createPreferenceConnectedTo(connectedFragment, context);
                preference.setTitle("second preference connected to " + connectedFragment.getSimpleName());
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

    public static class PreferenceFragmentWithSinglePreference extends PreferenceFragmentTemplateWithSinglePreference {

        @Override
        protected Preference createPreference(final Context context) {
            final Preference preference = new Preference(context);
            preference.setKey(KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT);
            preference.setTitle(KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT);
            return preference;
        }
    }
}
