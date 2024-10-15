package de.KnollFrank.lib.settingssearch.db.preference.dao;

import android.os.Bundle;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class POJOTestFactory {

    public static SearchablePreferencePOJO createSomeSearchablePreferencePOJO(final Bundle extras) {
        return new SearchablePreferencePOJO(
                "some key",
                null,
                4711,
                4712,
                "some summary",
                "some title",
                4713,
                "some fragment",
                true,
                "some searchableInfo",
                extras,
                List.of(
                        new SearchablePreferencePOJO(
                                "some key 2",
                                null,
                                4714,
                                4715,
                                "some summary 2",
                                "some title 2",
                                4716,
                                "some fragment 2",
                                true,
                                "some searchableInfo 2",
                                new Bundle(),
                                List.of(),
                                Optional.empty())),
                Optional.empty());
    }
}
