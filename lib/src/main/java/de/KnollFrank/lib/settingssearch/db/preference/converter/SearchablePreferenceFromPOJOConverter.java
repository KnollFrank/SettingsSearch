package de.KnollFrank.lib.settingssearch.db.preference.converter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.preference.PreferenceGroup;

import com.codepoetics.ambivalence.Either;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchablePreferenceFromPOJOConverter {

    public static BiMap<SearchablePreferencePOJO, SearchablePreference> addConvertedPOJO2Parent(
            final SearchablePreferencePOJO searchablePreferencePOJO,
            final PreferenceGroup parent,
            final StringGenerator preferenceKeyGenerator) {
        final SearchablePreference searchablePreference =
                createPlainSearchablePreferenceHavingKey(
                        searchablePreferencePOJO,
                        preferenceKeyGenerator.nextString(),
                        parent.getContext());
        parent.addPreference(searchablePreference);
        return ImmutableBiMap
                .<SearchablePreferencePOJO, SearchablePreference>builder()
                .put(searchablePreferencePOJO, searchablePreference)
                .putAll(addConvertedPOJOs2Parent(searchablePreferencePOJO.children(), searchablePreference, preferenceKeyGenerator))
                .build();
    }

    public static BiMap<SearchablePreferencePOJO, SearchablePreference> addConvertedPOJOs2Parent(
            final List<SearchablePreferencePOJO> searchablePreferencePOJOs,
            final PreferenceGroup parent,
            final StringGenerator preferenceKeyGenerator) {
        return Maps.mergeBiMaps(
                searchablePreferencePOJOs
                        .stream()
                        .map(searchablePreferencePOJO -> addConvertedPOJO2Parent(searchablePreferencePOJO, parent, preferenceKeyGenerator))
                        .collect(Collectors.toList()));
    }

    public static SearchablePreference createPlainSearchablePreference(final SearchablePreferencePOJO searchablePreferencePOJO,
                                                                       final Context context) {
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        context,
                        searchablePreferencePOJO.searchableInfo());
        copyAttributesFromSrc2Dst(
                searchablePreferencePOJO,
                searchablePreference,
                context.getResources());
        return searchablePreference;
    }

    private static SearchablePreference createPlainSearchablePreferenceHavingKey(final SearchablePreferencePOJO searchablePreferencePOJO,
                                                                                 final String key,
                                                                                 final Context context) {
        final SearchablePreference plainSearchablePreference = createPlainSearchablePreference(searchablePreferencePOJO, context);
        plainSearchablePreference.setKey(key);
        return plainSearchablePreference;
    }

    private static void copyAttributesFromSrc2Dst(final SearchablePreferencePOJO src,
                                                  final SearchablePreference dst,
                                                  final Resources resources) {
        dst.setKey(src.key().orElse(null));
        copyIconFromSrc2Dst(src, dst, resources);
        dst.setLayoutResource(src.layoutResId());
        dst.setSummary(src.summary().orElse(null));
        dst.setTitle(src.title().orElse(null));
        dst.setWidgetLayoutResource(src.widgetLayoutResId());
        dst.setFragment(src.fragment().orElse(null));
        dst.setVisible(src.visible());
        dst.getExtras().putAll(src.extras());
    }

    private static void copyIconFromSrc2Dst(final SearchablePreferencePOJO src, final SearchablePreference dst, final Resources resources) {
        SearchablePreferenceFromPOJOConverter
                .iconPixelData2Drawable(src.iconResourceIdOrIconPixelData(), resources)
                .forEither(dst::setIcon, dst::setIcon);
    }

    private static Either<Integer, Drawable> iconPixelData2Drawable(
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final Resources resources) {
        return iconResourceIdOrIconPixelData
                .map(_iconResourceIdOrIconPixelData ->
                        _iconResourceIdOrIconPixelData.map(
                                Function.identity(),
                                iconStr -> DrawableAndStringConverter.string2Drawable(iconStr, resources)))
                .orElse(Either.ofRight(null));
    }
}
