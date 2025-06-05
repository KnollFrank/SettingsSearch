package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverterFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceEntityDAO;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogsFactory;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.lib.settingssearch.search.PreferenceVisibleAndSearchablePredicate;

public class SearchablePreferenceScreenGraphProviderFactory {

    public static SearchablePreferenceScreenGraphProvider createSearchablePreferenceScreenGraphProvider(
            final Fragment fragment,
            final SearchablePreferenceEntityDAO searchablePreferenceDAO,
            final @IdRes int containerViewId,
            final SearchDatabaseConfig searchDatabaseConfig) {
        return createSearchablePreferenceScreenGraphProvider(
                containerViewId,
                (ViewGroup) fragment.requireView(),
                fragment.requireActivity(),
                fragment.getChildFragmentManager(),
                fragment.requireContext(),
                searchDatabaseConfig,
                searchablePreferenceDAO);
    }

    public static SearchablePreferenceScreenGraphProvider createSearchablePreferenceScreenGraphProvider(
            final @IdRes int containerViewId,
            final ViewGroup view,
            final FragmentActivity fragmentActivity,
            final FragmentManager childFragmentManager,
            final Context context,
            final SearchDatabaseConfig searchDatabaseConfig,
            final SearchablePreferenceEntityDAO searchablePreferenceDAO) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                view,
                containerViewId);
        return new SearchablePreferenceScreenGraphProvider(
                searchDatabaseConfig.preferenceScreenGraphAvailableListener,
                searchDatabaseConfig.computePreferencesListener,
                new Graph2POJOGraphTransformer(
                        new PreferenceScreen2SearchablePreferenceScreenConverter(
                                Preference2SearchablePreferenceConverterFactory.createPreference2SearchablePreferenceConverter(
                                        searchDatabaseConfig,
                                        PreferenceDialogsFactory.createPreferenceDialogs(fragmentActivity, containerViewId),
                                        IdGeneratorFactory.createIdGeneratorStartingAt(
                                                searchablePreferenceDAO
                                                        .getMaxId()
                                                        .map(maxId -> maxId + 1)
                                                        .orElse(0)))),
                        searchDatabaseConfig.preferenceFragmentIdProvider),
                PreferenceScreenGraphProviderFactory.createPreferenceScreenGraphProvider(
                        new PreferenceScreenWithHostProvider(
                                InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment(
                                        searchDatabaseConfig.fragmentFactory,
                                        FragmentInitializerFactory.createFragmentInitializer(
                                                childFragmentManager,
                                                containerViewId,
                                                OnUiThreadRunnerFactory.fromActivity(fragmentActivity)),
                                        context),
                                new SearchablePreferenceScreenProvider(
                                        new PreferenceVisibleAndSearchablePredicate(searchDatabaseConfig.preferenceSearchablePredicate)),
                                searchDatabaseConfig.principalAndProxyProvider),
                        searchDatabaseConfig.preferenceFragmentConnected2PreferenceProvider,
                        searchDatabaseConfig.rootPreferenceFragmentOfActivityProvider,
                        context,
                        preferenceScreenWithHost -> {
                        }));
    }
}
