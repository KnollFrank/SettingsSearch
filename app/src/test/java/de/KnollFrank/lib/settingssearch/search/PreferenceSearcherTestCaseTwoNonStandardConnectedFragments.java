package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static de.KnollFrank.lib.settingssearch.graph.TreeBuilderListeners.emptyTreeBuilderListener;
import static de.KnollFrank.lib.settingssearch.search.PreferenceMatchHelper.getKeySet;

import android.os.Bundle;

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
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenTreeRepository;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.settingssearch.Configuration;

class PreferenceSearcherTestCaseTwoNonStandardConnectedFragments {

    private static final String KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT = "some preference of connected fragment";
    private static final String KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT = "keyOfPreferenceOfConnectedFragment";

    private PreferenceSearcherTestCaseTwoNonStandardConnectedFragments() {
    }

    public static void shouldSearchAndFindPreferenceOfNonStandardConnectedFragment(
            final SearchablePreferenceScreenTreeRepository<Configuration> treeRepository) {
        testSearch(
                // Given a fragment with a non standard connected fragment
                new FragmentWithNonStandardConnection(),
                (preference, hostOfPreference) ->
                        "key".equals(preference.getKey()) ?
                                Optional.of(PreferenceFragmentWithSinglePreference.class) :
                                Optional.empty(),
                // When searching for KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT of that connected fragment
                KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT,
                // Then the preference of the non standard connected fragment is found
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT)),
                treeRepository);
    }

    public static class FragmentWithNonStandardConnection extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(requireContext());
            screen.setTitle("screen with connection");
            {
                final Preference preference = new Preference(requireContext());
                preference.setTitle("preference with a non standard connection to " + PreferenceFragmentWithSinglePreference.class.getSimpleName());
                preference.setKey("key");
                screen.addPreference(preference);
            }
            setPreferenceScreen(screen);
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

    private static void testSearch(final FragmentWithNonStandardConnection fragmentWithNonStandardConnection,
                                   final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                                   final String keyword,
                                   final Consumer<Set<PreferenceMatch>> checkPreferenceMatches,
                                   final SearchablePreferenceScreenTreeRepository<Configuration> treeRepository) {
        PreferenceSearcherTest.testSearch(
                fragmentWithNonStandardConnection,
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                preferenceFragmentConnected2PreferenceProvider,
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                checkPreferenceMatches,
                treeRepository,
                emptyTreeBuilderListener());
    }
}
