package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static de.KnollFrank.lib.settingssearch.search.PreferenceMatchHelper.getKeySet;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import com.google.common.collect.ImmutableBiMap;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;

class PreferenceSearcherTestCaseNonStandardPreferenceFragment {

    private static final String TITLE_OF_PREFERENCE = "some preference of NonStandardPreferenceFragment";
    private static final String KEY_OF_PREFERENCE = "key";

    public static void shouldSearchAndFindPreferenceOfNonStandardPreferenceFragment() {
        testSearch(
                // Given a NonStandardPreferenceFragment
                new NonStandardPreferenceFragment(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of(NonStandardPreferenceFragment.class, PreferenceFragment.class)),
                // When searching for TITLE_OF_PREFERENCE
                TITLE_OF_PREFERENCE,
                // Then the preference of NonStandardPreferenceFragment is found
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(KEY_OF_PREFERENCE)));
    }

    public static class NonStandardPreferenceFragment extends Fragment {
    }

    public static class PreferenceFragment extends PreferenceFragmentTemplate {

        public PreferenceFragment() {
            super(context -> {
                final Preference preference = new Preference(context);
                preference.setKey(KEY_OF_PREFERENCE);
                preference.setTitle(TITLE_OF_PREFERENCE);
                return List.of(preference);
            });
        }
    }

    private static void testSearch(final Fragment nonPreferenceFragment,
                                   final PrincipalAndProxyProvider principalAndProxyProvider,
                                   final String keyword,
                                   final Consumer<Set<PreferenceMatch>> checkPreferenceMatches) {
        PreferenceSearcherTest.testSearch(
                nonPreferenceFragment,
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                principalAndProxyProvider,
                checkPreferenceMatches);
    }
}
