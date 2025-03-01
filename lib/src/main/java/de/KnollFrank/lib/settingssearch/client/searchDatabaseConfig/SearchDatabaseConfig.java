package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.ConnectedFragmentProvider;
import de.KnollFrank.lib.settingssearch.ConnectedPreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
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
    public final PreferenceSearchablePredicate preferenceSearchablePredicate;
    public final ConnectedPreferenceFragmentProvider connectedPreferenceFragmentProvider;
    public final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity;
    public final ConnectedFragmentProvider connectedFragmentProvider;

    SearchDatabaseConfig(final FragmentFactory fragmentFactory,
                         final IconResourceIdProvider iconResourceIdProvider,
                         final SearchableInfoProvider searchableInfoProvider,
                         final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                         final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                         final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
                         final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                         final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                         final PreferenceSearchablePredicate preferenceSearchablePredicate,
                         final ConnectedPreferenceFragmentProvider connectedPreferenceFragmentProvider,
                         final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity,
                         final ConnectedFragmentProvider connectedFragmentProvider) {
        this.fragmentFactory = fragmentFactory;
        this.iconResourceIdProvider = iconResourceIdProvider;
        this.searchableInfoProvider = searchableInfoProvider;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.preferenceFragmentConnected2PreferenceProvider = preferenceFragmentConnected2PreferenceProvider;
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.rootPreferenceFragmentOfActivityProvider = rootPreferenceFragmentOfActivityProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        this.connectedPreferenceFragmentProvider = connectedPreferenceFragmentProvider;
        this.activityInitializerByActivity = activityInitializerByActivity;
        this.connectedFragmentProvider = connectedFragmentProvider;
    }

    public static SearchDatabaseConfigBuilder builder(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        return new SearchDatabaseConfigBuilder(rootPreferenceFragment);
    }
}
