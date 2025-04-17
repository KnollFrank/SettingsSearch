package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceScreenWithHostClass2POJOConverterTest {

    @Test
    public void shouldConvertPreferenceScreenWithHostClass2POJO() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final BiConsumer<PreferenceScreen, Context> addPreferences2Screen =
                        new BiConsumer<>() {

                            @Override
                            public void accept(final PreferenceScreen screen, final Context context) {
                                final PreferenceCategory preference = createParent(context);
                                screen.addPreference(preference);
                                preference.addPreference(createChild(context));
                                preference.addPreference(createChild(context));
                            }

                            private static PreferenceCategory createParent(final Context context) {
                                final PreferenceCategory preference = new PreferenceCategory(context);
                                preference.setKey("parentKey");
                                preference.setLayoutResource(15);
                                return preference;
                            }

                            private static Preference createChild(final Context context) {
                                final Preference preference = new Preference(context);
                                preference.setKey("some key");
                                preference.setLayoutResource(16);
                                return preference;
                            }
                        };
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(addPreferences2Screen);
                final PreferenceScreenWithHost entity =
                        new PreferenceScreenWithHost(
                                getPreferenceScreen(preferenceFragment, activity),
                                preferenceFragment);
                final int id = 4711;
                final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter =
                        new Preference2SearchablePreferenceConverter(
                                (preference, hostOfPreference) -> Optional.empty(),
                                new SearchableInfoAndDialogInfoProvider(
                                        preference -> Optional.empty(),
                                        (preference, hostOfPreference) -> Optional.empty()),
                                new IdGenerator());

                // When
                final PreferenceScreenWithHostClass pojo =
                        PreferenceScreenWithHostClass2POJOConverter
                                .convert2POJO(entity, id, preference2SearchablePreferenceConverter)
                                .preferenceScreenWithHostClass();

                // Then
                assertThat(pojo, is(
                        new PreferenceScreenWithHostClass(
                                id,
                                new SearchablePreferenceScreen(
                                        "screen title",
                                        "screen summary",
                                        List.of(
                                                new SearchablePreference(
                                                        1,
                                                        "parentKey",
                                                        Optional.empty(),
                                                        15,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        0,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        true,
                                                        Optional.of("some searchable info"),
                                                        new Bundle(),
                                                        preferenceFragment.getClass(),
                                                        List.of(
                                                                new SearchablePreference(
                                                                        2,
                                                                        "some child key 1",
                                                                        Optional.empty(),
                                                                        16,
                                                                        Optional.empty(),
                                                                        Optional.empty(),
                                                                        0,
                                                                        Optional.empty(),
                                                                        Optional.empty(),
                                                                        true,
                                                                        Optional.of("some searchable info of first child"),
                                                                        new Bundle(),
                                                                        preferenceFragment.getClass(),
                                                                        List.of()),
                                                                new SearchablePreference(
                                                                        3,
                                                                        "some child key 2",
                                                                        Optional.empty(),
                                                                        16,
                                                                        Optional.empty(),
                                                                        Optional.empty(),
                                                                        0,
                                                                        Optional.empty(),
                                                                        Optional.empty(),
                                                                        true,
                                                                        Optional.of("some searchable info of second child"),
                                                                        new Bundle(),
                                                                        preferenceFragment.getClass(),
                                                                        List.of()))))),
                                preferenceFragment.getClass())));
            });
        }
    }

    public static PreferenceScreen getPreferenceScreen(final PreferenceFragmentCompat preferenceFragment,
                                                       final FragmentActivity activity) {
        return ((PreferenceFragmentCompat) initializeFragment(preferenceFragment, activity)).getPreferenceScreen();
    }

    public static Fragment initializeFragment(final Fragment preferenceFragment,
                                              final FragmentActivity activity) {
        return initializeFragment(preferenceFragment, getInstantiateAndInitializeFragment(preferenceFragment, activity));
    }

    public static InstantiateAndInitializeFragment getInstantiateAndInitializeFragment(final Fragment fragment,
                                                                                       final FragmentActivity activity) {
        return new Fragments(
                new FragmentFactoryAndInitializerWithCache(
                        new FragmentFactoryAndInitializer(
                                createFragmentFactoryReturning(fragment),
                                new DefaultFragmentInitializer(
                                        activity.getSupportFragmentManager(),
                                        TestActivity.FRAGMENT_CONTAINER_VIEW,
                                        OnUiThreadRunnerFactory.fromActivity(activity)))),
                activity);
    }

    private static FragmentFactory createFragmentFactoryReturning(final Fragment fragment) {
        final DefaultFragmentFactory defaultFragmentFactory = new DefaultFragmentFactory();
        return new FragmentFactory() {

            @Override
            public <T extends Fragment> T instantiate(final Class<T> fragmentClassName,
                                                      final Optional<PreferenceWithHost> src,
                                                      final Context context,
                                                      final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
                return fragment.getClass().equals(fragmentClassName) ?
                        (T) fragment :
                        defaultFragmentFactory.instantiate(fragmentClassName, src, context, instantiateAndInitializeFragment);
            }
        };
    }

    public static Fragment initializeFragment(final Fragment fragment, final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return instantiateAndInitializeFragment.instantiateAndInitializeFragment(
                fragment.getClass(),
                Optional.empty());
    }
}
