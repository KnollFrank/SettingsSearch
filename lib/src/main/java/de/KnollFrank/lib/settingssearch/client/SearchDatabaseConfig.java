package de.KnollFrank.lib.settingssearch.client;

import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public record SearchDatabaseConfig(FragmentFactory fragmentFactory,
                                   IconResourceIdProvider iconResourceIdProvider,
                                   SearchableInfoProvider searchableInfoProvider,
                                   PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                   PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                                   PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                   PreferenceSearchablePredicate preferenceSearchablePredicate) {
}
