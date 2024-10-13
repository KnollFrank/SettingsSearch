package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByName;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreensProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphDAOProvider;
import de.KnollFrank.settingssearch.test.TestActivity;

public class PreferenceScreensProvider2Test {

    @Test
    public void shouldIgnoreNonPreferenceFragments() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(PreferenceScreensProvider2Test::shouldIgnoreNonPreferenceFragments);
        }
    }

    private static void shouldIgnoreNonPreferenceFragments(final FragmentActivity activity) {
        // Given
        final Fragments fragments = FragmentsFactory.createFragments(activity);
        final String rootPreferenceFragmentClassName = FragmentConnectedToNonPreferenceFragment.class.getName();
        final PreferenceScreensProvider preferenceScreensProvider =
                new PreferenceScreensProvider(
                        new PreferenceScreenWithHostProvider(fragments, PreferenceFragmentCompat::getPreferenceScreen),
                        (preference, hostOfPreference) -> Optional.empty(),
                        preferenceScreenGraph -> {
                        },
                        new SearchableInfoAndDialogInfoProvider(
                                preference -> Optional.empty(),
                                (preference, hostOfPreference) -> Optional.empty()),
                        getPreferenceManager(rootPreferenceFragmentClassName, fragments));

        // When
        final Set<PreferenceScreenWithHostClass> preferenceScreens =
                preferenceScreensProvider
                        .getConnectedPreferenceScreens(rootPreferenceFragmentClassName, SearchablePreferenceScreenGraphDAOProvider.Mode.COMPUTE_AND_PERSIST_GRAPH)
                        .connectedSearchablePreferenceScreens();

        // Then
        assertThat(
                preferenceScreens,
                is(ImmutableSet.of(getPreferenceScreenByName(preferenceScreens, "first screen"))));
    }

    public static PreferenceManager getPreferenceManager(final String rootPreferenceFragmentClassName,
                                                         final Fragments fragments) {
        return ((PreferenceFragmentCompat) fragments
                .instantiateAndInitializeFragment(
                        rootPreferenceFragmentClassName,
                        Optional.empty()))
                .getPreferenceManager();
    }

    public static class FragmentConnectedToNonPreferenceFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "first screen",
                    ImmutableList.of(NonPreferenceFragment.class));
        }
    }

    public static class NonPreferenceFragment extends Fragment {
    }
}