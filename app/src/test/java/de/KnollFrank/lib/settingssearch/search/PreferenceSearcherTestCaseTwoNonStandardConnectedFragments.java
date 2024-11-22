package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;

class PreferenceSearcherTestCaseTwoNonStandardConnectedFragments {

    private static final String KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT = "some preference of connected fragment";
    private static final String KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT = "keyOfPreferenceOfConnectedFragment";

    public static void shouldSearchAndFindPreferenceOfNonStandardConnectedFragment() {
        testSearch(
                // Given a fragment with a non standard connected fragment
                new FragmentWithNonStandardConnection(),
                (preference, hostOfPreference) ->
                        "key".equals(preference.getKey()) ?
                                Optional.of(PreferenceFragmentWithSinglePreference.class.getName()) :
                                Optional.empty(),
                // When searching for KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT of that connected fragment
                KEYWORD_OR_TITLE_OF_PREFERENCE_OF_CONNECTED_FRAGMENT,
                // Then the preference of the non standard connected fragment is found
                preferenceMatches ->
                        assertThat(
                                PreferenceSearcherTest.getKeys(preferenceMatches),
                                hasItem(KEY_OF_PREFERENCE_OF_CONNECTED_FRAGMENT)));
    }

    public static class FragmentWithNonStandardConnection extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            screen.setTitle("screen with connection");
            {
                final Preference preference = new Preference(context);
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
                                   final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                   final String keyword,
                                   final Consumer<Set<PreferenceMatch>> checkPreferenceMatches) {
        PreferenceSearcherTest.testSearch(
                fragmentWithNonStandardConnection,
                (preference, host) -> true,
                (preference, host) -> true,
                keyword,
                preferenceConnected2PreferenceFragmentProvider,
                (preference, hostOfPreference) -> Optional.empty(),
                checkPreferenceMatches);
    }
}
