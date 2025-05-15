package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.db.preference.dao.BundleTestFactory.createBundle;
import static de.KnollFrank.lib.settingssearch.test.SearchablePreferenceScreenEquality.assertActualEqualsExpected;

import android.content.Context;
import android.os.Bundle;

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

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.BundleWithEquality;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.HostWithArguments;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceScreenWithHost2POJOConverterTest {

    @Test
    public void shouldConvertPreferenceScreenWithHost2POJO() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String parentKey = "parentKey";
                final @LayoutRes int layoutResIdOfParent = 15;

                final String keyOfChild1 = "some child key 1";
                final String keyOfChild2 = "some child key 2";
                final @LayoutRes int layoutResIdOfEachChild = 16;

                final PreferenceFragmentCompat preferenceFragment = createPreferenceFragmentHavingParentWithTwoChildren(parentKey, layoutResIdOfParent, keyOfChild1, keyOfChild2, layoutResIdOfEachChild, createBundle("someKey", "someValue"));
                final String id = "some unique id";

                // When
                final SearchablePreferenceScreen pojo =
                        PreferenceScreenWithHost2POJOConverter
                                .convert2POJO(
                                        new PreferenceScreenWithHost(
                                                getPreferenceScreen(preferenceFragment, activity),
                                                preferenceFragment),
                                        id,
                                        Optional.empty(),
                                        new Preference2SearchablePreferenceConverter(
                                                (preference, hostOfPreference) -> Optional.empty(),
                                                new SearchableInfoAndDialogInfoProvider(
                                                        preference -> Optional.empty(),
                                                        (preference, hostOfPreference) -> Optional.empty()),
                                                IdGeneratorFactory.createIdGeneratorStartingAt(1)),
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
                                new HostWithArguments(
                                        preferenceFragment.getClass(),
                                        Optional
                                                .ofNullable(preferenceFragment.getArguments())
                                                .map(BundleWithEquality::new))));
            });
        }
    }

    private static PreferenceFragmentCompat createPreferenceFragmentHavingParentWithTwoChildren(
            final String parentKey,
            final @LayoutRes int layoutResIdOfParent,

            final String keyOfChild1,
            final String keyOfChild2,
            final @LayoutRes int layoutResIdOfEachChild,
            final Bundle arguments) {
        final PreferenceFragmentTemplate preferenceFragment =
                new PreferenceFragmentTemplate(
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
        preferenceFragment.setArguments(arguments);
        return preferenceFragment;
    }

    private static SearchablePreferenceScreen getSearchablePreferenceScreenHavingParentWithTwoChildren(
            final String id,

            final String parentKey,
            final @LayoutRes int layoutResIdOfParent,

            final String keyOfChild1,
            final String keyOfChild2,
            final @LayoutRes int layoutResIdOfEachChild,
            final HostWithArguments hostWithArguments) {
        final SearchablePreference parent =
                new SearchablePreference(
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
                        new Bundle(),
                        Optional.empty(),
                        Optional.empty(),
                        id);
        return new SearchablePreferenceScreen(
                id,
                hostWithArguments,
                Optional.of("screen title"),
                Optional.of("screen summary"),
                Set.of(
                        parent,
                        new SearchablePreference(
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
                                new Bundle(),
                                Optional.of(1),
                                Optional.empty(),
                                id),
                        new SearchablePreference(
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
                                new Bundle(),
                                Optional.of(1),
                                Optional.empty(),
                                id)),
                Optional.empty());
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
