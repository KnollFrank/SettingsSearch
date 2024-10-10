package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

public record PreferenceScreenWithHostClassPOJO(int id,
                                                SearchablePreferenceScreenPOJO preferenceScreen,
                                                Class<? extends PreferenceFragmentCompat> host) {
}
