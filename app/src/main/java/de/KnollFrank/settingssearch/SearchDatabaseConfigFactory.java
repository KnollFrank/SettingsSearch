package de.KnollFrank.settingssearch;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverterFactory;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.client.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.client.SearchDatabaseConfigBuilder;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentHelper;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
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

    public static SearchDatabaseConfig createSearchDatabaseConfig() {
        return new SearchDatabaseConfigBuilder()
                .withFragmentFactory(
                        new FragmentFactory() {

                            @Override
                            public <T extends Fragment> T instantiate(final Class<T> fragmentClass, final Optional<PreferenceWithHost> src, final Context context, final Fragments fragments) {
                                if (PreferenceFragmentWithSinglePreference.class.equals(fragmentClass) &&
                                        src.isPresent() &&
                                        PrefsFragmentFirst.KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS.equals(src.get().preference().getKey())) {
                                    return FragmentHelper.instantiateFragmentClass(fragmentClass, Optional.of(PrefsFragmentFirst.createArguments4PreferenceWithoutExtras(src.get().preference())));
                                }
                                if (ItemFragment.PreferenceFragment.class.equals(fragmentClass)) {
                                    final ItemFragment.PreferenceFragment preferenceFragment = (ItemFragment.PreferenceFragment) new DefaultFragmentFactory().instantiate(fragmentClass, src, context, fragments);
                                    preferenceFragment.beforeOnCreate(fragments);
                                    return (T) preferenceFragment;
                                }
                                if (ItemFragment3.PreferenceFragment3.class.equals(fragmentClass)) {
                                    final ItemFragment3.PreferenceFragment3 preferenceFragment3 = (ItemFragment3.PreferenceFragment3) new DefaultFragmentFactory().instantiate(fragmentClass, src, context, fragments);
                                    preferenceFragment3.beforeOnCreate(fragments);
                                    return (T) preferenceFragment3;
                                }
                                return new DefaultFragmentFactory().instantiate(fragmentClass, src, context, fragments);
                            }
                        })
                // FK-TODO: combine withRootPreferenceFragmentOfActivityProvider() and withFragment2PreferenceFragmentConverterFactory()?
                .withRootPreferenceFragmentOfActivityProvider(
                        new RootPreferenceFragmentOfActivityProvider() {

                            @Override
                            public Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentOfActivity(final String classNameOfActivity) {
                                if (SettingsActivity.class.getName().equals(classNameOfActivity)) {
                                    return Optional.of(SettingsFragment.class);
                                }
                                if (SettingsActivity2.class.getName().equals(classNameOfActivity)) {
                                    return Optional.of(SettingsFragment2.class);
                                }
                                if (SettingsActivity3.class.getName().equals(classNameOfActivity)) {
                                    return Optional.of(ItemFragment3.PreferenceFragment3.class);
                                }
                                return Optional.empty();
                            }
                        })
                // FK-TODO: remove?
                .withFragment2PreferenceFragmentConverterFactory(
                        new Fragment2PreferenceFragmentConverterFactory() {

                            @Override
                            public Fragment2PreferenceFragmentConverter createFragment2PreferenceFragmentConverter(final Fragments fragments) {
                                return new Fragment2PreferenceFragmentConverter() {

                                    @Override
                                    public Optional<? extends PreferenceFragmentCompat> asPreferenceFragment(final Fragment fragment) {
                                        if (fragment instanceof final ItemFragment itemFragment) {
                                            return Optional.of(
                                                    fragments.instantiateAndInitializeFragment(
                                                            itemFragment.asPreferenceFragment().getClass(),
                                                            Optional.empty()));
                                        }
                                        if (fragment instanceof final ItemFragment3 itemFragment3) {
                                            return Optional.of(
                                                    (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                                                            itemFragment3.asPreferenceFragment().getClass(),
                                                            Optional.empty()));
                                        }
                                        return Optional.empty();
                                    }
                                };
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
