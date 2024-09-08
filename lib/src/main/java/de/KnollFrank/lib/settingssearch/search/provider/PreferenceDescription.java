package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.Preference;

public record PreferenceDescription<T extends Preference>(Class<T> preferenceClass,
                                                          SearchableInfoProvider searchableInfoProvider) {

}
