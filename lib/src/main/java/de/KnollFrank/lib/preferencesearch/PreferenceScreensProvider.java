package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public Set<PreferenceScreenWithHost> getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        final Set<PreferenceScreenWithHost> connectedPreferenceScreens = new HashSet<>();
        getConnectedPreferenceScreens(
                PreferenceScreenWithHost.fromPreferenceFragment(root),
                connectedPreferenceScreens);
        return connectedPreferenceScreens;
    }

    private void getConnectedPreferenceScreens(final PreferenceScreenWithHost root,
                                               final Set<PreferenceScreenWithHost> connectedPreferenceScreens) {
        if (connectedPreferenceScreens.contains(root)) {
            return;
        }
        connectedPreferenceScreens.add(root);
        for (final PreferenceScreenWithHost child : getChildren(root)) {
            getConnectedPreferenceScreens(child, connectedPreferenceScreens);
        }
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
