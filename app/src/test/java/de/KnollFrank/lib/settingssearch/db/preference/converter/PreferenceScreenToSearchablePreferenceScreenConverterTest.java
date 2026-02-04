package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.test.SearchablePreferenceScreenEquality.assertActualEqualsExpected;

import android.content.Context;
import android.os.PersistableBundle;

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

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.InstantiateAndInitializeFragmentFactory;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceScreenToSearchablePreferenceScreenConverterTest {

    @Test
    public void shouldConvertPreferenceScreen() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceScreenToSearchablePreferenceScreenConverter preferenceScreenToSearchablePreferenceScreenConverter =
                        new PreferenceScreenToSearchablePreferenceScreenConverter(
                                createPreferenceToSearchablePreferenceConverter());

                final String parentKey = "parentKey";
                final @LayoutRes int layoutResIdOfParent = 15;

                final String keyOfChild1 = "some child key 1";
                final String keyOfChild2 = "some child key 2";
                final @LayoutRes int layoutResIdOfEachChild = 16;

                final PreferenceFragmentCompat preferenceFragment = createPreferenceFragmentHavingParentWithTwoChildren(parentKey, layoutResIdOfParent, keyOfChild1, keyOfChild2, layoutResIdOfEachChild);
                final String id = "some unique id";

                // When
                final PreferenceScreenOfHostOfActivity preferenceScreen =
                        new PreferenceScreenOfHostOfActivity(
                                getPreferenceScreen(preferenceFragment, activity),
                                preferenceFragment,
                                new ActivityDescription(
                                        activity.getClass(),
                                        new PersistableBundle()));
                final SearchablePreferenceScreen pojo =
                        preferenceScreenToSearchablePreferenceScreenConverter
                                .convertPreferenceScreen(preferenceScreen, id)
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
                                preferenceScreen
                                        .asPreferenceFragmentOfActivity()
                                        .asPreferenceFragmentClassOfActivity()));
            });
        }
    }

    private static PreferenceToSearchablePreferenceConverter createPreferenceToSearchablePreferenceConverter() {
        return new PreferenceToSearchablePreferenceConverter(
                (preference, hostOfPreference) -> Optional.empty(),
                new SearchableInfoAndDialogInfoProvider(
                        preference -> Optional.empty(),
                        (preference, hostOfPreference) -> Optional.empty()));
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

    private static SearchablePreferenceScreen getSearchablePreferenceScreenHavingParentWithTwoChildren(
            final String id,

            final String parentKey,
            final @LayoutRes int layoutResIdOfParent,

            final String keyOfChild1,
            final String keyOfChild2,
            final @LayoutRes int layoutResIdOfEachChild,
            final FragmentClassOfActivity<? extends PreferenceFragmentCompat> host) {
        final SearchablePreference child1 =
                new SearchablePreference(
                        id + "-0-0",
                        keyOfChild1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        layoutResIdOfEachChild,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        new PersistableBundle(),
                        Optional.empty(),
                        Set.of());
        final SearchablePreference child2 =
                new SearchablePreference(
                        id + "-0-1",
                        keyOfChild2,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        layoutResIdOfEachChild,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        new PersistableBundle(),
                        Optional.empty(),
                        Set.of());
        final SearchablePreference parent =
                new SearchablePreference(
                        id + "-0",
                        parentKey,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        layoutResIdOfParent,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        new PersistableBundle(),
                        Optional.empty(),
                        Set.of(child1, child2));
        return new SearchablePreferenceScreen(
                id,
                host,
                Optional.of("screen title"),
                Optional.of("screen summary"),
                Set.of(parent, child1, child2));
    }

    public static PreferenceScreen getPreferenceScreen(final PreferenceFragmentCompat preferenceFragment,
                                                       final FragmentActivity activity) {
        return ((PreferenceFragmentCompat) initializeFragment(preferenceFragment, activity)).getPreferenceScreen();
    }

    public static Fragment initializeFragment(final Fragment preferenceFragment,
                                              final FragmentActivity activity) {
        return initializeFragment(
                new FragmentClassOfActivity<>(
                        preferenceFragment.getClass(),
                        new ActivityDescription(
                                activity.getClass(),
                                new PersistableBundle())),
                InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment(preferenceFragment, activity));
    }

    public static Fragment initializeFragment(final FragmentClassOfActivity<? extends Fragment> fragment,
                                              final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return instantiateAndInitializeFragment.instantiateAndInitializeFragment(
                fragment,
                Optional.empty());
    }
}
