package de.KnollFrank.lib.settingssearch.search;

import androidx.annotation.RawRes;

public record MergedPreferenceScreenDataInput(@RawRes int preferences,
                                              @RawRes int preferencePathByPreference,
                                              @RawRes int hostByPreference) {
}
