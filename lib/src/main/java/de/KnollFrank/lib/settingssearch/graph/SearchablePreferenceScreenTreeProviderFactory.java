package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceToSearchablePreferenceConverterFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogsFactory;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;

public class SearchablePreferenceScreenTreeProviderFactory {

    private SearchablePreferenceScreenTreeProviderFactory() {
    }

    public static SearchablePreferenceScreenTreeProvider createSearchablePreferenceScreenTreeProvider(
            final Fragment fragment,
            final @IdRes int containerViewId,
            final SearchDatabaseConfig<?> searchDatabaseConfig,
            final Locale locale,
            final AddEdgeToTreePredicate<PreferenceScreenOfHostOfActivity, Preference> addEdgeToTreePredicate) {
        return createSearchablePreferenceScreenTreeProvider(
                containerViewId,
                (ViewGroup) fragment.requireView(),
                fragment.requireActivity(),
                fragment.getChildFragmentManager(),
                fragment.requireContext(),
                searchDatabaseConfig,
                locale,
                addEdgeToTreePredicate);
    }

    public static SearchablePreferenceScreenTreeProvider createSearchablePreferenceScreenTreeProvider(
            final @IdRes int containerViewId,
            final ViewGroup view,
            final FragmentActivity fragmentActivity,
            final FragmentManager childFragmentManager,
            final Context context,
            final SearchDatabaseConfig<?> searchDatabaseConfig,
            final Locale locale,
            final AddEdgeToTreePredicate<PreferenceScreenOfHostOfActivity, Preference> addEdgeToTreePredicate) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                view,
                containerViewId);
        return new SearchablePreferenceScreenTreeProvider(
                new TreeToPojoTreeTransformer(
                        new PreferenceScreenToSearchablePreferenceScreenConverter(
                                PreferenceToSearchablePreferenceConverterFactory.createPreferenceToSearchablePreferenceConverter(
                                        searchDatabaseConfig,
                                        PreferenceDialogsFactory.createPreferenceDialogs(fragmentActivity, containerViewId, searchDatabaseConfig.preferenceSearchablePredicate))),
                        searchDatabaseConfig.preferenceFragmentIdProvider),
                PreferenceScreenTreeBuilderFactory.createPreferenceScreenTreeBuilder(
                        new PreferenceScreenProvider(
                                InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment(
                                        searchDatabaseConfig.fragmentFactory,
                                        FragmentInitializerFactory.createFragmentInitializer(
                                                childFragmentManager,
                                                containerViewId,
                                                OnUiThreadRunnerFactory.fromActivity(fragmentActivity),
                                                searchDatabaseConfig.preferenceSearchablePredicate),
                                        context),
                                searchDatabaseConfig.principalAndProxyProvider),
                        searchDatabaseConfig.preferenceFragmentConnectedToPreferenceProvider,
                        searchDatabaseConfig.rootPreferenceFragmentOfActivityProvider,
                        addEdgeToTreePredicate,
                        searchDatabaseConfig.preferenceScreenTreeBuilderListener,
                        context),
                locale);
    }
}
