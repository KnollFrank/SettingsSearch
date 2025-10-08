package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.io.File;

public record PrepackagedAppDatabase(File prepackagedDatabaseAssetFile,
                                     AppDatabaseProcessor prepackagedAppDatabaseProcessor) {
}
