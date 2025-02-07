package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public record SearchDatabaseConfig(FragmentFactory fragmentFactory,
                                   IconResourceIdProvider iconResourceIdProvider,
                                   SearchableInfoProvider searchableInfoProvider,
                                   PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                   PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                                   Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
                                   RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                                   PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                   PreferenceSearchablePredicate preferenceSearchablePredicate,
                                   Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter) {
}
