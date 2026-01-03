package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static de.KnollFrank.lib.settingssearch.search.PreferenceMatchHelper.getKeyList;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableBiMap;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenGraphRepository;
import de.KnollFrank.settingssearch.Configuration;

class PreferenceSearcherTestCaseTwoDifferentPreferencePaths {

    private static final String KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT = "some preference of connected fragment";
    private static final String KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT = "keyOfPreferenceOfConnectedFragment";

    public static void shouldSearchAndFindPreferenceWithTwoDifferentPreferencePaths(
            final SearchablePreferenceScreenGraphRepository<Configuration> graphRepository) {
        testSearch(
                // Given a fragment with TWO connections to a connected fragment
                new FragmentWith2Connections(),
                // When searching for KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT of that connected fragment
                KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT,
                // Then there are TWO preference search results
                preferenceMatches ->
                        assertThat(
                                getKeyList(preferenceMatches),
                                contains(KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT, KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT)),
                graphRepository);
    }

    public static class FragmentWith2Connections extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(requireContext());
            screen.setTitle("screen with connection");
            final String someArgument = "someArgument";
            {
                final Preference preference = createPreferenceConnectedTo(PreferenceFragmentWithSinglePreference.class, requireContext());
                preference.setTitle("first preference connected to " + PreferenceFragmentWithSinglePreference.class.getSimpleName());
                preference.setKey("key1");
                preference.getExtras().putString(someArgument, "1");
                screen.addPreference(preference);
            }
            {
                final Preference preference = createPreferenceConnectedTo(PreferenceFragmentWithSinglePreference.class, requireContext());
                preference.setTitle("second preference connected to " + PreferenceFragmentWithSinglePreference.class.getSimpleName());
                preference.setKey("key2");
                preference.getExtras().putString(someArgument, "2");
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
                                   final Consumer<Set<PreferenceMatch>> checkPreferenceMatches,
                                   final SearchablePreferenceScreenGraphRepository<Configuration> graphRepository) {
        PreferenceSearcherTest.testSearch(
                fragmentWith2Connections,
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                checkPreferenceMatches,
                graphRepository,
                preferenceScreenGraph -> {
                });
    }
}
