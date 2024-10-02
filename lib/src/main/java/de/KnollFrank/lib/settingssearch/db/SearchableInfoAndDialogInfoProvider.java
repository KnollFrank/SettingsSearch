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
        return join(
                searchableInfoProvider.getSearchableInfo(preference),
                searchableInfoByPreferenceProvider.getSearchableDialogInfoOfPreference(preference, hostOfPreference),
                "\n");
    }

    private static Optional<String> join(final Optional<String> str1, final Optional<String> str2, final String delimiter) {
        return str1.isPresent() || str2.isPresent() ?
                Optional.of(join2Str(str1, str2, delimiter)) :
                Optional.empty();
    }

    private static String join2Str(final Optional<String> str1, final Optional<String> str2, final String delimiter) {
        return String.join(delimiter, Lists.getPresentElements(List.of(str1, str2)));
    }
}
