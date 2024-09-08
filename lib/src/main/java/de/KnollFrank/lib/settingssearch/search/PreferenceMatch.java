package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.Preference;

record PreferenceMatch(Preference preference, Type type, IndexRange indexRange) {

    public enum Type {
        TITLE, SUMMARY, SEARCHABLE_INFO
    }
}
