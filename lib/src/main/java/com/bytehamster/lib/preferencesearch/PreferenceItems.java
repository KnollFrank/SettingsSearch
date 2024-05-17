package com.bytehamster.lib.preferencesearch;

import androidx.annotation.XmlRes;
import androidx.preference.Preference;

import java.util.List;

public class PreferenceItems {

    public static List<PreferenceItem> getPreferenceItems(final List<Preference> preferences,
                                                          @XmlRes final int resId) {
        return new SearchConfiguration().indexItems(preferences, resId);
    }
}
