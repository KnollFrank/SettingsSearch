package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.ConnectedSearchablePreferenceScreensPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

class POJOTestFactory {

    public static ConnectedSearchablePreferenceScreensPOJO createSomeConnectedSearchablePreferenceScreensPOJO() {
        return new ConnectedSearchablePreferenceScreensPOJO(
                Set.of(createSomePreferenceScreenWithHostClassPOJO()),
                Map.of(createSomeSearchablePreferencePOJO(), createSomePreferencePathPOJO()));
    }

    private static PreferenceScreenWithHostClassPOJO createSomePreferenceScreenWithHostClassPOJO() {
        return new PreferenceScreenWithHostClassPOJO(
                createSomeSearchablePreferenceScreenPOJO(),
                PreferenceFragmentCompat.class);
    }

    private static PreferencePathPOJO createSomePreferencePathPOJO() {
        return new PreferencePathPOJO(List.of(createSomeSearchablePreferencePOJO()));
    }

    private static SearchablePreferencePOJO createSomeSearchablePreferencePOJO() {
        return new SearchablePreferencePOJO(
                0,
                "some key",
                4711,
                4712,
                "some summary",
                "some title",
                4713,
                "some fragment",
                true,
                "some searchableInfo",
                List.of(
                        new SearchablePreferencePOJO(
                                1,
                                "some key 2",
                                4714,
                                4715,
                                "some summary 2",
                                "some title 2",
                                4716,
                                "some fragment 2",
                                true,
                                "some searchableInfo 2",
                                List.of())));
    }

    private static SearchablePreferenceScreenPOJO createSomeSearchablePreferenceScreenPOJO() {
        return new SearchablePreferenceScreenPOJO(List.of(createSomeSearchablePreferencePOJO()));
    }
}
