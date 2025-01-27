package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static de.KnollFrank.lib.settingssearch.search.PreferenceMatchHelper.getKeySet;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverterFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;

class PreferenceSearcherTestCaseNonStandardPreferenceFragment {

    private static final String TITLE_OF_PREFERENCE = "some preference of NonStandardPreferenceFragment";
    private static final String KEY_OF_PREFERENCE = "key";

    public static void shouldSearchAndFindPreferenceOfNonStandardPreferenceFragment() {
        testSearch(
                // Given a NonStandardPreferenceFragment
                new NonStandardPreferenceFragment(),
                new Fragment2PreferenceFragmentConverterFactory() {

                    @Override
                    public Fragment2PreferenceFragmentConverter createFragment2PreferenceFragmentConverter(final Fragments fragments) {
                        return new Fragment2PreferenceFragmentConverter() {

                            @Override
                            public Optional<PreferenceFragmentCompat> convert(final Fragment fragment) {
                                return fragment instanceof final NonStandardPreferenceFragment nonPreferenceFragment ?
                                        Optional.of(
                                                (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                                                        nonPreferenceFragment.asPreferenceFragment().getClass().getName(),
                                                        Optional.empty())) :
                                        Optional.empty();
                            }
                        };
                    }
                },
                // When searching for TITLE_OF_PREFERENCE
                TITLE_OF_PREFERENCE,
                // Then the preference of NonStandardPreferenceFragment is found
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(KEY_OF_PREFERENCE)));
    }

    public static class NonStandardPreferenceFragment extends Fragment {

        public PreferenceFragmentCompat asPreferenceFragment() {
            return new PreferenceFragment();
        }
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
                                   final Fragment2PreferenceFragmentConverterFactory fragment2PreferenceFragmentConverterFactory,
                                   final String keyword,
                                   final Consumer<Set<PreferenceMatch>> checkPreferenceMatches) {
        PreferenceSearcherTest.testSearch(
                nonPreferenceFragment,
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                fragment2PreferenceFragmentConverterFactory,
                checkPreferenceMatches);
    }
}
