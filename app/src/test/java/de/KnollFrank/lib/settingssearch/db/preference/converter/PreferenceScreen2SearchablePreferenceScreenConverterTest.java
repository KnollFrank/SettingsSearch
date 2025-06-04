package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.test.SearchablePreferenceScreenEquality.assertActualEqualsExpected;

import android.content.Context;

import androidx.annotation.LayoutRes;
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

import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceScreen2SearchablePreferenceScreenConverterTest {

    @Test
    public void shouldConvertPreferenceScreen() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceScreen2SearchablePreferenceScreenConverter preferenceScreen2SearchablePreferenceScreenConverter =
                        new PreferenceScreen2SearchablePreferenceScreenConverter(
                                createPreference2SearchablePreferenceConverter());

                final String parentKey = "parentKey";
                final @LayoutRes int layoutResIdOfParent = 15;

                final String keyOfChild1 = "some child key 1";
                final String keyOfChild2 = "some child key 2";
                final @LayoutRes int layoutResIdOfEachChild = 16;

                final PreferenceFragmentCompat preferenceFragment = createPreferenceFragmentHavingParentWithTwoChildren(parentKey, layoutResIdOfParent, keyOfChild1, keyOfChild2, layoutResIdOfEachChild);
                final String id = "some unique id";

                // When
                final SearchablePreferenceScreenEntity pojo =
                        preferenceScreen2SearchablePreferenceScreenConverter
                                .convertPreferenceScreen(
                                        getPreferenceScreen(preferenceFragment, activity),
                                        preferenceFragment,
                                        id,
                                        Optional.empty())
                                .searchablePreferenceScreen();

                // Then
                assertActualEqualsExpected(
                        pojo,
                        getSearchablePreferenceScreenHavingParentWithTwoChildren(
                                id,
                                parentKey,
                                layoutResIdOfParent,

                                keyOfChild1,
                                keyOfChild2,
                                layoutResIdOfEachChild,
                                preferenceFragment.getClass()));
            });
        }
    }

    private static Preference2SearchablePreferenceConverter createPreference2SearchablePreferenceConverter() {
        return new Preference2SearchablePreferenceConverter(
                (preference, hostOfPreference) -> Optional.empty(),
                new SearchableInfoAndDialogInfoProvider(
                        preference -> Optional.empty(),
                        (preference, hostOfPreference) -> Optional.empty()),
                IdGeneratorFactory.createIdGeneratorStartingAt(1));
    }

    private static PreferenceFragmentCompat createPreferenceFragmentHavingParentWithTwoChildren(
            final String parentKey,
            final @LayoutRes int layoutResIdOfParent,

            final String keyOfChild1,
            final String keyOfChild2,
            final @LayoutRes int layoutResIdOfEachChild) {
        return new PreferenceFragmentTemplate(
                new BiConsumer<>() {

                    @Override
                    public void accept(final PreferenceScreen screen, final Context context) {
                        final PreferenceCategory preference = createParent(context);
                        screen.addPreference(preference);
                        preference.addPreference(createChild(keyOfChild1, context));
                        preference.addPreference(createChild(keyOfChild2, context));
                    }

                    private PreferenceCategory createParent(final Context context) {
                        final PreferenceCategory preference = new PreferenceCategory(context);
                        preference.setKey(parentKey);
                        preference.setLayoutResource(layoutResIdOfParent);
                        return preference;
                    }

                    private Preference createChild(final String key, final Context context) {
                        final Preference preference = new Preference(context);
                        preference.setKey(key);
                        preference.setLayoutResource(layoutResIdOfEachChild);
                        return preference;
                    }
                });
    }

    private static SearchablePreferenceScreenEntity getSearchablePreferenceScreenHavingParentWithTwoChildren(
            final String id,

            final String parentKey,
            final @LayoutRes int layoutResIdOfParent,

            final String keyOfChild1,
            final String keyOfChild2,
            final @LayoutRes int layoutResIdOfEachChild,
            final Class<? extends PreferenceFragmentCompat> host) {
        final SearchablePreferenceEntity parent =
                new SearchablePreferenceEntity(
                        1,
                        parentKey,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        layoutResIdOfParent,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        id);
        return new SearchablePreferenceScreenEntity(
                id,
                host,
                Optional.of("screen title"),
                Optional.of("screen summary"),
                Set.of(
                        parent,
                        new SearchablePreferenceEntity(
                                2,
                                keyOfChild1,
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty(),
                                layoutResIdOfEachChild,
                                0,
                                Optional.empty(),
                                Optional.empty(),
                                true,
                                Optional.empty(),
                                Optional.of(1),
                                Optional.empty(),
                                id),
                        new SearchablePreferenceEntity(
                                3,
                                keyOfChild2,
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty(),
                                layoutResIdOfEachChild,
                                0,
                                Optional.empty(),
                                Optional.empty(),
                                true,
                                Optional.empty(),
                                Optional.of(1),
                                Optional.empty(),
                                id)));
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
                                FragmentInitializerFactory.createFragmentInitializer(
                                        activity,
                                        TestActivity.FRAGMENT_CONTAINER_VIEW))),
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
