package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.dao.Exclude;

public record SearchablePreferencePOJO(
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
        List<SearchablePreferencePOJO> children,
        @Exclude Optional<SearchablePreference> origin) {

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
}
