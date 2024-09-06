package de.KnollFrank.lib.settingssearch.client;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;
import static de.KnollFrank.lib.settingssearch.search.provider.BuiltinPreferenceDescriptionsFactory.createBuiltinPreferenceDescriptions;
import static de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescriptions.getSearchableInfoProviders;

import androidx.fragment.app.FragmentManager;

import com.google.common.collect.ImmutableList;

import java.util.List;

import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescription;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;
    private final FragmentFactory fragmentFactory;
    private final List<PreferenceDescription> preferenceDescriptions;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final ShowPreferencePath showPreferencePath;
    private final PrepareShow prepareShow;
    private final FragmentManager fragmentManager;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final FragmentFactory fragmentFactory,
                                     final List<PreferenceDescription> preferenceDescriptions,
                                     final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                     final IsPreferenceSearchable isPreferenceSearchable,
                                     final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                     final ShowPreferencePath showPreferencePath,
                                     final PrepareShow prepareShow,
                                     final FragmentManager fragmentManager) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentFactory = fragmentFactory;
        this.preferenceDescriptions =
                ImmutableList
                        .<PreferenceDescription>builder()
                        .addAll(createBuiltinPreferenceDescriptions())
                        .addAll(preferenceDescriptions)
                        .build();
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.showPreferencePath = showPreferencePath;
        this.prepareShow = prepareShow;
        this.fragmentManager = fragmentManager;
    }

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final FragmentFactory fragmentFactory,
                                     final List<PreferenceDescription> preferenceDescriptions,
                                     final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                     final IsPreferenceSearchable isPreferenceSearchable,
                                     final ShowPreferencePath showPreferencePath,
                                     final PrepareShow prepareShow,
                                     final FragmentManager fragmentManager) {
        this(
                searchConfiguration,
                fragmentFactory,
                preferenceDescriptions,
                preferenceDialogAndSearchableInfoProvider,
                isPreferenceSearchable,
                preferenceScreenGraph -> {
                },
                showPreferencePath,
                prepareShow,
                fragmentManager);
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                SearchPreferenceFragment.newInstance(
                        searchConfiguration,
                        isPreferenceSearchable,
                        getSearchableInfoProviders(preferenceDescriptions),
                        new SearchableInfoAttribute(),
                        showPreferencePath,
                        fragmentFactory,
                        preferenceDialogAndSearchableInfoProvider,
                        preferenceScreenGraphAvailableListener,
                        prepareShow),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId,
                fragmentManager);
    }
}
