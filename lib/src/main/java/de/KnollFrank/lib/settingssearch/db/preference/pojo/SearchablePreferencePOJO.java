package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.dao.Exclude;

public final class SearchablePreferencePOJO {

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

    public SearchablePreferencePOJO(
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
        return layoutResId == that.layoutResId && visible == that.visible && widgetLayoutResId == that.widgetLayoutResId && Objects.equals(key, that.key) && Objects.equals(icon, that.icon) && Objects.equals(title, that.title) && Objects.equals(summary, that.summary) && Objects.equals(fragment, that.fragment) && Objects.equals(searchableInfo, that.searchableInfo) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, icon, layoutResId, summary, title, widgetLayoutResId, fragment, visible, searchableInfo, children);
    }

    @Override
    public String toString() {
        return "SearchablePreferencePOJO[" +
                "key=" + key + ", " +
                "icon=" + icon + ", " +
                "layoutResId=" + layoutResId + ", " +
                "summary=" + summary + ", " +
                "title=" + title + ", " +
                "widgetLayoutResId=" + widgetLayoutResId + ", " +
                "fragment=" + fragment + ", " +
                "visible=" + visible + ", " +
                "searchableInfo=" + searchableInfo + ", " +
                "extras=" + extras + ", " +
                "children=" + children + ", " +
                "origin=" + origin + ']';
    }
}
