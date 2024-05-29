package com.bytehamster.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.Sets;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PreferenceScreensProvider {

    private final PreferenceFragments preferenceFragments;

    public PreferenceScreensProvider(final PreferenceFragments preferenceFragments) {
        this.preferenceFragments = preferenceFragments;
    }

    public Set<PreferenceScreenWithHost> getPreferenceScreens(final PreferenceFragmentCompat root) {
        this.preferenceFragments.initialize(root);
        return getPreferenceScreens(PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(root));
    }

    private Set<PreferenceScreenWithHost> getPreferenceScreens(final PreferenceScreenWithHost root) {
        final Set<PreferenceScreenWithHost> preferenceScreensOfChildren =
                Sets.union(
                        this
                                .getChildren(root)
                                .stream()
                                .map(this::getPreferenceScreens)
                                .collect(Collectors.toSet()));
        return ImmutableSet
                .<PreferenceScreenWithHost>builder()
                .add(root)
                .addAll(preferenceScreensOfChildren)
                .build();
    }

    private List<PreferenceScreenWithHost> getChildren(final PreferenceScreenWithHost preferenceScreen) {
        return PreferenceParser
                .getPreferences(preferenceScreen.preferenceScreen)
                .stream()
                .map(Preference::getFragment)
                .filter(Objects::nonNull)
                .map(this.preferenceFragments::getPreferenceScreenOfFragment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
