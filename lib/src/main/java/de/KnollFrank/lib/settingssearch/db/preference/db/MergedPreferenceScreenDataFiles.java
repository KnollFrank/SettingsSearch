package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.io.File;
import java.util.stream.Stream;

record MergedPreferenceScreenDataFiles(File preferences,
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
