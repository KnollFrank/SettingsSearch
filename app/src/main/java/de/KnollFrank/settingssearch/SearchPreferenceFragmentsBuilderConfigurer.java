package de.KnollFrank.settingssearch;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragmentsBuilder;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragmentUI;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.settingssearch.preference.fragment.CustomDialogFragment;
import de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentSecond;

public class SearchPreferenceFragmentsBuilderConfigurer {

    public static SearchPreferenceFragmentsBuilder configure(
            final SearchPreferenceFragmentsBuilder builder,
            final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier) {
        return builder
                .withSearchPreferenceFragmentUI(
                        new SearchPreferenceFragmentUI() {

                            @Override
                            public @LayoutRes int getRootViewId() {
                                return R.layout.custom_searchpreference_fragment;
                            }

                            @Override
                            public SearchView getSearchView(final View rootView) {
                                return rootView.requireViewById(R.id.searchViewCustom);
                            }

                            @Override
                            public FragmentContainerView getSearchResultsFragmentContainerView(final View rootView) {
                                return rootView.requireViewById(R.id.searchResultsFragmentContainerViewCustom);
                            }

                            @Override
                            public View getProgressContainer(final View rootView) {
                                return rootView.requireViewById(R.id.progressContainerCustom);
                            }

                            @Override
                            public TextView getProgressText(final View progressContainer) {
                                return progressContainer.requireViewById(de.KnollFrank.lib.settingssearch.R.id.progressText);
                            }
                        })
                .withCreateSearchDatabaseTaskSupplier(createSearchDatabaseTaskSupplier)
                .withSearchableInfoProvider(new ReversedListPreferenceSearchableInfoProvider())
                .withPreferenceConnected2PreferenceFragmentProvider(
                        new PreferenceConnected2PreferenceFragmentProvider() {

                            @Override
                            public Optional<String> getClassNameOfConnectedPreferenceFragment(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
                                return PrefsFragmentFirst.NON_STANDARD_LINK_TO_SECOND_FRAGMENT.equals(preference.getKey()) ?
                                        Optional.of(PrefsFragmentSecond.class.getName()) :
                                        Optional.empty();
                            }
                        })
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
                .withPreferenceDialogAndSearchableInfoProvider(
                        new PreferenceDialogAndSearchableInfoProvider() {

                            @Override
                            public Optional<PreferenceDialogAndSearchableInfoByPreferenceDialogProvider> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
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
                        });
    }
}
