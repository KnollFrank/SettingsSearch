package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import com.codepoetics.ambivalence.Either;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SearchablePreferencePOJO {

    private final int id;
    private final String key;
    private final Either<Integer, String> iconResourceIdOrIconPixelData;
    private final int layoutResId;
    private final String summary;
    private final String title;
    private final int widgetLayoutResId;
    private final String fragment;
    private final boolean visible;
    private final String searchableInfo;
    private final Bundle extras;
    private final List<SearchablePreferencePOJO> children;

    public SearchablePreferencePOJO(
            final int id,
            final Optional<String> key,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final int layoutResId,
            final Optional<String> summary,
            final Optional<String> title,
            final int widgetLayoutResId,
            final Optional<String> fragment,
            final boolean visible,
            final Optional<String> searchableInfo,
            final Bundle extras,
            final List<SearchablePreferencePOJO> children) {
        this.id = id;
        this.key = key.orElse(null);
        this.iconResourceIdOrIconPixelData = iconResourceIdOrIconPixelData.orElse(null);
        this.layoutResId = layoutResId;
        this.summary = summary.orElse(null);
        this.title = title.orElse(null);
        this.widgetLayoutResId = widgetLayoutResId;
        this.fragment = fragment.orElse(null);
        this.visible = visible;
        this.searchableInfo = searchableInfo.orElse(null);
        this.extras = extras;
        this.children = children;
    }

    public int id() {
        return id;
    }

    public List<SearchablePreferencePOJO> children() {
        return children;
    }

    public Optional<String> key() {
        return Optional.ofNullable(key);
    }

    public Optional<Either<Integer, String>> iconResourceIdOrIconPixelData() {
        return Optional.ofNullable(iconResourceIdOrIconPixelData);
    }

    public int layoutResId() {
        return layoutResId;
    }

    public Optional<String> summary() {
        return Optional.ofNullable(summary);
    }

    public Optional<String> title() {
        return Optional.ofNullable(title);
    }

    public int widgetLayoutResId() {
        return widgetLayoutResId;
    }

    public Optional<String> fragment() {
        return Optional.ofNullable(fragment);
    }

    public boolean visible() {
        return visible;
    }

    public Optional<String> searchableInfo() {
        return Optional.ofNullable(searchableInfo);
    }

    public Bundle extras() {
        return extras;
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

    @Override
    public String toString() {
        return "SearchablePreferencePOJO[" +
                "id=" + id + ", " +
                "key=" + key + ", " +
                "iconResourceIdOrIconPixelData=" + iconResourceIdOrIconPixelData + ", " +
                "layoutResId=" + layoutResId + ", " +
                "summary=" + summary + ", " +
                "title=" + title + ", " +
                "widgetLayoutResId=" + widgetLayoutResId + ", " +
                "fragment=" + fragment + ", " +
                "visible=" + visible + ", " +
                "searchableInfo=" + searchableInfo + ", " +
                "extras=" + extras + ", " +
                "children=" + children + ']';
    }
}
