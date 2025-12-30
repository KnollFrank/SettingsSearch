package de.KnollFrank.lib.settingssearch;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinGraph;

public record PreferencePath(List<SearchablePreferenceWithinGraph> preferences) {

    public PreferencePath {
        Preconditions.checkArgument(!preferences.isEmpty());
    }

    public SearchablePreferenceWithinGraph getStart() {
        return Lists
                .getHead(preferences)
                .orElseThrow();
    }

    public SearchablePreferenceWithinGraph getEnd() {
        return Lists
                .getLastElement(preferences)
                .orElseThrow();
    }

    public Optional<PreferencePath> getTail() {
        final List<SearchablePreferenceWithinGraph> tailOfPreferences =
                Lists
                        .getTail(preferences)
                        .orElseThrow();
        return tailOfPreferences.isEmpty() ?
                Optional.empty() :
                Optional.of(new PreferencePath(tailOfPreferences));
    }

    public PreferencePath append(final SearchablePreferenceWithinGraph preference) {
        return new PreferencePath(
                ImmutableList
                        .<SearchablePreferenceWithinGraph>builder()
                        .addAll(preferences)
                        .add(preference)
                        .build());
    }
}
