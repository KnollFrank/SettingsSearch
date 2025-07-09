package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

class SearchablePreferenceToSearchablePreferenceEntityTransformer {

    private final Map<String, SearchablePreferenceEntity> preferenceById;

    SearchablePreferenceToSearchablePreferenceEntityTransformer(final Map<String, SearchablePreferenceEntity> preferenceById) {
        this.preferenceById = preferenceById;
    }

    public Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> transform(final Map<SearchablePreference, Set<SearchablePreference>> map) {
        return Maps.mapKeysAndValues(map, this::transform, this::transform);
    }

    private SearchablePreferenceEntity transform(final SearchablePreference searchablePreference) {
        return preferenceById.get(searchablePreference.getId());
    }

    private Set<SearchablePreferenceEntity> transform(final Set<SearchablePreference> preferences) {
        return preferences
                .stream()
                .map(this::transform)
                .collect(Collectors.toSet());
    }
}
