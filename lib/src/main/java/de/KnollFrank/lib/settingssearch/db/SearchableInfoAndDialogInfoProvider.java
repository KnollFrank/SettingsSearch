package de.KnollFrank.lib.settingssearch.db;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchableInfoAndDialogInfoProvider {

    private final SearchableInfoProvider searchableInfoProvider;
    private final ISearchableDialogInfoOfProvider searchableInfoByPreferenceProvider;

    public SearchableInfoAndDialogInfoProvider(final SearchableInfoProvider searchableInfoProvider,
                                               final ISearchableDialogInfoOfProvider searchableInfoByPreferenceProvider) {
        this.searchableInfoProvider = searchableInfoProvider;
        this.searchableInfoByPreferenceProvider = searchableInfoByPreferenceProvider;
    }

    public Optional<String> getSearchableInfo(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
        final Optional<String> searchableInfo = searchableInfoProvider.getSearchableInfo(preference);
        final Optional<String> searchableInfoOfDialogOfPreference = searchableInfoByPreferenceProvider.getSearchableDialogInfoOfPreference(preference, hostOfPreference);
        return searchableInfo.isPresent() || searchableInfoOfDialogOfPreference.isPresent() ?
                Optional.of(join(searchableInfo, searchableInfoOfDialogOfPreference, "\n")) :
                Optional.empty();
    }

    private static String join(final Optional<String> str1, final Optional<String> str2, final String delimiter) {
        return String.join(delimiter, Lists.getPresentElements(List.of(str1, str2)));
    }
}
