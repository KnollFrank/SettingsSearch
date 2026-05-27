package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.graph.TreeBuilderListener;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializer;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnectedToPreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchDatabaseConfig<C> {

    public final FragmentFactory fragmentFactory;
    public final IconResourceIdProvider iconResourceIdProvider;
    public final SearchableInfoProvider searchableInfoProvider;
    public final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    public final PreferenceFragmentConnectedToPreferenceProvider preferenceFragmentConnectedToPreferenceProvider;
    public final FragmentClassOfActivity<? extends Fragment> rootFragment;
    public final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider;
    public final TreeBuilderListener<PreferenceScreenOfHostOfActivity, Preference> preferenceScreenTreeBuilderListener;
    public final PreferenceSearchablePredicate preferenceSearchablePredicate;
    public final PrincipalAndProxyProvider principalAndProxyProvider;
    public final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity;
    public final FragmentIdProvider fragmentIdProvider;
    public final TreeProcessorFactory<C> treeProcessorFactory;
    public final FragmentToPreferencesConverter fragmentToPreferencesConverter;

    SearchDatabaseConfig(final FragmentFactory fragmentFactory,
                         final IconResourceIdProvider iconResourceIdProvider,
                         final SearchableInfoProvider searchableInfoProvider,
                         final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                         final PreferenceFragmentConnectedToPreferenceProvider preferenceFragmentConnectedToPreferenceProvider,
                         final FragmentClassOfActivity<? extends Fragment> rootFragment,
                         final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                         final TreeBuilderListener<PreferenceScreenOfHostOfActivity, Preference> preferenceScreenTreeBuilderListener,
                         final PreferenceSearchablePredicate preferenceSearchablePredicate,
                         final PrincipalAndProxyProvider principalAndProxyProvider,
                         final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity,
                         final FragmentIdProvider fragmentIdProvider,
                         final TreeProcessorFactory<C> treeProcessorFactory,
                         final FragmentToPreferencesConverter fragmentToPreferencesConverter) {
        this.fragmentFactory = fragmentFactory;
        this.iconResourceIdProvider = iconResourceIdProvider;
        this.searchableInfoProvider = searchableInfoProvider;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.preferenceFragmentConnectedToPreferenceProvider = preferenceFragmentConnectedToPreferenceProvider;
        this.rootFragment = rootFragment;
        this.rootPreferenceFragmentOfActivityProvider = rootPreferenceFragmentOfActivityProvider;
        this.preferenceScreenTreeBuilderListener = preferenceScreenTreeBuilderListener;
        this.preferenceSearchablePredicate = new PreferenceVisibleAndSearchablePredicate(preferenceSearchablePredicate);
        this.principalAndProxyProvider = principalAndProxyProvider;
        this.activityInitializerByActivity = activityInitializerByActivity;
        this.fragmentIdProvider = fragmentIdProvider;
        this.treeProcessorFactory = treeProcessorFactory;
        this.fragmentToPreferencesConverter = fragmentToPreferencesConverter;
    }

    public static <C> SearchDatabaseConfigBuilder<C> builder(final FragmentClassOfActivity<? extends Fragment> rootFragment,
                                                             final TreeProcessorFactory<C> treeProcessorFactory) {
        return new SearchDatabaseConfigBuilder<>(rootFragment, treeProcessorFactory);
    }
}
