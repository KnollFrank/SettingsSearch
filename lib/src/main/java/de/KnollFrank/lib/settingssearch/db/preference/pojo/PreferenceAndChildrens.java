package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PreferenceAndChildrens {

    public static Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> getChildrenByPreference(final Set<PreferenceAndChildren> preferencesAndChildren) {
        return preferencesAndChildren
                .stream()
                .collect(
                        Collectors.toMap(
                                PreferenceAndChildren::preference,
                                PreferenceAndChildren::children));
    }
}
