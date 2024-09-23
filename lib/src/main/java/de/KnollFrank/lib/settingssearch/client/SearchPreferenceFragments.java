package de.KnollFrank.lib.settingssearch.client;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;
import static de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProviderFactory.getBuiltinSearchableInfoProvider;

import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchPreferenceFragments {

    public final SearchConfiguration searchConfiguration;
    private final FragmentFactory fragmentFactory;
    private final SearchableInfoProvider searchableInfoProvider;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final ShowPreferencePath showPreferencePath;
    private final PrepareShow prepareShow;
    private final FragmentManager fragmentManager;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;

    public static SearchPreferenceFragmentsBuilder builder(final SearchConfiguration searchConfiguration,
                                                           final FragmentManager fragmentManager) {
        return new SearchPreferenceFragmentsBuilder(searchConfiguration, fragmentManager);
    }

    protected SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                        final FragmentFactory fragmentFactory,
                                        final SearchableInfoProvider searchableInfoProvider,
                                        final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                        final IsPreferenceSearchable isPreferenceSearchable,
                                        final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                        final ShowPreferencePath showPreferencePath,
                                        final PrepareShow prepareShow,
                                        final FragmentManager fragmentManager,
                                        final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentFactory = fragmentFactory;
        this.searchableInfoProvider = searchableInfoProvider.orElse(getBuiltinSearchableInfoProvider());
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.showPreferencePath = showPreferencePath;
        this.prepareShow = prepareShow;
        this.fragmentManager = fragmentManager;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                SearchPreferenceFragment.newInstance(
                        searchConfiguration,
                        isPreferenceSearchable,
                        searchableInfoProvider,
                        new SearchableInfoAttribute(),
                        showPreferencePath,
                        fragmentFactory,
                        preferenceDialogAndSearchableInfoProvider,
                        preferenceScreenGraphAvailableListener,
                        prepareShow,
                        preferenceConnected2PreferenceFragmentProvider),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId(),
                fragmentManager);
    }
}
