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
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescription;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final ShowPreferencePath showPreferencePath;
    private final FragmentFactory fragmentFactory;
    private final FragmentManager fragmentManager;
    private final List<PreferenceDescription> preferenceDescriptions;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final IsPreferenceSearchable isPreferenceSearchable,
                                     final ShowPreferencePath showPreferencePath,
                                     final List<PreferenceDescription> preferenceDescriptions,
                                     final FragmentFactory fragmentFactory,
                                     final FragmentManager fragmentManager,
                                     final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        this.searchConfiguration = searchConfiguration;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.showPreferencePath = showPreferencePath;
        this.preferenceDescriptions =
                ImmutableList
                        .<PreferenceDescription>builder()
                        .addAll(createBuiltinPreferenceDescriptions())
                        .addAll(preferenceDescriptions)
                        .build();
        this.fragmentFactory = fragmentFactory;
        this.fragmentManager = fragmentManager;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
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
                        preferenceDialogAndSearchableInfoProvider),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId,
                fragmentManager);
    }
}
