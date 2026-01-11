package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverterFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogsFactory;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;

public class SearchablePreferenceScreenGraphProviderFactory {

    public static SearchablePreferenceScreenTreeProvider createSearchablePreferenceScreenGraphProvider(
            final Fragment fragment,
            final @IdRes int containerViewId,
            final SearchDatabaseConfig searchDatabaseConfig,
            final Locale locale,
            final AddEdgeToGraphPredicate addEdgeToGraphPredicate) {
        return createSearchablePreferenceScreenGraphProvider(
                containerViewId,
                (ViewGroup) fragment.requireView(),
                fragment.requireActivity(),
                fragment.getChildFragmentManager(),
                fragment.requireContext(),
                searchDatabaseConfig,
                locale,
                addEdgeToGraphPredicate);
    }

    public static SearchablePreferenceScreenTreeProvider createSearchablePreferenceScreenGraphProvider(
            final @IdRes int containerViewId,
            final ViewGroup view,
            final FragmentActivity fragmentActivity,
            final FragmentManager childFragmentManager,
            final Context context,
            final SearchDatabaseConfig searchDatabaseConfig,
            final Locale locale,
            final AddEdgeToGraphPredicate addEdgeToGraphPredicate) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                view,
                containerViewId);
        return new SearchablePreferenceScreenTreeProvider(
                searchDatabaseConfig.preferenceScreenTreeAvailableListener,
                searchDatabaseConfig.computePreferencesListener,
                new TreeToPojoTreeTransformer(
                        new PreferenceScreenToSearchablePreferenceScreenConverter(
                                Preference2SearchablePreferenceConverterFactory.createPreference2SearchablePreferenceConverter(
                                        searchDatabaseConfig,
                                        PreferenceDialogsFactory.createPreferenceDialogs(fragmentActivity, containerViewId, searchDatabaseConfig.preferenceSearchablePredicate))),
                        searchDatabaseConfig.preferenceFragmentIdProvider),
                PreferenceScreenGraphProviderFactory.createPreferenceScreenGraphProvider(
                        new PreferenceScreenWithHostProvider(
                                InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment(
                                        searchDatabaseConfig.fragmentFactory,
                                        FragmentInitializerFactory.createFragmentInitializer(
                                                childFragmentManager,
                                                containerViewId,
                                                OnUiThreadRunnerFactory.fromActivity(fragmentActivity),
                                                searchDatabaseConfig.preferenceSearchablePredicate),
                                        context),
                                searchDatabaseConfig.principalAndProxyProvider),
                        searchDatabaseConfig.preferenceFragmentConnected2PreferenceProvider,
                        searchDatabaseConfig.rootPreferenceFragmentOfActivityProvider,
                        addEdgeToGraphPredicate,
                        context,
                        preferenceScreenWithHost -> {
                        }),
                locale);
    }
}
