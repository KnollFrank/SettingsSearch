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
                            public Fragment instantiate(final String fragmentClassName,
                                                        final Optional<PreferenceWithHost> src,
                                                        final Context context) {
                                if (PreferenceFragmentWithSinglePreference.class.getName().equals(fragmentClassName) &&
                                        src.isPresent() &&
                                        PrefsFragmentFirst.KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS.equals(src.get().preference().getKey())) {
                                    return Fragment.instantiate(
                                            context,
                                            fragmentClassName,
                                            PrefsFragmentFirst.createArguments4PreferenceWithoutExtras(src.get().preference()));
                                }
                                return new DefaultFragmentFactory().instantiate(fragmentClassName, src, context);
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
                // FK-TODO: combine withRootPreferenceFragmentOfActivityProvider() and withFragment2PreferenceFragmentConverterFactory()?
                .withRootPreferenceFragmentOfActivityProvider(
                        new RootPreferenceFragmentOfActivityProvider() {

                            @Override
                            public Optional<Class<? extends Fragment>> getRootPreferenceFragmentOfActivity(final String classNameOfActivity) {
                                if (classNameOfActivity.equals(SettingsActivity.class.getName())) {
                                    return Optional.of(SettingsFragment.class);
                                }
                                if (classNameOfActivity.equals(SettingsActivity2.class.getName())) {
                                    return Optional.of(SettingsFragment2.class);
                                }
                                if (classNameOfActivity.equals(SettingsActivity3.class.getName())) {
                                    return Optional.of(ItemFragment3.class);
                                }
                                return Optional.empty();
                            }
                        })
                .withFragment2PreferenceFragmentConverterFactory(
                        new Fragment2PreferenceFragmentConverterFactory() {

                            @Override
                            public Fragment2PreferenceFragmentConverter createFragment2PreferenceFragmentConverter(final Fragments fragments) {
                                return new Fragment2PreferenceFragmentConverter() {

                                    @Override
                                    public Optional<PreferenceFragmentCompat> asPreferenceFragment(final Fragment fragment) {
                                        if (fragment instanceof final ItemFragment itemFragment) {
                                            return Optional.of(
                                                    (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                                                            itemFragment.asPreferenceFragment().getClass().getName(),
                                                            Optional.empty()));
                                        }
                                        if (fragment instanceof final ItemFragment3 itemFragment3) {
                                            return Optional.of(
                                                    (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                                                            itemFragment3.asPreferenceFragment().getClass().getName(),
                                                            Optional.empty()));
                                        }
                                        return Optional.empty();
                                    }

                                    @Override
                                    public Optional<Class<? extends PreferenceFragmentCompat>> asPreferenceFragment(final Class<? extends Fragment> fragment) {
                                        if (ItemFragment.class.isAssignableFrom(fragment)) {
                                            return Optional.of(ItemFragment.PreferenceFragment.class);
                                        }
                                        if (ItemFragment3.class.isAssignableFrom(fragment)) {
                                            return Optional.of(ItemFragment3.PreferenceFragment3.class);
                                        }
                                        return Optional.empty();
                                    }
                                };
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
