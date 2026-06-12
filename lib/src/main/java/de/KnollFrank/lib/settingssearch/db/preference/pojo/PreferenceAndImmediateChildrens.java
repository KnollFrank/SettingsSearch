package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PreferenceAndImmediateChildrens {

    private PreferenceAndImmediateChildrens() {
    }

    public static Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> getImmediateChildrenByPreference(final Set<PreferenceAndImmediateChildren> preferencesAndChildren) {
        return preferencesAndChildren
                .stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                PreferenceAndImmediateChildren::preference,
                                PreferenceAndImmediateChildren::immediateChildren));
    }
}
