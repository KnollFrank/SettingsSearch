package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record SearchablePreferencePOJO(
        int id,
        String key,
        String icon,
        int layoutResId,
        String summary,
        String title,
        int widgetLayoutResId,
        String fragment,
        boolean visible,
        String searchableInfo,
        Bundle extras,
        List<SearchablePreferencePOJO> children) {

    public Optional<String> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public Optional<String> optionalSummary() {
        return Optional.ofNullable(summary);
    }

    public Optional<String> optionalSearchableInfo() {
        return Optional.ofNullable(searchableInfo);
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
