package de.KnollFrank.lib.settingssearch;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;

public record PreferencePath(List<SearchablePreferenceOfHostWithinGraph> preferences) {

    public PreferencePath {
        Preconditions.checkArgument(!preferences.isEmpty());
    }

    public SearchablePreferenceOfHostWithinGraph getStart() {
        return Lists
                .getHead(preferences)
                .orElseThrow();
    }

    public SearchablePreferenceOfHostWithinGraph getEnd() {
        return Lists
                .getLastElement(preferences)
                .orElseThrow();
    }

    public Optional<PreferencePath> getTail() {
        final List<SearchablePreferenceOfHostWithinGraph> tailOfPreferences =
                Lists
                        .getTail(preferences)
                        .orElseThrow();
        return tailOfPreferences.isEmpty() ?
                Optional.empty() :
                Optional.of(new PreferencePath(tailOfPreferences));
    }

    public PreferencePath append(final SearchablePreferenceOfHostWithinGraph preference) {
        return new PreferencePath(
                ImmutableList
                        .<SearchablePreferenceOfHostWithinGraph>builder()
                        .addAll(preferences)
                        .add(preference)
                        .build());
    }
}
