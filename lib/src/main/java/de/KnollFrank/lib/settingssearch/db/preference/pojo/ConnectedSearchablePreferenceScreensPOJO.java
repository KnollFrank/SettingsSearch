package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Set;

public record ConnectedSearchablePreferenceScreensPOJO(
        Set<PreferenceScreenWithHostClassPOJO> connectedSearchablePreferenceScreens,
        Map<SearchablePreferencePOJO, PreferencePathPOJO> preferencePathByPreference) {
}
