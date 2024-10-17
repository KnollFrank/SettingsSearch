package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter.string2Drawable;

import android.content.Context;
import android.content.res.Resources;

import androidx.preference.PreferenceGroup;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class SearchablePreferenceFromPOJOConverter {

    public static void addConvertedPOJO2Parent(final SearchablePreferencePOJO searchablePreferencePOJO,
                                               final PreferenceGroup parent,
                                               final Context context) {
        final SearchablePreference searchablePreference = createPlainSearchablePreference(searchablePreferencePOJO, context);
        parent.addPreference(searchablePreference);
        addConvertedPOJOs2Parent(searchablePreferencePOJO.children(), searchablePreference, context);
    }

    public static void addConvertedPOJOs2Parent(final List<SearchablePreferencePOJO> searchablePreferencePOJOs,
                                                final PreferenceGroup parent,
                                                final Context context) {
        for (final SearchablePreferencePOJO searchablePreferencePOJO : searchablePreferencePOJOs) {
            addConvertedPOJO2Parent(searchablePreferencePOJO, parent, context);
        }
    }

    private static SearchablePreference createPlainSearchablePreference(final SearchablePreferencePOJO searchablePreferencePOJO,
                                                                        final Context context) {
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        context,
                        SearchableInfoAttributeConverter.convertFromPOJO(searchablePreferencePOJO.searchableInfo()),
                        Optional.of(searchablePreferencePOJO));
        copyAttributesFromSrc2Dst(searchablePreferencePOJO, searchablePreference, context.getResources());
        return searchablePreference;
    }

    private static void copyAttributesFromSrc2Dst(final SearchablePreferencePOJO src,
                                                  final SearchablePreference dst,
                                                  final Resources resources) {
        dst.setKey(src.key());
        dst.setIcon(string2Drawable(src.icon(), resources));
        dst.setLayoutResource(src.layoutResId());
        dst.setSummary(src.summary());
        dst.setTitle(src.title());
        dst.setWidgetLayoutResource(src.widgetLayoutResId());
        dst.setFragment(src.fragment());
        dst.setVisible(src.visible());
        dst.getExtras().putAll(src.extras());
    }
}
