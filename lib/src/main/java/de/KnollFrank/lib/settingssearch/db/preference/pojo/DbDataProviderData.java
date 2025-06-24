package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public record DbDataProviderData(
        Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> allPreferencesBySearchablePreferenceScreen,
        Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference,
        Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> predecessorByPreference,
        Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference) {

    public static DbDataProviderDataBuilder builder() {
        return new DbDataProviderDataBuilder();
    }
}
