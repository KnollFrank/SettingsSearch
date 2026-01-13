package de.KnollFrank.lib.settingssearch.graph;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static de.KnollFrank.lib.settingssearch.search.PreferenceSearcherTest.createFragmentFactoryReturning;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableBiMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.DefaultPreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceToSearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogsFactory;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerRegistry;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.IconProvider;
import de.KnollFrank.lib.settingssearch.search.ReflectionIconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenTreeProviderTest {

    @Test
    public void shouldInvokeComputePreferencesListener() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final ComputePreferencesListener computePreferencesListener = mock(ComputePreferencesListener.class);
                final PreferenceFragmentCompat preferenceFragment =
                        PreferenceFragmentFactory.fromSinglePreference(
                                context -> {
                                    final CheckBoxPreference preference = new CheckBoxPreference(context);
                                    preference.setKey("fourthfile");
                                    preference.setTitle(String.format("Checkbox %s file", "fourth"));
                                    return preference;
                                });
                final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider =
                        createPreferenceScreenWithHostProvider(
                                fragmentActivity,
                                preferenceFragment);
                final SearchablePreferenceScreenTreeProvider searchablePreferenceScreenTreeProvider =
                        createSearchablePreferenceScreenGraphProvider(
                                computePreferencesListener,
                                fragmentActivity,
                                preferenceScreenWithHostProvider);

                // When
                searchablePreferenceScreenTreeProvider.getSearchablePreferenceScreenTree(
                        preferenceScreenWithHostProvider
                                .getPreferenceScreenWithHostOfFragment(
                                        preferenceFragment.getClass(),
                                        Optional.empty())
                                .orElseThrow());

                // Then
                verify(computePreferencesListener).onStartComputePreferences();
                verify(computePreferencesListener).onFinishComputePreferences();
            });
        }
    }

    private static PreferenceScreenWithHostProvider createPreferenceScreenWithHostProvider(
            final TestActivity fragmentActivity,
            final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHostProvider(
                new Fragments(
                        new FragmentFactoryAndInitializerRegistry(
                                new FragmentFactoryAndInitializer(
                                        createFragmentFactoryReturning(preferenceFragment),
                                        FragmentInitializerFactory.createFragmentInitializer(
                                                fragmentActivity,
                                                TestActivity.FRAGMENT_CONTAINER_VIEW,
                                                (preference, hostOfPreference) -> preference.isVisible()))),
                        fragmentActivity),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()));
    }

    private static SearchablePreferenceScreenTreeProvider createSearchablePreferenceScreenGraphProvider(
            final ComputePreferencesListener computePreferencesListener,
            final FragmentActivity fragmentActivity,
            final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        return new SearchablePreferenceScreenTreeProvider(
                preferenceScreenGraph -> {
                },
                computePreferencesListener,
                new TreeToPojoTreeTransformer(
                        new PreferenceScreenToSearchablePreferenceScreenConverter(
                                new PreferenceToSearchablePreferenceConverter(
                                        new IconProvider(new ReflectionIconResourceIdProvider()),
                                        new SearchableInfoAndDialogInfoProvider(
                                                new BuiltinSearchableInfoProvider(),
                                                new SearchableDialogInfoOfProvider(
                                                        PreferenceDialogsFactory.createPreferenceDialogs(
                                                                fragmentActivity,
                                                                TestActivity.FRAGMENT_CONTAINER_VIEW,
                                                                (preference, hostOfPreference) -> preference.isVisible()),
                                                        (preference, hostOfPreference) -> Optional.empty())))),
                        new DefaultPreferenceFragmentIdProvider()),
                PreferenceScreenTreeProviderFactory.createPreferenceScreenTreeProvider(
                        preferenceScreenWithHostProvider,
                        (preference, hostOfPreference) -> Optional.empty(),
                        activityClass -> Optional.empty(),
                        (edge, sourceNodeOfEdge, targetNodeOfEdge) -> true,
                        fragmentActivity,
                        preferenceScreenWithHost -> {
                        }),
                Locale.GERMAN);
    }
}
