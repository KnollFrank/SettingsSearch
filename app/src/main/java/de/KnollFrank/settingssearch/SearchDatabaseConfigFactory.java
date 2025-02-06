package de.KnollFrank.settingssearch;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivitySearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityWithRootPreferenceFragment;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentWithPreferenceFragmentConnection;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentFactory;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfigBuilder;
import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
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

    public static SearchDatabaseConfig createSearchDatabaseConfig() {
        return new SearchDatabaseConfigBuilder()
                .withFragmentFactory(
                        new FragmentFactory() {

                            @Override
                            public <T extends Fragment> T instantiate(final Class<T> fragmentClass, final Optional<PreferenceWithHost> src, final Context context, final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
                                if (PreferenceFragmentWithSinglePreference.class.equals(fragmentClass) &&
                                        src.isPresent() &&
                                        PrefsFragmentFirst.KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS.equals(src.get().preference().getKey())) {
                                    return Classes.instantiateFragmentClass(fragmentClass, Optional.of(PrefsFragmentFirst.createArguments4PreferenceWithoutExtras(src.get().preference())));
                                }
                                return new DefaultFragmentFactory().instantiate(fragmentClass, src, context, instantiateAndInitializeFragment);
                            }
                        })
                .withActivitySearchDatabaseConfigs(getActivitySearchDatabaseConfigs())
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

    private static List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> getActivitySearchDatabaseConfigs() {
        return List.of(
                new ActivitySearchDatabaseConfig<>(
                        new ActivityWithRootPreferenceFragment<>(SettingsActivity.class, SettingsFragment.class),
                        Optional.of(
                                new PreferenceFragmentFactory<>(
                                        new FragmentWithPreferenceFragmentConnection<>(
                                                ItemFragment.class,
                                                ItemFragment.PreferenceFragment.class)) {

                                    @Override
                                    protected void initializePreferenceFragmentWithFragment(final ItemFragment.PreferenceFragment preferenceFragment, final ItemFragment fragment) {
                                        preferenceFragment.beforeOnCreate(fragment);
                                    }
                                })),
                new ActivitySearchDatabaseConfig<>(
                        new ActivityWithRootPreferenceFragment<>(SettingsActivity2.class, SettingsFragment2.class),
                        Optional.empty()),
                new ActivitySearchDatabaseConfig<>(
                        new ActivityWithRootPreferenceFragment<>(SettingsActivity3.class, ItemFragment3.PreferenceFragment3.class),
                        Optional.of(
                                new PreferenceFragmentFactory<>(
                                        new FragmentWithPreferenceFragmentConnection<>(
                                                ItemFragment3.class,
                                                ItemFragment3.PreferenceFragment3.class)) {

                                    @Override
                                    protected void initializePreferenceFragmentWithFragment(final ItemFragment3.PreferenceFragment3 preferenceFragment, final ItemFragment3 fragment) {
                                        preferenceFragment.beforeOnCreate(fragment);
                                    }
                                })));
    }
}
