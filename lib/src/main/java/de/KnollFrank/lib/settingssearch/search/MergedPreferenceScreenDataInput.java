package de.KnollFrank.lib.settingssearch.search;

import java.io.File;

record MergedPreferenceScreenDataInput(File preferences,
                                       File preferencePathByPreference,
                                       File hostByPreference) {
}
