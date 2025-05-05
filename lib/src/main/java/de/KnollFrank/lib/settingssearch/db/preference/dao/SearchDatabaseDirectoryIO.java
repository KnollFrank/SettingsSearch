package de.KnollFrank.lib.settingssearch.db.preference.dao;

import android.content.Context;

import java.io.File;
import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.IOUtils;
import de.KnollFrank.lib.settingssearch.common.LockingSupport;

// FK-TODO: remove
public class SearchDatabaseDirectoryIO {

    private final Context context;

    public SearchDatabaseDirectoryIO(final Context context) {
        this.context = context;
    }

    public File getAndMakeSearchDatabaseDirectory4Locale(final Locale locale) {
        synchronized (LockingSupport.searchDatabaseLock) {
            final File searchDatabaseDirectory4Locale =
                    new File(
                            getSearchDatabaseDirectory(),
                            locale.getLanguage());
            searchDatabaseDirectory4Locale.mkdirs();
            return searchDatabaseDirectory4Locale;
        }
    }

    public void removeSearchDatabaseDirectories4AllLocales() {
        synchronized (LockingSupport.searchDatabaseLock) {
            IOUtils.deleteDirectory(getSearchDatabaseDirectory());
        }
    }

    private File getSearchDatabaseDirectory() {
        return context.getDir("settingssearch", Context.MODE_PRIVATE);
    }
}
