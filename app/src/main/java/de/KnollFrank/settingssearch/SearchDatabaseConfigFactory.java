package de.KnollFrank.settingssearch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableMap;

import org.jgrapht.Graph;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivitySearchDatabaseConfigs;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PrincipalAndProxy;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.graph.ComputePreferencesListener;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializer;
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
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFifth;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentSecond;

public class SearchDatabaseConfigFactory {

    public static SearchDatabaseConfig createSearchDatabaseConfig() {
        return SearchDatabaseConfig
                .builder(PrefsFragmentFirst.class)
                .withFragmentFactory(
                        new FragmentFactory() {

                            @Override
                            public <T extends Fragment> T instantiate(final Class<T> fragmentClass,
                                                                      final Optional<PreferenceWithHost> src,
                                                                      final Context context,
                                                                      final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
                                if (PreferenceFragmentWithSinglePreference.class.equals(fragmentClass) &&
                                        src.isPresent() &&
                                        PrefsFragmentFifth.KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS.equals(src.orElseThrow().preference().getKey())) {
                                    return Classes.instantiateFragmentClass(
                                            fragmentClass,
                                            Optional.of(PrefsFragmentFifth.createArguments4PreferenceWithoutExtras(src.orElseThrow().preference(), context)));
                                } else if (ItemFragment3.class.equals(fragmentClass) && src.isPresent() && "preferenceWithIntent3".equals(src.orElseThrow().preference().getKey())) {
                                    final Preference preference = src.orElseThrow().preference();
                                    PrefsFragmentFirst
                                            .markExtrasOfPreferenceConnectingSrcWithDst(
                                                    preference,
                                                    src.orElseThrow().host(),
                                                    fragmentClass);
                                    return Classes.instantiateFragmentClass(
                                            fragmentClass,
                                            Optional.of(preference.getExtras()));
                                }
                                return new DefaultFragmentFactory().instantiate(fragmentClass, src, context, instantiateAndInitializeFragment);
                            }
                        })
                .withActivitySearchDatabaseConfigs(
                        new ActivitySearchDatabaseConfigs(
                                ImmutableMap
                                        .<Class<? extends Activity>, Class<? extends PreferenceFragmentCompat>>builder()
                                        .put(SettingsActivity.class, SettingsFragment.class)
                                        .put(SettingsActivity2.class, SettingsFragment2.class)
                                        .put(SettingsActivity3.class, ItemFragment3.PreferenceFragment3.class)
                                        .build(),
                                Set.of(
                                        new PrincipalAndProxy<>(ItemFragment.class, ItemFragment.PreferenceFragment.class),
                                        new PrincipalAndProxy<>(ItemFragment3.class, ItemFragment3.PreferenceFragment3.class))))
                .withActivityInitializerByActivity(
                        ImmutableMap
                                .<Class<? extends Activity>, ActivityInitializer<?>>builder()
                                .put(
                                        SettingsActivity.class,
                                        new ActivityInitializer<PrefsFragmentFirst>() {

                                            @Override
                                            public void beforeStartActivity(final PrefsFragmentFirst src) {
                                                Log.i(this.getClass().getSimpleName(), "beforeStartActivity called");
                                            }

                                            @Override
                                            public Optional<Bundle> createExtras(final PrefsFragmentFirst src) {
                                                return Optional.of(PrefsFragmentFirst.createExtrasForSettingsActivity());
                                            }
                                        })
                                .build())
                .withSearchableInfoProvider(new ReversedListPreferenceSearchableInfoProvider())
                .withPreferenceFragmentConnected2PreferenceProvider(
                        new PreferenceFragmentConnected2PreferenceProvider() {

                            @Override
                            public Optional<Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentConnected2Preference(
                                    final Preference preference,
                                    final PreferenceFragmentCompat hostOfPreference) {
                                return PrefsFragmentFirst.NON_STANDARD_LINK_TO_SECOND_FRAGMENT.equals(preference.getKey()) ?
                                        Optional.of(PrefsFragmentSecond.class) :
                                        Optional.empty();
                            }
                        })
                .withPreferenceDialogAndSearchableInfoProvider(
                        new PreferenceDialogAndSearchableInfoProvider() {

                            @Override
                            public Optional<PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<?>> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(
                                    final Preference preference,
                                    final PreferenceFragmentCompat hostOfPreference) {
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
                            public void onPreferenceScreenGraphAvailable(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
                                Log.i(this.getClass().getSimpleName(), PreferenceScreenGraph2DOTConverter.graph2DOT(preferenceScreenGraph));
                            }
                        })
                .withComputePreferencesListener(
                        new ComputePreferencesListener() {

                            @Override
                            public void onStartComputePreferences() {
                                Log.i(this.getClass().getSimpleName(), "onStartComputePreferences");
                            }

                            @Override
                            public void onFinishComputePreferences() {
                                Log.i(this.getClass().getSimpleName(), "onFinishComputePreferences");
                            }
                        })
                .build();
    }
}
