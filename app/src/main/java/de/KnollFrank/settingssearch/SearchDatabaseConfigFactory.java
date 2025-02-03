package de.KnollFrank.settingssearch;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableMap;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.client.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.client.SearchDatabaseConfigBuilder;
import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.IFragments;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;
import de.KnollFrank.settingssearch.SettingsActivity.SettingsFragment;
import de.KnollFrank.settingssearch.SettingsActivity2.SettingsFragment2;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.settingssearch.preference.fragment.CustomDialogFragment;
import de.KnollFrank.settingssearch.preference.fragment.ItemFragment;
import de.KnollFrank.settingssearch.preference.fragment.ItemFragment3;
import de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentSecond;

class SearchDatabaseConfigFactory {

    public record ActivitySearchDatabaseConfig<T extends Activity, U extends PreferenceFragmentCompat, V extends PreferenceFragmentCompat>(
            Class<T> activityClass,
            Class<U> rootPreferenceFragmentClassOfActivityClass,
            Optional<FragmentWithPreferenceFragmentConnection<V>> fragmentWithPreferenceFragmentConnection,
            Optional<BiConsumer<V, IFragments>> initializePreferenceFragment) {
    }

    public record FragmentWithPreferenceFragmentConnection<T extends PreferenceFragmentCompat>(
            Class<? extends Fragment> fragment,
            Class<T> preferenceFragment) {
    }

