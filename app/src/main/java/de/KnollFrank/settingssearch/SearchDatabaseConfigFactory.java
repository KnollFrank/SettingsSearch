package de.KnollFrank.settingssearch;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
            Optional<FragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer<F, P2>> fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer) {
    }

    public record FragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer<F extends Fragment, P extends PreferenceFragmentCompat>(
            FragmentWithPreferenceFragmentConnection<F, P> fragmentWithPreferenceFragmentConnection,
            PreferenceFragmentInitializer<P, F> preferenceFragmentInitializer) {

        public P createPreferenceFragment(final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
            final P preferenceFragment = _createPreferenceFragment(src, context, fragments);
            initializePreferenceFragmentWithFragment(preferenceFragment, getFragment(fragments));
            return preferenceFragment;
        }

        private P _createPreferenceFragment(final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
            return new DefaultFragmentFactory().instantiate(
                    fragmentWithPreferenceFragmentConnection().preferenceFragment(),
                    src,
                    context,
                    fragments);
        }

        private F getFragment(final IFragments fragments) {
            return fragments.instantiateAndInitializeFragment(
                    fragmentWithPreferenceFragmentConnection().fragment(),
                    Optional.empty());
        }

        private void initializePreferenceFragmentWithFragment(final P preferenceFragment, final F fragment) {
            this
                    .preferenceFragmentInitializer()
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
        final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs =
                List.of(
                        new ActivitySearchDatabaseConfig<>(
                                new ActivityWithRootPreferenceFragmentConnection<>(SettingsActivity.class, SettingsFragment.class),
                                Optional.of(
                                        new FragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer<>(
                                                new FragmentWithPreferenceFragmentConnection<>(ItemFragment.class, ItemFragment.PreferenceFragment.class),
                                                ItemFragment.PreferenceFragment::beforeOnCreate))),
                        new ActivitySearchDatabaseConfig<>(
                                new ActivityWithRootPreferenceFragmentConnection<>(SettingsActivity2.class, SettingsFragment2.class),
                                Optional.empty()),
                        new ActivitySearchDatabaseConfig<>(
                                new ActivityWithRootPreferenceFragmentConnection<>(SettingsActivity3.class, ItemFragment3.PreferenceFragment3.class),
                                Optional.of(
                                        new FragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer<>(
                                                new FragmentWithPreferenceFragmentConnection<>(ItemFragment3.class, ItemFragment3.PreferenceFragment3.class),
                                                ItemFragment3.PreferenceFragment3::beforeOnCreate))));
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
                                for (final ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat> activitySearchDatabaseConfig : activitySearchDatabaseConfigs) {
                                    if (activitySearchDatabaseConfig.fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer().isPresent()) {
                                        final var pair = activitySearchDatabaseConfig.fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer().orElseThrow();
                                        if (pair.fragmentWithPreferenceFragmentConnection().preferenceFragment().equals(fragmentClass)) {
                                            return (T) pair.createPreferenceFragment(src, context, fragments);
                                        }
                                    }
                                }
                                return new DefaultFragmentFactory().instantiate(fragmentClass, src, context, fragments);
                            }
                        })
                // FK-TODO: combine withRootPreferenceFragmentOfActivityProvider() and withFragment2PreferenceFragmentConverterFactory()?
                .withRootPreferenceFragmentOfActivityProvider(
                        new RootPreferenceFragmentOfActivityProvider() {

                            private final Map<? extends Class<? extends AppCompatActivity>, ? extends Class<? extends PreferenceFragmentCompat>> rootPreferenceFragmentByActivity = getRootPreferenceFragmentByActivityMap(activitySearchDatabaseConfigs);

                            @Override
                            public Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentOfActivity(final Class<? extends Activity> activityClass) {
                                return Optional.ofNullable(rootPreferenceFragmentByActivity.get(activityClass));
                            }

                            private static Map<? extends Class<? extends AppCompatActivity>, ? extends Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentByActivityMap(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
                                return activitySearchDatabaseConfigs
                                        .stream()
                                        .collect(
                                                Collectors.toMap(
                                                        _activitySearchDatabaseConfig -> _activitySearchDatabaseConfig.activityWithRootPreferenceFragmentConnection().activityClass(),
                                                        _activitySearchDatabaseConfig -> _activitySearchDatabaseConfig.activityWithRootPreferenceFragmentConnection().rootPreferenceFragmentClassOfActivityClass()));
                            }
                        })
                .withFragment2PreferenceFragmentConverter(
                        new Fragment2PreferenceFragmentConverter() {

                            private final Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> preferenceFragmentByFragment = getPreferenceFragmentByFragmentMap(activitySearchDatabaseConfigs);

                            @Override
                            public Optional<Class<? extends PreferenceFragmentCompat>> asPreferenceFragment(final Class<? extends Fragment> fragment) {
                                return Maps.get(preferenceFragmentByFragment, fragment);
                            }

                            private static Map<Class<? extends Fragment>, Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentByFragmentMap(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
                                return activitySearchDatabaseConfigs
                                        .stream()
                                        .map(ActivitySearchDatabaseConfig::fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer)
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .map(FragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer::fragmentWithPreferenceFragmentConnection)
                                        .collect(
                                                Collectors.toMap(
                                                        FragmentWithPreferenceFragmentConnection::fragment,
                                                        FragmentWithPreferenceFragmentConnection::preferenceFragment));
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
