package de.KnollFrank.lib.settingssearch.fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public record PreferencePathPointer(PreferencePath preferencePath,
                                    int indexWithinPreferencePath) {

    public SearchablePreference dereference() {
        return this
                .getElementAtIndex(indexWithinPreferencePath)
                .orElseThrow();
    }

    public Optional<PreferencePathPointer> next() {
        return getElementAtIndex(indexWithinPreferencePath + 1).isPresent() ?
                Optional.of(
                        new PreferencePathPointer(
                                preferencePath,
                                indexWithinPreferencePath + 1)) :
                Optional.empty();
    }

    private Optional<SearchablePreference> getElementAtIndex(final int indexWithinPreferencePath) {
        return Lists.getElementAtIndex(
                preferencePath.preferences(),
                indexWithinPreferencePath);
    }
}
