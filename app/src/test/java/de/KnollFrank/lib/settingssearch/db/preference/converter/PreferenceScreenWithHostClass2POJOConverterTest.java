package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.os.Bundle;

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
                final PreferenceFragmentTemplate preferenceFragment = new PreferenceFragmentTemplate(addPreferences2Screen);
                final PreferenceScreenWithHostClass entity =
                        new PreferenceScreenWithHostClass(
                                getPreferenceScreen(preferenceFragment, activity),
                                preferenceFragment.getClass());
                final int id = 4711;

                // When
                final PreferenceScreenWithHostClassPOJO pojo = PreferenceScreenWithHostClass2POJOConverter.convert2POJO(entity, id);

                // Then
                assertThat(pojo, is(
                        new PreferenceScreenWithHostClassPOJO(
                                id,
                                new SearchablePreferenceScreenPOJO(
                                        "screen title",
                                        "screen summary",
                                        List.of(
                                                new SearchablePreferencePOJO(
                                                        "parentKey",
                                                        0,
                                                        15,
                                                        null,
                                                        null,
                                                        0,
                                                        null,
                                                        true,
                                                        "some searchable info",
                                                        new Bundle(),
                                                        List.of(
                                                                new SearchablePreferencePOJO(
                                                                        null,
                                                                        0,
                                                                        16,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        true,
                                                                        "some searchable info of first child",
                                                                        new Bundle(),
                                                                        List.of(),
                                                                        Optional.empty()),
                                                                new SearchablePreferencePOJO(
                                                                        null,
                                                                        0,
                                                                        16,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        true,
                                                                        "some searchable info of second child",
                                                                        new Bundle(),
                                                                        List.of(),
                                                                        Optional.empty())),
                                                        Optional.empty()))),
                                preferenceFragment.getClass())));
            });
        }
    }

    private static PreferenceScreen getPreferenceScreen(final PreferenceFragmentCompat preferenceFragment,
                                                        final FragmentActivity activity) {
        return initializeFragment(preferenceFragment, activity).getPreferenceScreen();
    }

    public static PreferenceFragmentCompat initializeFragment(final PreferenceFragmentCompat preferenceFragment,
                                                              final FragmentActivity activity) {
        return initializeFragment(preferenceFragment, getFragments(preferenceFragment, activity));
    }

    public static Fragments getFragments(final PreferenceFragmentCompat preferenceFragment,
                                         final FragmentActivity activity) {
        return new Fragments(
                new FragmentFactoryAndInitializerWithCache(
                        new FragmentFactoryAndInitializer(
                                createFragmentFactoryReturning(preferenceFragment),
                                new DefaultFragmentInitializer(
                                        activity.getSupportFragmentManager(),
                                        TestActivity.FRAGMENT_CONTAINER_VIEW))),
                activity);
    }

    private static FragmentFactory createFragmentFactoryReturning(final PreferenceFragmentCompat preferenceFragment) {
        final DefaultFragmentFactory defaultFragmentFactory = new DefaultFragmentFactory();
        return (fragmentClassName, src, context) ->
                preferenceFragment.getClass().getName().equals(fragmentClassName) ?
                        preferenceFragment :
                        defaultFragmentFactory.instantiate(fragmentClassName, src, context);
    }

    public static PreferenceFragmentCompat initializeFragment(final PreferenceFragmentCompat preferenceFragment,
                                                              final Fragments fragments) {
        return (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                preferenceFragment.getClass().getName(),
                Optional.empty());
    }

    public static class PreferenceFragmentTemplate extends PreferenceFragmentCompat {

        private final BiConsumer<PreferenceScreen, Context> addPreferences2Screen;

        public PreferenceFragmentTemplate(final BiConsumer<PreferenceScreen, Context> addPreferences2Screen) {
            this.addPreferences2Screen = addPreferences2Screen;
        }

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            screen.setTitle("screen title");
            screen.setSummary("screen summary");
            addPreferences2Screen.accept(screen, context);
            setPreferenceScreen(screen);
        }
    }
}
