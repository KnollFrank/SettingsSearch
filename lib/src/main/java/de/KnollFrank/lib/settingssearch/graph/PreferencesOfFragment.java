package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import java.util.List;
import java.util.Optional;

public record PreferencesOfFragment(List<Preference> preferences,
                                    Optional<String> title,
                                    Optional<String> summary) {
}
