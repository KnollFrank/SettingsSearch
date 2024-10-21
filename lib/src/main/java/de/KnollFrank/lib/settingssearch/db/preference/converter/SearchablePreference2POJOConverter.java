package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter.drawable2String;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchablePreference2POJOConverter {

    public record SearchablePreferencePOJOWithMap(SearchablePreferencePOJO searchablePreferencePOJO,
                                                  BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
    }

    public record SearchablePreferencePOJOsWithMap(
            List<SearchablePreferencePOJO> searchablePreferencePOJOs,
            BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
    }

    public static SearchablePreferencePOJOWithMap convert2POJO(final SearchablePreference searchablePreference,
                                                               final IdGenerator idGenerator) {
        final int id = idGenerator.nextId();
        final SearchablePreferencePOJOsWithMap searchablePreferencePOJOsWithMap =
                convert2POJOs(
                        SearchablePreferenceCaster.cast(Preferences.getImmediateChildren(searchablePreference)),
                        idGenerator);
        final SearchablePreferencePOJO searchablePreferencePOJO =
                SearchablePreferencePOJO.of(
                        id,
                        searchablePreference.getKey(),
                        drawable2String(searchablePreference.getIcon()),
                        searchablePreference.getLayoutResource(),
                        toString(searchablePreference.getSummary()),
                        toString(searchablePreference.getTitle()),
                        searchablePreference.getWidgetLayoutResource(),
                        Optional.ofNullable(searchablePreference.getFragment()),
                        searchablePreference.isVisible(),
                        SearchableInfoAttributeConverter.convert2POJO(searchablePreference.getSearchableInfo()),
                        searchablePreference.getExtras(),
                        searchablePreferencePOJOsWithMap.searchablePreferencePOJOs());
        return new SearchablePreferencePOJOWithMap(
                searchablePreferencePOJO,
                ImmutableBiMap
                        .<SearchablePreferencePOJO, SearchablePreference>builder()
                        .putAll(searchablePreferencePOJOsWithMap.pojoEntityMap())
                        .put(searchablePreferencePOJO, searchablePreference)
                        .buildOrThrow());
    }

    public static SearchablePreferencePOJOsWithMap convert2POJOs(final List<SearchablePreference> searchablePreferences,
                                                                 final IdGenerator idGenerator) {
        final List<SearchablePreferencePOJOWithMap> pojoWithMapList =
                searchablePreferences
                        .stream()
                        .map(searchablePreference -> convert2POJO(searchablePreference, idGenerator))
                        .collect(Collectors.toList());
        return new SearchablePreferencePOJOsWithMap(
                getSearchablePreferencePOJOs(pojoWithMapList),
                getPojoEntityMap(pojoWithMapList));
    }

    private static List<SearchablePreferencePOJO> getSearchablePreferencePOJOs(final List<SearchablePreferencePOJOWithMap> pojoWithMapList) {
        return pojoWithMapList
                .stream()
                .map(SearchablePreferencePOJOWithMap::searchablePreferencePOJO)
                .collect(Collectors.toList());
    }

    private static BiMap<SearchablePreferencePOJO, SearchablePreference> getPojoEntityMap(final List<SearchablePreferencePOJOWithMap> pojoWithMapList) {
        return Maps.mergeBiMaps(
                pojoWithMapList
                        .stream()
                        .map(SearchablePreferencePOJOWithMap::pojoEntityMap)
                        .collect(Collectors.toList()));
    }

    private static Optional<String> toString(final CharSequence charSequence) {
        return Optional
                .ofNullable(charSequence)
                .map(CharSequence::toString);
    }
}
