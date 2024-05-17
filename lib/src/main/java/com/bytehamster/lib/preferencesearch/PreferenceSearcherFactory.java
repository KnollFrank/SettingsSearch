package com.bytehamster.lib.preferencesearch;

import androidx.annotation.XmlRes;
import androidx.preference.Preference;

import java.util.List;

class PreferenceSearcherFactory {

    public static PreferenceSearcher createPreferenceSearcher(final List<Preference> preferences,
                                                              @XmlRes final int resId) {
        return new PreferenceSearcher(PreferenceItems.getPreferenceItems(preferences, resId));
    }
}
