package de.KnollFrank.lib.settingssearch.db;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchableInfoAndDialogInfoProvider {

    private final SearchableInfoProvider searchableInfoProvider;
    private final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider;

    public SearchableInfoAndDialogInfoProvider(final SearchableInfoProvider searchableInfoProvider,
                                               final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider) {
        this.searchableInfoProvider = searchableInfoProvider;
        this.searchableDialogInfoOfProvider = searchableDialogInfoOfProvider;
    }

    public Optional<String> getSearchableInfo(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
        return join(
                searchableInfoProvider.getSearchableInfo(preference),
                searchableDialogInfoOfProvider.getSearchableDialogInfoOfPreference(preference, hostOfPreference),
                "\n");
    }

    private static Optional<String> join(final Optional<String> str1, final Optional<String> str2, final String delimiter) {
        return str1.isPresent() || str2.isPresent() ?
                Optional.of(join2Str(str1, str2, delimiter)) :
                Optional.empty();
    }

    private static String join2Str(final Optional<String> str1, final Optional<String> str2, final String delimiter) {
        return Optionals
                .streamOfPresentElements(str1, str2)
                .collect(Collectors.joining(delimiter));
    }
}
