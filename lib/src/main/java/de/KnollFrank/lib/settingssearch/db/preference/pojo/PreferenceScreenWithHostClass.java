package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

public record PreferenceScreenWithHostClass(int id,
                                            SearchablePreferenceScreen preferenceScreen,
                                            Class<? extends PreferenceFragmentCompat> host) {
}
