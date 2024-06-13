package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.PreferenceGroup;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHosts;
import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;
import de.KnollFrank.lib.preferencesearch.matcher.PreferenceMatcher;

class PreferenceSearcher {

    // FK-TODO: make private again
    public final PreferenceScreenWithHosts preferenceScreenWithHosts;

    public PreferenceSearcher(final PreferenceScreenWithHosts preferenceScreenWithHosts) {
        this.preferenceScreenWithHosts = preferenceScreenWithHosts;
    }

    public List<PreferenceWithHost> searchFor(final String keyword) {
        return preferenceScreenWithHosts
                .preferenceWithHostList
                .stream()
                .filter(preferenceWithHost -> !(preferenceWithHost.preference instanceof PreferenceGroup))
                .filter(preferenceWithHost -> PreferenceMatcher.matches(preferenceWithHost.preference, keyword))
                .collect(Collectors.toList());
    }
}
