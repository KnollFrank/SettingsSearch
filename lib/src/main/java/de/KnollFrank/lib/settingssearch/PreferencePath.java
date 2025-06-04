package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableList;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

public record PreferencePath(List<SearchablePreferenceEntity> preferences) {

    public SearchablePreferenceEntity getPreference() {
        return Lists.getLastElement(preferences).orElseThrow();
    }

    public PreferencePath append(final SearchablePreferenceEntity preference) {
        return new PreferencePath(
                ImmutableList
                        .<SearchablePreferenceEntity>builder()
                        .addAll(preferences)
                        .add(preference)
                        .build());
    }
}
