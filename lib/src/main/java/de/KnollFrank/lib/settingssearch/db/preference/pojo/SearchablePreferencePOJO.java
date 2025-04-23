package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.Optional;

@Entity
public final class SearchablePreferencePOJO {

    @PrimaryKey
    private final int id;
    private final Optional<String> title;

    public SearchablePreferencePOJO(final int id, final Optional<String> title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public Optional<String> getTitle() {
        return title;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreferencePOJO that = (SearchablePreferencePOJO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
