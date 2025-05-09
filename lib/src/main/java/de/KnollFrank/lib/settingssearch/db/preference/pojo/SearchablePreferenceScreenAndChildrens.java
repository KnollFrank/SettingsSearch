package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchablePreferenceScreenAndChildrens {

    public static Map<SearchablePreferenceScreen, List<SearchablePreferenceScreen>> getChildrenBySearchablePreferenceScreen(final List<SearchablePreferenceScreenAndChildren> searchablePreferenceScreenAndChildrenList) {
        return searchablePreferenceScreenAndChildrenList
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferenceScreenAndChildren::searchablePreferenceScreen,
                                SearchablePreferenceScreenAndChildren::children));
    }
}
