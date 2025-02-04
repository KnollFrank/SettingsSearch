package de.KnollFrank.settingssearch;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableMap;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Optional;

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

    public record ActivitySearchDatabaseConfig<A extends Activity, F extends Fragment, P1 extends PreferenceFragmentCompat, P2 extends PreferenceFragmentCompat>(
            ActivityWithRootPreferenceFragmentConnection<A, P1> activityWithRootPreferenceFragmentConnection,
            Optional<Pair<FragmentWithPreferenceFragmentConnection<F, P2>, PreferenceFragmentInitializer<P2, F>>> fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer) {

        public P2 createPreferenceFragment(final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
            final P2 preferenceFragment = _createPreferenceFragment(src, context, fragments);
            initializePreferenceFragmentWithFragment(preferenceFragment, getFragment(fragments));
            return preferenceFragment;
        }

        private P2 _createPreferenceFragment(final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
            return new DefaultFragmentFactory().instantiate(
                    fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer().orElseThrow().first.preferenceFragment(),
                    src,
                    context,
                    fragments);
        }

        private F getFragment(final IFragments fragments) {
            return fragments.instantiateAndInitializeFragment(
                    fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer().orElseThrow().first.fragment(),
                    Optional.empty());
        }

        private void initializePreferenceFragmentWithFragment(final P2 preferenceFragment, final F fragment) {
            fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer()
                    .orElseThrow()
                    .second
                    .initializePreferenceFragmentWithFragment(
                            preferenceFragment,
                            fragment);
        }
    }

    public record ActivityWithRootPreferenceFragmentConnection<A extends Activity, P extends PreferenceFragmentCompat>(
            Class<A> activityClass,
            Class<P> rootPreferenceFragmentClassOfActivityClass) {
    }

    public record FragmentWithPreferenceFragmentConnection<F extends Fragment, P extends PreferenceFragmentCompat>(
            Class<F> fragment,
            Class<P> preferenceFragment) {
    }

    @FunctionalInterface
    public interface PreferenceFragmentInitializer<P extends PreferenceFragmentCompat, F extends Fragment> {

        void initializePreferenceFragmentWithFragment(P preferenceFragment, F fragment);
    }

    public static SearchDatabaseConfig createSearchDatabaseConfig() {
        final var activitySearchDatabaseConfig =
                new ActivitySearchDatabaseConfig<SettingsActivity, ItemFragment, SettingsFragment, ItemFragment.PreferenceFragment>(
                        new ActivityWithRootPreferenceFragmentConnection<>(SettingsActivity.class, SettingsFragment.class),
                        Optional.of(
                                Pair.create(
                                        new FragmentWithPreferenceFragmentConnection<>(ItemFragment.class, ItemFragment.PreferenceFragment.class),
                                        ItemFragment.PreferenceFragment::beforeOnCreate)));
        final var activitySearchDatabaseConfig2 =
                new ActivitySearchDatabaseConfig<SettingsActivity2, Fragment, SettingsFragment2, PreferenceFragmentCompat>(
                        new ActivityWithRootPreferenceFragmentConnection<>(SettingsActivity2.class, SettingsFragment2.class),
                        Optional.empty());
        final var activitySearchDatabaseConfig3 =
                new ActivitySearchDatabaseConfig<SettingsActivity3, ItemFragment3, ItemFragment3.PreferenceFragment3, ItemFragment3.PreferenceFragment3>(
                        new ActivityWithRootPreferenceFragmentConnection<>(SettingsActivity3.class, ItemFragment3.PreferenceFragment3.class),
                        Optional.of(
                                Pair.create(
                                        new FragmentWithPreferenceFragmentConnection<>(ItemFragment3.class, ItemFragment3.PreferenceFragment3.class),
                                        ItemFragment3.PreferenceFragment3::beforeOnCreate)));
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
                                if (activitySearchDatabaseConfig.fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer().orElseThrow().first.preferenceFragment().equals(fragmentClass)) {
                                    return (T) activitySearchDatabaseConfig.createPreferenceFragment(src, context, fragments);
                                }
                                if (activitySearchDatabaseConfig3.fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer().orElseThrow().first.preferenceFragment().equals(fragmentClass)) {
                                    return (T) activitySearchDatabaseConfig3.createPreferenceFragment(src, context, fragments);
                                }
                                return new DefaultFragmentFactory().instantiate(fragmentClass, src, context, fragments);
                            }
                        })
                // FK-TODO: combine withRootPreferenceFragmentOfActivityProvider() and withFragment2PreferenceFragmentConverterFactory()?
                .withRootPreferenceFragmentOfActivityProvider(
                        new RootPreferenceFragmentOfActivityProvider() {

                            @Override
                            public Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentOfActivity(final Class<? extends Activity> activityClass) {
                                if (activitySearchDatabaseConfig.activityWithRootPreferenceFragmentConnection().activityClass().equals(activityClass)) {
                                    return Optional.of(activitySearchDatabaseConfig.activityWithRootPreferenceFragmentConnection().rootPreferenceFragmentClassOfActivityClass());
                                }
                                if (activitySearchDatabaseConfig2.activityWithRootPreferenceFragmentConnection().activityClass().equals(activityClass)) {
                                    return Optional.of(activitySearchDatabaseConfig2.activityWithRootPreferenceFragmentConnection().rootPreferenceFragmentClassOfActivityClass());
                                }
                                if (activitySearchDatabaseConfig3.activityWithRootPreferenceFragmentConnection().activityClass().equals(activityClass)) {
                                    return Optional.of(activitySearchDatabaseConfig3.activityWithRootPreferenceFragmentConnection().rootPreferenceFragmentClassOfActivityClass());
                                }
                                return Optional.empty();
                            }
                        })
                .withFragment2PreferenceFragmentConverter(
                        new Fragment2PreferenceFragmentConverter() {

                            private final Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> preferenceFragmentByFragment =
                                    ImmutableMap
                                            .<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>>builder()
                                            .put(activitySearchDatabaseConfig.fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer().orElseThrow().first.fragment(), activitySearchDatabaseConfig.fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer().orElseThrow().first.preferenceFragment())
                                            .put(activitySearchDatabaseConfig3.fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer().orElseThrow().first.fragment(), activitySearchDatabaseConfig3.fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer().orElseThrow().first.preferenceFragment())
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
