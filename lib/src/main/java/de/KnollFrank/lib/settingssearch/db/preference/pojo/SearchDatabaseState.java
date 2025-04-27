package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SearchDatabaseState {

    @PrimaryKey
    private final int id;
    private boolean initialized;

    public SearchDatabaseState(final int id, final boolean initialized) {
        this.id = id;
        this.initialized = initialized;
    }

    public int getId() {
        return id;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(final boolean initialized) {
        this.initialized = initialized;
    }
}
