package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter.string2Drawable;

import android.content.Context;
import android.content.res.Resources;

import androidx.preference.PreferenceGroup;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchablePreferenceFromPOJOConverter {

    public static BiMap<SearchablePreferencePOJO, SearchablePreference> addConvertedPOJO2Parent(
            final SearchablePreferencePOJO searchablePreferencePOJO,
            final PreferenceGroup parent) {
        final SearchablePreference searchablePreference = createPlainSearchablePreference(searchablePreferencePOJO, parent.getContext());
        parent.addPreference(searchablePreference);
        return ImmutableBiMap
                .<SearchablePreferencePOJO, SearchablePreference>builder()
                .put(searchablePreferencePOJO, searchablePreference)
                .putAll(addConvertedPOJOs2Parent(searchablePreferencePOJO.children(), searchablePreference))
                .build();
    }

    public static BiMap<SearchablePreferencePOJO, SearchablePreference> addConvertedPOJOs2Parent(
            final List<SearchablePreferencePOJO> searchablePreferencePOJOs,
            final PreferenceGroup parent) {
        return Maps.mergeBiMaps(
                searchablePreferencePOJOs
                        .stream()
                        .map(searchablePreferencePOJO -> addConvertedPOJO2Parent(searchablePreferencePOJO, parent))
                        .collect(Collectors.toList()));
    }

    private static SearchablePreference createPlainSearchablePreference(final SearchablePreferencePOJO searchablePreferencePOJO,
                                                                        final Context context) {
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        context,
                        SearchableInfoAttributeConverter.convertFromPOJO(searchablePreferencePOJO.searchableInfo()));
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
