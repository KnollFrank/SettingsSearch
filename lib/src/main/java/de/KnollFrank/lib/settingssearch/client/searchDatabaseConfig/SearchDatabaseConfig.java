package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseConfig;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.graph.ComputePreferencesListener;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializer;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchDatabaseConfig {

    public final FragmentFactory fragmentFactory;
    public final IconResourceIdProvider iconResourceIdProvider;
    public final SearchableInfoProvider searchableInfoProvider;
    public final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    public final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider;
    public final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    public final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider;
    public final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    public final ComputePreferencesListener computePreferencesListener;
    public final PreferenceSearchablePredicate preferenceSearchablePredicate;
    public final PrincipalAndProxyProvider principalAndProxyProvider;
    public final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity;
    public final PreferenceFragmentIdProvider preferenceFragmentIdProvider;
    // FK-TODO: remove appDatabaseConfig
    public final AppDatabaseConfig appDatabaseConfig;

    SearchDatabaseConfig(final FragmentFactory fragmentFactory,
                         final IconResourceIdProvider iconResourceIdProvider,
                         final SearchableInfoProvider searchableInfoProvider,
                         final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                         final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                         final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
                         final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                         final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                         final ComputePreferencesListener computePreferencesListener,
                         final PreferenceSearchablePredicate preferenceSearchablePredicate,
                         final PrincipalAndProxyProvider principalAndProxyProvider,
                         final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity,
                         final PreferenceFragmentIdProvider preferenceFragmentIdProvider,
                         final AppDatabaseConfig appDatabaseConfig) {
        this.fragmentFactory = fragmentFactory;
        this.iconResourceIdProvider = iconResourceIdProvider;
        this.searchableInfoProvider = searchableInfoProvider;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.preferenceFragmentConnected2PreferenceProvider = preferenceFragmentConnected2PreferenceProvider;
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.rootPreferenceFragmentOfActivityProvider = rootPreferenceFragmentOfActivityProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.computePreferencesListener = computePreferencesListener;
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        this.principalAndProxyProvider = principalAndProxyProvider;
        this.activityInitializerByActivity = activityInitializerByActivity;
        this.preferenceFragmentIdProvider = preferenceFragmentIdProvider;
        this.appDatabaseConfig = appDatabaseConfig;
    }

    public static SearchDatabaseConfigBuilder builder(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
                                                      final AppDatabaseConfig appDatabaseConfig) {
        return new SearchDatabaseConfigBuilder(rootPreferenceFragment, appDatabaseConfig);
    }
}
