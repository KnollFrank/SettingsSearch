package de.KnollFrank.lib.settingssearch.client;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;
import static de.KnollFrank.lib.settingssearch.search.provider.BuiltinPreferenceDescriptionsFactory.createBuiltinPreferenceDescriptions;
import static de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescriptions.getSearchableInfoProviders;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
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
    private final FragmentManager fragmentManager;
    private final Consumer<PreferenceFragmentCompat> prepareShow;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final FragmentFactory fragmentFactory,
                                     final List<PreferenceDescription> preferenceDescriptions,
                                     final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                     final IsPreferenceSearchable isPreferenceSearchable,
                                     final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                     final ShowPreferencePath showPreferencePath,
                                     final FragmentManager fragmentManager,
                                     final Consumer<PreferenceFragmentCompat> prepareShow) {
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
        this.fragmentManager = fragmentManager;
        this.prepareShow = prepareShow;
    }

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final FragmentFactory fragmentFactory,
                                     final List<PreferenceDescription> preferenceDescriptions,
                                     final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                     final IsPreferenceSearchable isPreferenceSearchable,
                                     final ShowPreferencePath showPreferencePath,
                                     final FragmentManager fragmentManager,
                                     final Consumer<PreferenceFragmentCompat> prepareShow) {
        this(
                searchConfiguration,
                fragmentFactory,
                preferenceDescriptions,
                preferenceDialogAndSearchableInfoProvider,
                isPreferenceSearchable,
                preferenceScreenGraph -> {
                },
                showPreferencePath,
                fragmentManager,
                prepareShow);
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
