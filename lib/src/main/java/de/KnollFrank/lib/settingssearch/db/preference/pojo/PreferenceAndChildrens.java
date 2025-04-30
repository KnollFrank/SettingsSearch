package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreferenceAndChildrens {

    public static Map<SearchablePreference, List<SearchablePreference>> getChildrenByPreference(final List<PreferenceAndChildren> preferencesAndChildren) {
        return preferencesAndChildren
                .stream()
                .collect(
                        Collectors.toMap(
                                PreferenceAndChildren::preference,
                                PreferenceAndChildren::children));
    }
}
