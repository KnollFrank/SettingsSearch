package de.KnollFrank.lib.settingssearch;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public record PreferencePath(List<SearchablePreference> preferences) {

    public PreferencePath {
        Preconditions.checkArgument(!preferences.isEmpty());
    }

    public SearchablePreference getStart() {
        return Lists
                .getHead(preferences)
                .orElseThrow();
    }

    public SearchablePreference getEnd() {
        return Lists
                .getLastElement(preferences)
                .orElseThrow();
    }

    public Optional<PreferencePath> getTail() {
        final List<SearchablePreference> tailOfPreferences =
                Lists
                        .getTail(preferences)
                        .orElseThrow();
        return tailOfPreferences.isEmpty() ?
                Optional.empty() :
                Optional.of(new PreferencePath(tailOfPreferences));
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
