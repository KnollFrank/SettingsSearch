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
                "some key",
                drawable2String(resources.getDrawable(R.drawable.smiley, null)),
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
                                2,
                                "some key 2",
                                null,
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
