package de.KnollFrank.lib.settingssearch.search;

import java.io.File;

public record MergedPreferenceScreenDataInput(File preferences,
                                              File preferencePathByPreference,
                                              File hostByPreference) {
}
