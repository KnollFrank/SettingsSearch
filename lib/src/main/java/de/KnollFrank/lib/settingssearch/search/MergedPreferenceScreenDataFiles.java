package de.KnollFrank.lib.settingssearch.search;

import java.io.File;

public record MergedPreferenceScreenDataFiles(File preferences,
                                              File preferencePathByPreference,
                                              File hostByPreference) {
}
