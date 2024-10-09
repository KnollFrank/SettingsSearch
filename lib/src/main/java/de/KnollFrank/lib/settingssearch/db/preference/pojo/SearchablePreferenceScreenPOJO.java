package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.List;

// FK-TODO: Kann SearchablePreferenceScreenPOJO einfach durch SearchablePreferencePOJO ersetzt werden?
public record SearchablePreferenceScreenPOJO(String title,
                                             String summary,
                                             List<SearchablePreferencePOJO> children) {
}
