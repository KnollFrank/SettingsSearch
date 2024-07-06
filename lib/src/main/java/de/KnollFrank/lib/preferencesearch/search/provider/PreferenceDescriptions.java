package de.KnollFrank.lib.preferencesearch.search.provider;

import java.util.List;
import java.util.stream.Collectors;

public class PreferenceDescriptions {

    public static SearchableInfoProviders getSearchableInfoProviders(final List<PreferenceDescription> preferenceDescriptions) {
        return new SearchableInfoProviders(
                preferenceDescriptions
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        preferenceDescription -> preferenceDescription.preferenceClass,
                                        preferenceDescription -> preferenceDescription.searchableInfoProvider)));
    }
}
