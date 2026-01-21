package de.KnollFrank.lib.settingssearch.common;

public class LockingSupport {

    public static final Object searchDatabaseLock = new Object();

    private LockingSupport() {
    }
}
