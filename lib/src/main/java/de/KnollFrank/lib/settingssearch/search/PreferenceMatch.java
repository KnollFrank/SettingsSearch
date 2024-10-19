package de.KnollFrank.lib.settingssearch.search;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

record PreferenceMatch(SearchablePreferencePOJO preference, Type type, IndexRange indexRange) {

    public enum Type {
        TITLE, SUMMARY, SEARCHABLE_INFO
    }
}
