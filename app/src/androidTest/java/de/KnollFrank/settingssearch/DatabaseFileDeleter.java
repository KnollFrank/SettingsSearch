package de.KnollFrank.settingssearch;

import android.content.Context;

import java.io.File;

class DatabaseFileDeleter {

    public static void deleteDatabaseFile(final Context context, String dbName) {
        final File dbFile = context.getDatabasePath(dbName);
        deleteIfExists(dbFile);
        deleteIfExists(getShmFile(dbName, dbFile));
        deleteIfExists(getWalFile(dbName, dbFile));
    }

    private static File getShmFile(final String dbName, final File dbFile) {
        return new File(dbFile.getParent(), dbName + "-shm");
    }

    private static File getWalFile(final String dbName, final File dbFile) {
        return new File(dbFile.getParent(), dbName + "-wal");
    }

    private static void deleteIfExists(final File dbFile) {
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }
}
