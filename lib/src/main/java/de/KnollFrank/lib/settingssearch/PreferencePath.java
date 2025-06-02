package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableList;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public record PreferencePath(List<SearchablePreference> preferences) {

    public SearchablePreference getPreference() {
        return Lists.getLastElement(preferences).orElseThrow();
    }

    public PreferencePath append(final SearchablePreference preference) {
        return new PreferencePath(
                ImmutableList
                        .<SearchablePreference>builder()
                        .addAll(preferences)
                        .add(preference)
                        .build());
    }
}
