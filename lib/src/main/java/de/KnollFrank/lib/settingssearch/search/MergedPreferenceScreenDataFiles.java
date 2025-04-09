package de.KnollFrank.lib.settingssearch.search;

import java.io.File;
import java.util.stream.Stream;

// FK-TODO: make package private
public record MergedPreferenceScreenDataFiles(File preferences,
                                              File preferencePathByPreference,
                                              File hostByPreference) {

    public boolean exists() {
        return Stream
                .of(
                        preferences(),
                        preferencePathByPreference(),
                        hostByPreference())
                .allMatch(File::exists);
    }
}
