package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import com.codepoetics.ambivalence.Either;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.provider.IconProvider;

public class Preference2SearchablePreferencePOJOConverter {

    private final IconProvider iconProvider;
    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;
    private final IdGenerator idGenerator;

    public Preference2SearchablePreferencePOJOConverter(
            final IconProvider iconProvider,
            final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider,
            final IdGenerator idGenerator) {
        this.iconProvider = iconProvider;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
        this.idGenerator = idGenerator;
    }

    public record SearchablePreferencePOJOWithMap(SearchablePreferencePOJO searchablePreferencePOJO,
                                                  BiMap<SearchablePreferencePOJO, Preference> pojoEntityMap) {
    }

    public record SearchablePreferencePOJOsWithMap(
            List<SearchablePreferencePOJO> searchablePreferencePOJOs,
            BiMap<SearchablePreferencePOJO, Preference> pojoEntityMap) {
    }

    public SearchablePreferencePOJOWithMap convert2POJO(final Preference preference,
                                                        final PreferenceFragmentCompat hostOfPreference) {
        final int id = idGenerator.nextId();
        final SearchablePreferencePOJOsWithMap searchablePreferencePOJOsWithMap =
                convertChildren2POJOs(preference, hostOfPreference);
        final SearchablePreferencePOJO searchablePreferencePOJO =
                new SearchablePreferencePOJO(
                        id,
                        Optional.ofNullable(preference.getKey()),
                        getIconResourceIdOrIconPixelData(preference, hostOfPreference),
                        preference.getLayoutResource(),
                        toString(preference.getSummary()),
                        toString(preference.getTitle()),
                        preference.getWidgetLayoutResource(),
                        Optional.ofNullable(preference.getFragment()),
                        preference.isVisible(),
                        searchableInfoAndDialogInfoProvider.getSearchableInfo(preference, hostOfPreference),
                        preference.getExtras(),
                        searchablePreferencePOJOsWithMap.searchablePreferencePOJOs());
        return new SearchablePreferencePOJOWithMap(
                searchablePreferencePOJO,
                ImmutableBiMap
                        .<SearchablePreferencePOJO, Preference>builder()
                        .putAll(searchablePreferencePOJOsWithMap.pojoEntityMap())
                        .put(searchablePreferencePOJO, preference)
                        .buildOrThrow());
    }

    public SearchablePreferencePOJOsWithMap convert2POJOs(final List<Preference> preferences,
                                                          final PreferenceFragmentCompat hostOfPreferences) {
        final List<SearchablePreferencePOJOWithMap> pojoWithMapList =
                preferences
                        .stream()
                        .map(preference -> convert2POJO(preference, hostOfPreferences))
                        .collect(Collectors.toList());
        return new SearchablePreferencePOJOsWithMap(
                getSearchablePreferencePOJOs(pojoWithMapList),
                getPojoEntityMap(pojoWithMapList));
    }

    private SearchablePreferencePOJOsWithMap convertChildren2POJOs(final Preference preference,
                                                                   final PreferenceFragmentCompat hostOfPreference) {
        return preference instanceof final PreferenceGroup preferenceGroup ?
                convert2POJOs(
                        Preferences.getImmediateChildren(preferenceGroup),
                        hostOfPreference) :
                new SearchablePreferencePOJOsWithMap(
                        Collections.emptyList(),
                        ImmutableBiMap.<SearchablePreferencePOJO, Preference>builder().build());
    }

    private Optional<Either<Integer, String>> getIconResourceIdOrIconPixelData(final Preference preference,
                                                                               final PreferenceFragmentCompat hostOfPreference) {
        return iconProvider
                .getIconResourceIdOrIconDrawableOfPreference(preference, hostOfPreference)
                .map(iconResourceIdOrIconDrawable ->
                        iconResourceIdOrIconDrawable.map(
                                Function.identity(),
                                DrawableAndStringConverter::drawable2String));
    }

    private static List<SearchablePreferencePOJO> getSearchablePreferencePOJOs(final List<SearchablePreferencePOJOWithMap> pojoWithMapList) {
        return pojoWithMapList
                .stream()
                .map(SearchablePreferencePOJOWithMap::searchablePreferencePOJO)
                .collect(Collectors.toList());
    }

    private static BiMap<SearchablePreferencePOJO, Preference> getPojoEntityMap(final List<SearchablePreferencePOJOWithMap> pojoWithMapList) {
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
