package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private List<SearchablePreferencePOJO> children = Collections.emptyList();
    @Exclude
    private SearchablePreferencePOJO parent;

    private SearchablePreferencePOJO(
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
            final Bundle extras) {
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
    }

    public static SearchablePreferencePOJO of(
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
            final List<SearchablePreferencePOJO> children) {
        final SearchablePreferencePOJO searchablePreferencePOJO =
                new SearchablePreferencePOJO(
                        id,
                        key,
                        icon,
                        layoutResId,
                        summary,
                        title,
                        widgetLayoutResId,
                        fragment,
                        visible,
                        searchableInfo,
                        extras);
        searchablePreferencePOJO.setChildren(children);
        return searchablePreferencePOJO;
    }

    public int id() {
        return id;
    }

    public Optional<SearchablePreferencePOJO> getParent() {
        return Optional.ofNullable(parent);
    }

    private void setParent(final Optional<SearchablePreferencePOJO> parent) {
        this.parent = parent.orElse(null);
    }

    public List<SearchablePreferencePOJO> children() {
        return children;
    }

    private void setChildren(final List<SearchablePreferencePOJO> children) {
        this.children = children;
        for (final SearchablePreferencePOJO child : children) {
            child.setParent(Optional.of(this));
        }
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

    public Optional<String> optionalSummary() {
        return Optional.ofNullable(summary);
    }

    public String title() {
        return title;
    }

    // FK-TODO: for each nullable field (e.g. title, summary, ...) replace it's getter (e.g. title()) with it's optional version (see method optionalTitle())
    public Optional<String> optionalTitle() {
        return Optional.ofNullable(title);
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

    public Optional<String> optionalSearchableInfo() {
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
                "icon=" + icon + ", " +
                "layoutResId=" + layoutResId + ", " +
                "summary=" + summary + ", " +
                "title=" + title + ", " +
                "widgetLayoutResId=" + widgetLayoutResId + ", " +
                "fragment=" + fragment + ", " +
                "visible=" + visible + ", " +
                "searchableInfo=" + searchableInfo + ", " +
                "extras=" + extras + ", " +
                "parent=" + parent + ", " +
                "children=" + children + ']';
    }
}
