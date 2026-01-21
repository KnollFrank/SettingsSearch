package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.List;
import java.util.OptionalLong;

class DatabaseStateFactory {

    private DatabaseStateFactory() {
    }

    public static DatabaseState fromNumberOfChangedRows(final int numberOfChangedRows) {
        return DatabaseState.fromDatabaseChanged(numberOfChangedRows > 0);
    }

    public static DatabaseState fromInsertedRowIds(final List<Long> insertedRowIds) {
        return DatabaseState.fromDatabaseChanged(!insertedRowIds.isEmpty());
    }

    public static DatabaseState fromInsertedRowId(final long insertedRowId) {
        return fromInsertedRowId(getInsertedRowId(insertedRowId));
    }

    private static DatabaseState fromInsertedRowId(final OptionalLong insertedRowId) {
        return DatabaseState.fromDatabaseChanged(insertedRowId.isPresent());
    }

    private static OptionalLong getInsertedRowId(final long insertedRowId) {
        return insertedRowId == -1 ? OptionalLong.empty() : OptionalLong.of(insertedRowId);
    }
}
