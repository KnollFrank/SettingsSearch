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

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreference2POJOConverterTest {

    @Test
    public void shouldConvert2POJO() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final BiConsumer<PreferenceScreen, Context> addPreferences2Screen =
                        new BiConsumer<>() {

                            @Override
                            public void accept(final PreferenceScreen screen, final Context context) {
                                final SearchablePreference searchablePreference = createSearchablePreference1(context);
                                screen.addPreference(searchablePreference);
                                searchablePreference.addPreference(createSearchablePreference2(context));
                            }

                            private static SearchablePreference createSearchablePreference1(final Context context) {
                                final SearchablePreference searchablePreference = new SearchablePreference(context, Optional.of("some searchable info"));
                                searchablePreference.setKey("someKey");
                                searchablePreference.setLayoutResource(15);
                                return searchablePreference;
                            }

                            private static SearchablePreference createSearchablePreference2(final Context context) {
                                final SearchablePreference child = new SearchablePreference(context, Optional.of("some searchable info of child"));
                                child.setLayoutResource(16);
                                return child;
                            }
                        };
                final PreferenceScreen preferenceScreen =
                        getPreferenceScreen(
                                new PreferenceFragmentTemplate(addPreferences2Screen),
                                activity);
                final SearchablePreference searchablePreference = preferenceScreen.findPreference("someKey");

                // When
                final SearchablePreferencePOJO searchablePreferencePOJO = SearchablePreference2POJOConverter.convert2POJO(searchablePreference);

                // Then
                final SearchablePreferencePOJO result =
                        new SearchablePreferencePOJO(
                                0,
                                "someKey",
                                0,
                                15,
                                null,
                                null,
                                0,
                                null,
                                true,
                                "some searchable info",
                                List.of(
                                        new SearchablePreferencePOJO(
                                                1,
                                                null,
                                                0,
                                                16,
                                                null,
                                                null,
                                                0,
                                                null,
                                                true,
                                                "some searchable info of child",
                                                List.of())));
                assertThat(searchablePreferencePOJO, is(result));
            });
        }
    }

    private static PreferenceScreen getPreferenceScreen(final PreferenceFragmentCompat preferenceFragment,
                                                        final FragmentActivity activity) {
        return SearchablePreference2POJOConverterTest
                .initializeFragment(
                        preferenceFragment,
                        new Fragments(
                                new FragmentFactoryAndInitializerWithCache(
                                        new FragmentFactoryAndInitializer(
                                                createFragmentFactoryReturning(preferenceFragment),
                                                new DefaultFragmentInitializer(
                                                        activity.getSupportFragmentManager(),
                                                        TestActivity.FRAGMENT_CONTAINER_VIEW))),
                                activity))
                .getPreferenceScreen();
    }

    private static FragmentFactory createFragmentFactoryReturning(final PreferenceFragmentCompat preferenceFragment) {
        final DefaultFragmentFactory defaultFragmentFactory = new DefaultFragmentFactory();
        return (fragmentClassName, src, context) ->
                preferenceFragment.getClass().getName().equals(fragmentClassName) ?
                        preferenceFragment :
                        defaultFragmentFactory.instantiate(fragmentClassName, src, context);
    }

    private static PreferenceFragmentCompat initializeFragment(final PreferenceFragmentCompat preferenceFragment,
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
            addPreferences2Screen.accept(screen, context);
            setPreferenceScreen(screen);
        }
    }
}
