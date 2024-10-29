package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.Preference;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public record SearchResultsDescription(PreferenceScreenWithMap preferenceScreenWithMap,
                                       Map<Preference, PreferencePath> preferencePathByPreference,
                                       SearchableInfoAttribute searchableInfoAttribute) {
}
