package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.List;

public record SearchablePreferenceScreen(String title,
                                         String summary,
                                         List<SearchablePreference> children) {
}
