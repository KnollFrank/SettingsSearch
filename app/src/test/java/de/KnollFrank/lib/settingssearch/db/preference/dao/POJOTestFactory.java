package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter.drawable2String;

import android.content.res.Resources;
import android.os.Bundle;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;
import de.KnollFrank.settingssearch.R;

public class POJOTestFactory {

    public static SearchablePreferencePOJO createSomeSearchablePreferencePOJO(final Bundle extras,
                                                                              final Resources resources) {
        return SearchablePreferencePOJO.of(
                1,
                Optional.of("some key"),
                drawable2String(resources.getDrawable(R.drawable.smiley, null)),
                4712,
                Optional.of("some summary"),
                Optional.of("some title"),
                4713,
                Optional.of("some fragment"),
                true,
                "some searchableInfo",
                extras,
                List.of(
                        SearchablePreferencePOJO.of(
                                2,
                                Optional.of("some key 2"),
                                null,
                                4715,
                                Optional.of("some summary 2"),
                                Optional.of("some title 2"),
                                4716,
                                Optional.of("some fragment 2"),
                                true,
                                "some searchableInfo 2",
                                new Bundle(),
                                List.of())));
    }

    public static SearchablePreferencePOJO createSearchablePreferencePOJO(final Optional<String> title,
                                                                          final Optional<String> summary,
                                                                          final String searchableInfo) {
        return SearchablePreferencePOJO.of(
                1,
                Optional.of("some key"),
                null,
                4712,
                summary,
                title,
                4713,
                Optional.of("some fragment"),
                true,
                searchableInfo,
                new Bundle(),
                List.of());
    }

    public static SearchablePreferenceScreenPOJO createSomeSearchablePreferenceScreenPOJO(
            final SearchablePreferencePOJO searchablePreferencePOJO) {
        return new SearchablePreferenceScreenPOJO(
                "title of search results",
                "summary of search results",
                List.of(searchablePreferencePOJO));
    }
}
