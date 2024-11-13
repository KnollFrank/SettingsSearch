package de.KnollFrank.lib.settingssearch.search;

import java.io.File;

record MergedPreferenceScreenDataFiles(File preferences,
                                       File preferencePathByPreference,
                                       File hostByPreference) {
}
