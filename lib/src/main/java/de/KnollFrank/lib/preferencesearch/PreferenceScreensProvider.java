package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.common.Sets;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public Set<PreferenceScreenWithHost> getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        return getConnectedPreferenceScreens(PreferenceScreenWithHost.fromPreferenceFragment(root));
    }

    private Set<PreferenceScreenWithHost> getConnectedPreferenceScreens(final PreferenceScreenWithHost root) {
        return ImmutableSet
                .<PreferenceScreenWithHost>builder()
                .add(root)
                .addAll(getPreferenceScreensOfChildren(root))
                .build();
    }

    private Set<PreferenceScreenWithHost> getPreferenceScreensOfChildren(final PreferenceScreenWithHost root) {
        return Sets.union(
                this
                        .getChildren(root)
                        .stream()
                        .map(this::getConnectedPreferenceScreens)
                        .collect(Collectors.toSet()));
    }

    private List<PreferenceScreenWithHost> getChildren(final PreferenceScreenWithHost preferenceScreenWithHost) {
        return Preferences
                .getAllChildren(preferenceScreenWithHost.preferenceScreen)
                .stream()
                .map(Preference::getFragment)
                .filter(Objects::nonNull)
                .map(this.preferenceScreenWithHostProvider::getPreferenceScreenOfFragment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
