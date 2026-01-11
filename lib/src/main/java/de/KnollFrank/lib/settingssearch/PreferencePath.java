package de.KnollFrank.lib.settingssearch;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;

public record PreferencePath(List<SearchablePreferenceOfHostWithinTree> preferences) {

    public PreferencePath {
        Preconditions.checkArgument(!preferences.isEmpty());
    }

    public SearchablePreferenceOfHostWithinTree getStart() {
        return Lists
                .getHead(preferences)
                .orElseThrow();
    }

    public SearchablePreferenceOfHostWithinTree getEnd() {
        return Lists
                .getLastElement(preferences)
                .orElseThrow();
    }

    public Optional<PreferencePath> getTail() {
        final List<SearchablePreferenceOfHostWithinTree> tailOfPreferences =
                Lists
                        .getTail(preferences)
                        .orElseThrow();
        return tailOfPreferences.isEmpty() ?
                Optional.empty() :
                Optional.of(new PreferencePath(tailOfPreferences));
    }

    public PreferencePath append(final SearchablePreferenceOfHostWithinTree preference) {
        return new PreferencePath(
                ImmutableList
                        .<SearchablePreferenceOfHostWithinTree>builder()
                        .addAll(preferences)
                        .add(preference)
                        .build());
    }
}
