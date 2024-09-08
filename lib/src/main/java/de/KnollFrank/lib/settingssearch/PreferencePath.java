package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;

public record PreferencePath(List<Preference> preferences) {

    public Optional<Preference> getPreference() {
        return Lists.getLastElement(preferences);
    }

    public PreferencePath add(final Preference preference) {
        return new PreferencePath(
                ImmutableList
                        .<Preference>builder()
                        .addAll(preferences)
                        .add(preference)
                        .build());
    }
}
