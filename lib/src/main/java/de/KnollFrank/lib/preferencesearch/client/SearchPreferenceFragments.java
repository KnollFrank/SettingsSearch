package de.KnollFrank.lib.preferencesearch.client;

import static de.KnollFrank.lib.preferencesearch.fragment.Fragments.showFragment;
import static de.KnollFrank.lib.preferencesearch.search.provider.BuiltinPreferenceDescriptionsFactory.createBuiltinPreferenceDescriptions;
import static de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescriptions.getSearchableInfoProviders;

import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactory;
import de.KnollFrank.lib.preferencesearch.provider.SearchablePreferencePredicate;
import de.KnollFrank.lib.preferencesearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescription;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoAttribute;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;
    private final SearchablePreferencePredicate searchablePreferencePredicate;
    private final FragmentFactory fragmentFactory;
    private final FragmentManager fragmentManager;
    private final List<PreferenceDescription> preferenceDescriptions;
    private final Map<Class<? extends Preference>, String> tagOfDialogFragmentByPreference;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final SearchablePreferencePredicate searchablePreferencePredicate,
                                     final List<PreferenceDescription> preferenceDescriptions,
                                     final FragmentFactory fragmentFactory,
                                     final FragmentManager fragmentManager,
                                     final Map<Class<? extends Preference>, String> tagOfDialogFragmentByPreference) {
        this.searchConfiguration = searchConfiguration;
        this.searchablePreferencePredicate = searchablePreferencePredicate;
        this.preferenceDescriptions =
                ImmutableList
                        .<PreferenceDescription>builder()
                        .addAll(createBuiltinPreferenceDescriptions())
                        .addAll(preferenceDescriptions)
                        .build();
        this.fragmentFactory = fragmentFactory;
        this.fragmentManager = fragmentManager;
        this.tagOfDialogFragmentByPreference = tagOfDialogFragmentByPreference;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                SearchPreferenceFragment.newInstance(
                        searchConfiguration,
                        searchablePreferencePredicate,
                        getSearchableInfoProviders(preferenceDescriptions),
                        new SearchableInfoAttribute(),
                        fragmentFactory,
                        tagOfDialogFragmentByPreference),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId,
                fragmentManager);
    }
}
