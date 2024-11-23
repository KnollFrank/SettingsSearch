package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class IconProvider {

    private final Map<SearchablePreferencePOJO, Optional<Drawable>> iconByPreference = new HashMap<>();

    // FK-TODO: move method into SearchablePreferencePOJO?
    public Optional<Drawable> getIcon(final SearchablePreferencePOJO searchablePreferencePOJO,
                                      final Context context) {
        if (!iconByPreference.containsKey(searchablePreferencePOJO)) {
            iconByPreference.put(searchablePreferencePOJO, _getIcon(searchablePreferencePOJO, context));
        }
        return iconByPreference.get(searchablePreferencePOJO);
    }

    private static Optional<Drawable> _getIcon(final SearchablePreferencePOJO searchablePreferencePOJO,
                                               final Context context) {
        return searchablePreferencePOJO
                .iconResourceIdOrIconPixelData()
                .map(iconResourceIdOrIconPixelData ->
                        iconResourceIdOrIconPixelData.join(
                                iconResourceId -> AppCompatResources.getDrawable(context, iconResourceId),
                                iconPixelData ->
                                        DrawableAndStringConverter.string2Drawable(
                                                iconPixelData,
                                                context.getResources())));
    }
}
