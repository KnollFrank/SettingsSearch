package de.KnollFrank.lib.settingssearch.db;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;

public record SearchablePreferenceScreen(PreferenceScreen searchablePreferenceScreen,
                                         Map<Preference, SearchablePreference> searchablePreferenceByPreference) {
}
