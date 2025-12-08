package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Objects;
import java.util.StringJoiner;

class DatabaseState {

    private final boolean databaseChanged;

    private DatabaseState(final boolean databaseChanged) {
        this.databaseChanged = databaseChanged;
    }

    public static DatabaseState fromDatabaseChanged(final boolean databaseChanged) {
        return new DatabaseState(databaseChanged);
    }

    public boolean isDatabaseChanged() {
        return databaseChanged;
    }

    public DatabaseState combine(final DatabaseState databaseState) {
        return new DatabaseState(isDatabaseChanged() || databaseState.isDatabaseChanged());
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final DatabaseState that = (DatabaseState) o;
        return databaseChanged == that.databaseChanged;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(databaseChanged);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DatabaseState.class.getSimpleName() + "[", "]")
                .add("databaseChanged=" + databaseChanged)
                .toString();
    }
}
