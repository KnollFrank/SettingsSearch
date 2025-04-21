package de.KnollFrank.lib.settingssearch.search;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static de.KnollFrank.lib.settingssearch.search.PreferenceSearcherTest.createFragmentFactoryReturning;

import androidx.fragment.app.Fragment;
import androidx.preference.CheckBoxPreference;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableBiMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.ComputePreferencesListener;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphProviderTest {

    @Test
    public void shouldInvokeComputePreferencesListener() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final ComputePreferencesListener computePreferencesListener = mock(ComputePreferencesListener.class);
                final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                        createSearchablePreferenceScreenGraphProvider(
                                computePreferencesListener,
                                fragmentActivity);

                // When
                searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph();

                // Then
                verify(computePreferencesListener).onStartComputePreferences();
                verify(computePreferencesListener).onFinishComputePreferences();
            });
        }
    }

    private static SearchablePreferenceScreenGraphProvider createSearchablePreferenceScreenGraphProvider(
            final ComputePreferencesListener computePreferencesListener,
            final TestActivity fragmentActivity) {
        final Fragment preferenceFragment =
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey("fourthfile");
                            preference.setTitle(String.format("Checkbox %s file", "fourth"));
                            return preference;
                        });
        final DefaultFragmentInitializer fragmentInitializer =
                new DefaultFragmentInitializer(
                        fragmentActivity.getSupportFragmentManager(),
                        TestActivity.FRAGMENT_CONTAINER_VIEW,
                        OnUiThreadRunnerFactory.fromActivity(fragmentActivity));
        return new SearchablePreferenceScreenGraphProvider(
                preferenceFragment.getClass(),
                new PreferenceScreenWithHostProvider(
                        new Fragments(
                                new FragmentFactoryAndInitializerWithCache(
                                        new FragmentFactoryAndInitializer(
                                                createFragmentFactoryReturning(preferenceFragment),
                                                fragmentInitializer)),
                                fragmentActivity),
                        new SearchablePreferenceScreenProvider(
                                new PreferenceVisibleAndSearchablePredicate(
                                        (preference, hostOfPreference) -> true)),
                        new PrincipalAndProxyProvider(ImmutableBiMap.of())),
                (preference, hostOfPreference) -> Optional.empty(),
                activityClass -> Optional.empty(),
                preferenceScreenGraph -> {
                },
                preferenceScreenWithHost -> {
                },
                computePreferencesListener,
                new Preference2SearchablePreferenceConverter(
                        new IconProvider(new ReflectionIconResourceIdProvider()),
                        new SearchableInfoAndDialogInfoProvider(
                                new BuiltinSearchableInfoProvider(),
                                new SearchableDialogInfoOfProvider(
                                        fragmentInitializer,
                                        (preference, hostOfPreference) -> Optional.empty())),
                        IdGeneratorFactory.createIdGeneratorStartingAt1()),
                fragmentActivity);
    }
}