    public static SearchDatabaseConfig createSearchDatabaseConfig() {
        final var activitySearchDatabaseConfig =
                new ActivitySearchDatabaseConfig<SettingsActivity, SettingsFragment, ItemFragment.PreferenceFragment>(
                        SettingsActivity.class,
                        SettingsFragment.class,
                        Optional.of(new FragmentWithPreferenceFragmentConnection<>(ItemFragment.class, ItemFragment.PreferenceFragment.class)),
                        Optional.of(
                                (preferenceFragment, fragments) -> {
                                    final ItemFragment itemFragment = fragments.instantiateAndInitializeFragment(ItemFragment.class, Optional.empty());
                                    preferenceFragment.beforeOnCreate(itemFragment);
                                }));
        final var activitySearchDatabaseConfig2 =
                new ActivitySearchDatabaseConfig<SettingsActivity2, SettingsFragment2, PreferenceFragmentCompat>(
                        SettingsActivity2.class,
                        SettingsFragment2.class,
                        Optional.empty(),
                        Optional.empty());
        final var activitySearchDatabaseConfig3 =
                new ActivitySearchDatabaseConfig<SettingsActivity3, ItemFragment3.PreferenceFragment3, ItemFragment3.PreferenceFragment3>(
                        SettingsActivity3.class,
                        ItemFragment3.PreferenceFragment3.class,
                        Optional.of(new FragmentWithPreferenceFragmentConnection<>(ItemFragment3.class, ItemFragment3.PreferenceFragment3.class)),
                        Optional.of(
                                (preferenceFragment, fragments) -> {
                                    final ItemFragment3 itemFragment3 = fragments.instantiateAndInitializeFragment(ItemFragment3.class, Optional.empty());
                                    preferenceFragment.beforeOnCreate(itemFragment3);
                                }));
        return new SearchDatabaseConfigBuilder()
                .withFragmentFactory(
                        new FragmentFactory() {

                            @Override
                            public <T extends Fragment> T instantiate(final Class<T> fragmentClass, final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
                                if (PreferenceFragmentWithSinglePreference.class.equals(fragmentClass) &&
                                        src.isPresent() &&
                                        PrefsFragmentFirst.KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS.equals(src.get().preference().getKey())) {
                                    return Classes.instantiateFragmentClass(fragmentClass, Optional.of(PrefsFragmentFirst.createArguments4PreferenceWithoutExtras(src.get().preference())));
                                }
                                if (activitySearchDatabaseConfig.fragmentWithPreferenceFragmentConnection().orElseThrow().preferenceFragment().equals(fragmentClass)) {
                                    final ItemFragment.PreferenceFragment instantiate = new DefaultFragmentFactory().instantiate(activitySearchDatabaseConfig.fragmentWithPreferenceFragmentConnection().orElseThrow().preferenceFragment(), src, context, fragments);
                                    activitySearchDatabaseConfig.initializePreferenceFragment().orElseThrow().accept(instantiate, fragments);
                                    return (T) instantiate;
                                }
                                if (activitySearchDatabaseConfig3.fragmentWithPreferenceFragmentConnection().orElseThrow().preferenceFragment().equals(fragmentClass)) {
                                    final ItemFragment3.PreferenceFragment3 instantiate = new DefaultFragmentFactory().instantiate(activitySearchDatabaseConfig3.fragmentWithPreferenceFragmentConnection().orElseThrow().preferenceFragment(), src, context, fragments);
                                    activitySearchDatabaseConfig3.initializePreferenceFragment().orElseThrow().accept(instantiate, fragments);
                                    return (T) instantiate;
                                }
                                return new DefaultFragmentFactory().instantiate(fragmentClass, src, context, fragments);
                            }
                        })
                // FK-TODO: combine withRootPreferenceFragmentOfActivityProvider() and withFragment2PreferenceFragmentConverterFactory()?
                .withRootPreferenceFragmentOfActivityProvider(
                        new RootPreferenceFragmentOfActivityProvider() {

                            @Override
                            public Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentOfActivity(final Class<? extends Activity> activityClass) {
                                if (activitySearchDatabaseConfig.activityClass().equals(activityClass)) {
                                    return Optional.of(activitySearchDatabaseConfig.rootPreferenceFragmentClassOfActivityClass());
                                }
                                if (activitySearchDatabaseConfig2.activityClass().equals(activityClass)) {
                                    return Optional.of(activitySearchDatabaseConfig2.rootPreferenceFragmentClassOfActivityClass());
                                }
                                if (activitySearchDatabaseConfig3.activityClass().equals(activityClass)) {
                                    return Optional.of(activitySearchDatabaseConfig3.rootPreferenceFragmentClassOfActivityClass());
                                }
                                return Optional.empty();
                            }
                        })
                .withFragment2PreferenceFragmentConverter(
                        new Fragment2PreferenceFragmentConverter() {

                            private final Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> preferenceFragmentByFragment =
                                    ImmutableMap
                                            .<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>>builder()
                                            .put(activitySearchDatabaseConfig.fragmentWithPreferenceFragmentConnection().orElseThrow().fragment(), activitySearchDatabaseConfig.fragmentWithPreferenceFragmentConnection().orElseThrow().preferenceFragment())
                                            .put(activitySearchDatabaseConfig3.fragmentWithPreferenceFragmentConnection().orElseThrow().fragment(), activitySearchDatabaseConfig3.fragmentWithPreferenceFragmentConnection().orElseThrow().preferenceFragment())
                                            .build();

                            @Override
                            public Optional<Class<? extends PreferenceFragmentCompat>> asPreferenceFragment(final Class<? extends Fragment> fragment) {
                                return Maps.get(preferenceFragmentByFragment, fragment);
                            }
                        })
                .withSearchableInfoProvider(new ReversedListPreferenceSearchableInfoProvider())
                .withPreferenceFragmentConnected2PreferenceProvider(
                        new PreferenceFragmentConnected2PreferenceProvider() {

                            @Override
                            public Optional<Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentConnected2Preference(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
                                return PrefsFragmentFirst.NON_STANDARD_LINK_TO_SECOND_FRAGMENT.equals(preference.getKey()) ?
                                        Optional.of(PrefsFragmentSecond.class) :
                                        Optional.empty();
                            }
                        })
                .withPreferenceDialogAndSearchableInfoProvider(
                        new PreferenceDialogAndSearchableInfoProvider() {

                            @Override
                            public Optional<PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<?>> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
                                return preference instanceof CustomDialogPreference || "keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey()) ?
                                        Optional.of(
                                                new PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<>(
                                                        new CustomDialogFragment(),
                                                        CustomDialogFragment::getSearchableInfo)) :
                                        Optional.empty();
                            }
                        })
                .withPreferenceScreenGraphAvailableListener(
                        new PreferenceScreenGraphAvailableListener() {

                            @Override
                            public void onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
                                Log.i(this.getClass().getSimpleName(), PreferenceScreenGraph2DOTConverter.graph2DOT(preferenceScreenGraph));
                            }
                        })
                .build();
    }
}
