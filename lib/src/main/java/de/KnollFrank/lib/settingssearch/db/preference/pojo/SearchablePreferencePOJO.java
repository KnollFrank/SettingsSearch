package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.dao.Exclude;

public final class SearchablePreferencePOJO {

    private final int id;
    private final String key;
    private final String icon;
    private final int layoutResId;
    private final String summary;
    private final String title;
    private final int widgetLayoutResId;
    private final String fragment;
    private final boolean visible;
    private final String searchableInfo;
    private final Bundle extras;
    private final List<SearchablePreferencePOJO> children;
    @Exclude
    private Optional<SearchablePreference> origin;

    // FK-TODO: remove origin and handle as an external Map<SearchablePreferencePOJO, SearchablePreference>
    public SearchablePreferencePOJO(
            final int id,
            final String key,
            final String icon,
            final int layoutResId,
            final String summary,
            final String title,
            final int widgetLayoutResId,
            final String fragment,
            final boolean visible,
            final String searchableInfo,
            final Bundle extras,
            final List<SearchablePreferencePOJO> children,
            final Optional<SearchablePreference> origin) {
        this.id = id;
        this.key = key;
        this.icon = icon;
        this.layoutResId = layoutResId;
        this.summary = summary;
        this.title = title;
        this.widgetLayoutResId = widgetLayoutResId;
        this.fragment = fragment;
        this.visible = visible;
        this.searchableInfo = searchableInfo;
        this.extras = extras;
        this.children = children;
        this.origin = origin;
    }

    public int id() {
        return id;
    }

    public String key() {
        return key;
    }

    public String icon() {
        return icon;
    }

    public int layoutResId() {
        return layoutResId;
    }

    public String summary() {
        return summary;
    }

    public String title() {
        return title;
    }

    public int widgetLayoutResId() {
        return widgetLayoutResId;
    }

    public String fragment() {
        return fragment;
    }

    public boolean visible() {
        return visible;
    }

    public String searchableInfo() {
        return searchableInfo;
    }

    public Bundle extras() {
        return extras;
    }

    public List<SearchablePreferencePOJO> children() {
        return children;
    }

    public Optional<SearchablePreference> getOrigin() {
        return origin;
    }

    public void setOrigin(final Optional<SearchablePreference> origin) {
        this.origin = origin;
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
        return "SearchablePreferencePOJO{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", icon='" + icon + '\'' +
                ", layoutResId=" + layoutResId +
                ", summary='" + summary + '\'' +
                ", title='" + title + '\'' +
                ", widgetLayoutResId=" + widgetLayoutResId +
                ", fragment='" + fragment + '\'' +
                ", visible=" + visible +
                ", searchableInfo='" + searchableInfo + '\'' +
                ", extras=" + extras +
                ", children=" + children +
                // ", origin=" + origin +
                '}';
    }
}
