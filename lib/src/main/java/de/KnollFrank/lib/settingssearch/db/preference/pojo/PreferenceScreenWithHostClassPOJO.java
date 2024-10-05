package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

public record PreferenceScreenWithHostClassPOJO(SearchablePreferenceScreenPOJO preferenceScreen,
                                                Class<? extends PreferenceFragmentCompat> host) {
}
