package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter.drawable2String;

import android.content.res.Resources;
import android.os.Bundle;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.settingssearch.R;

public class POJOTestFactory {

    public static SearchablePreferencePOJO createSomeSearchablePreferencePOJO(final Bundle extras,
                                                                              final Resources resources) {
        return new SearchablePreferencePOJO(
                1,
                Optional.of("some key"),
                drawable2String(Optional.of(resources.getDrawable(R.drawable.smiley, null))),
                4712,
                Optional.of("some summary"),
                Optional.of("some title"),
                4713,
                Optional.of("some fragment"),
                true,
                Optional.of("some searchableInfo"),
                extras,
                List.of(
                        new SearchablePreferencePOJO(
                                2,
                                Optional.of("some key 2"),
                                Optional.empty(),
                                4715,
                                Optional.of("some summary 2"),
                                Optional.of("some title 2"),
                                4716,
                                Optional.of("some fragment 2"),
                                true,
                                Optional.of("some searchableInfo 2"),
                                new Bundle(),
                                List.of())));
    }

    public static SearchablePreferencePOJO createSearchablePreferencePOJO(final Optional<String> title,
                                                                          final Optional<String> summary,
                                                                          final Optional<String> searchableInfo) {
        return createSearchablePreferencePOJO(1, title, summary, searchableInfo);
    }

    public static SearchablePreferencePOJO createSearchablePreferencePOJO(
            final int id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo) {
        return new SearchablePreferencePOJO(
                id,
                Optional.of("some key"),
                Optional.empty(),
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
}
