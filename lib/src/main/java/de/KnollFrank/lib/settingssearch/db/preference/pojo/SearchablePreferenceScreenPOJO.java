package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.List;

public record SearchablePreferenceScreenPOJO(String title,
                                             String summary,
                                             List<SearchablePreferencePOJO> children) {
}
