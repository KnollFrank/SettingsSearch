package de.KnollFrank.lib.settingssearch.db.preference;

import java.io.Serializable;
import java.util.List;

public record SearchablePreferencePOJO(
        String key,
        int iconResId,
        int layoutResId,
        String summary,
        String title,
        int widgetLayoutResId,
        String fragment,
        boolean visible,
        String searchableInfo,
        List<SearchablePreferencePOJO> children) implements Serializable {
}
