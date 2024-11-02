package de.KnollFrank.lib.settingssearch.search;

import androidx.annotation.RawRes;

public record MergedPreferenceScreenDataInput(@RawRes int allPreferencesForSearch,
                                              @RawRes int preferencePathByPreference,
                                              @RawRes int hostByPreference) {
}
