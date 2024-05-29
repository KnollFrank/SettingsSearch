package com.bytehamster.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

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
        return ImmutableSet
                .<PreferenceScreenWithHost>builder()
                .add(root)
                .addAll(
                        this
                                .getChildren(root)
                                .stream()
                                .flatMap(child -> getPreferenceScreens(child).stream())
                                .collect(Collectors.toSet()))
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
