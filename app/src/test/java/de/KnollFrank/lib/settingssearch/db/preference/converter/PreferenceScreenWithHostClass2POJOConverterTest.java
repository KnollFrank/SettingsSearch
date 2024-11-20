package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
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
                                final SearchablePreference searchablePreference = createParent(context);
                                screen.addPreference(searchablePreference);
                                searchablePreference.addPreference(createChild(context, Optional.of("some searchable info of first child")));
                                searchablePreference.addPreference(createChild(context, Optional.of("some searchable info of second child")));
                            }

                            private static SearchablePreference createParent(final Context context) {
                                final SearchablePreference searchablePreference =
                                        new SearchablePreference(
                                                context,
                                                Optional.of("some searchable info"),
                                                Optional.empty());
                                searchablePreference.setKey("parentKey");
                                searchablePreference.setLayoutResource(15);
                                return searchablePreference;
                            }

                            private static SearchablePreference createChild(final Context context, final Optional<String> searchableInfo) {
                                final SearchablePreference child =
                                        new SearchablePreference(
                                                context,
                                                searchableInfo,
                                                Optional.empty());
                                child.setLayoutResource(16);
                                return child;
                            }
                        };
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(addPreferences2Screen);
                final PreferenceScreenWithHostClass entity =
                        new PreferenceScreenWithHostClass(
                                getPreferenceScreen(preferenceFragment, activity),
                                preferenceFragment.getClass());
                final int id = 4711;
                final IdGenerator idGenerator = new IdGenerator();

                // When
                final PreferenceScreenWithHostClassPOJO pojo =
                        PreferenceScreenWithHostClass2POJOConverter
                                .convert2POJO(entity, id, idGenerator)
                                .preferenceScreenWithHostClass();

                // Then
                assertThat(pojo, is(
                        new PreferenceScreenWithHostClassPOJO(
                                id,
                                new SearchablePreferenceScreenPOJO(
                                        "screen title",
                                        "screen summary",
                                        List.of(
                                                new SearchablePreferencePOJO(
                                                        1,
                                                        Optional.of("parentKey"),
                                                        Optional.empty(),
                                                        15,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        0,
                                                        Optional.empty(),
                                                        true,
                                                        Optional.of("some searchable info"),
                                                        new Bundle(),
                                                        List.of(
                                                                new SearchablePreferencePOJO(
                                                                        2,
                                                                        Optional.empty(),
                                                                        Optional.empty(),
                                                                        16,
                                                                        Optional.empty(),
                                                                        Optional.empty(),
                                                                        0,
                                                                        Optional.empty(),
                                                                        true,
                                                                        Optional.of("some searchable info of first child"),
                                                                        new Bundle(),
                                                                        List.of()),
                                                                new SearchablePreferencePOJO(
                                                                        3,
                                                                        Optional.empty(),
                                                                        Optional.empty(),
                                                                        16,
                                                                        Optional.empty(),
                                                                        Optional.empty(),
                                                                        0,
                                                                        Optional.empty(),
                                                                        true,
                                                                        Optional.of("some searchable info of second child"),
                                                                        new Bundle(),
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
        return initializeFragment(preferenceFragment, getFragments(preferenceFragment, activity));
    }

    public static Fragments getFragments(final Fragment fragment, final FragmentActivity activity) {
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
        return (fragmentClassName, src, context) ->
                fragment.getClass().getName().equals(fragmentClassName) ?
                        fragment :
                        defaultFragmentFactory.instantiate(fragmentClassName, src, context);
    }

    public static Fragment initializeFragment(final Fragment fragment, final Fragments fragments) {
        return fragments.instantiateAndInitializeFragment(
                fragment.getClass().getName(),
                Optional.empty());
    }
}
