package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import java.io.File;
import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.IOUtils;

public class SearchDatabaseDirectoryIO {

    private final Context context;

    public SearchDatabaseDirectoryIO(final Context context) {
        this.context = context;
    }

    public File getAndMakeSearchDatabaseDirectory4Locale(final Locale locale) {
        final File searchDatabaseDirectory4Locale =
                new File(
                        getSearchDatabaseDirectory(),
                        locale.getLanguage());
        searchDatabaseDirectory4Locale.mkdirs();
        return searchDatabaseDirectory4Locale;
    }

    public void removeSearchDatabaseDirectories4AllLocales() {
        IOUtils.deleteDirectory(getSearchDatabaseDirectory());
    }

    private File getSearchDatabaseDirectory() {
        return context.getDir("settingssearch", Context.MODE_PRIVATE);
    }
}
