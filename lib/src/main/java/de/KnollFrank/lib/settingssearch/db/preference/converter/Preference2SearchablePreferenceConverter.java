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
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.search.provider.IconProvider;

public class Preference2SearchablePreferenceConverter {

    private final IconProvider iconProvider;
    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;
    private final IdGenerator idGenerator;

    public Preference2SearchablePreferenceConverter(
            final IconProvider iconProvider,
            final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider,
            final IdGenerator idGenerator) {
        this.iconProvider = iconProvider;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
        this.idGenerator = idGenerator;
    }

    public record SearchablePreferenceWithMap(SearchablePreference searchablePreference,
                                              BiMap<SearchablePreference, Preference> pojoEntityMap) {
    }

    public record SearchablePreferencesWithMap(
            List<SearchablePreference> searchablePreferences,
            BiMap<SearchablePreference, Preference> pojoEntityMap) {
    }

    public SearchablePreferenceWithMap convert2POJO(final Preference preference,
                                                    final PreferenceFragmentCompat hostOfPreference) {
        final int id = idGenerator.nextId();
        final SearchablePreferencesWithMap searchablePreferencesWithMap =
                convertChildren2POJOs(preference, hostOfPreference);
        final SearchablePreference searchablePreference =
                new SearchablePreference(
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
                        searchablePreferencesWithMap.searchablePreferences());
        return new SearchablePreferenceWithMap(
                searchablePreference,
                ImmutableBiMap
                        .<SearchablePreference, Preference>builder()
                        .putAll(searchablePreferencesWithMap.pojoEntityMap())
                        .put(searchablePreference, preference)
                        .buildOrThrow());
    }

    public SearchablePreferencesWithMap convert2POJOs(final List<Preference> preferences,
                                                      final PreferenceFragmentCompat hostOfPreferences) {
        final List<SearchablePreferenceWithMap> pojoWithMapList =
                preferences
                        .stream()
                        .map(preference -> convert2POJO(preference, hostOfPreferences))
                        .collect(Collectors.toList());
        return new SearchablePreferencesWithMap(
                getSearchablePreferences(pojoWithMapList),
                getPojoEntityMap(pojoWithMapList));
    }

    private SearchablePreferencesWithMap convertChildren2POJOs(final Preference preference,
                                                               final PreferenceFragmentCompat hostOfPreference) {
        return preference instanceof final PreferenceGroup preferenceGroup ?
                convert2POJOs(
                        Preferences.getImmediateChildren(preferenceGroup),
                        hostOfPreference) :
                new SearchablePreferencesWithMap(
                        Collections.emptyList(),
                        ImmutableBiMap.<SearchablePreference, Preference>builder().build());
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

    private static List<SearchablePreference> getSearchablePreferences(final List<SearchablePreferenceWithMap> pojoWithMapList) {
        return pojoWithMapList
                .stream()
                .map(SearchablePreferenceWithMap::searchablePreference)
                .collect(Collectors.toList());
    }

    private static BiMap<SearchablePreference, Preference> getPojoEntityMap(final List<SearchablePreferenceWithMap> pojoWithMapList) {
        return Maps.mergeBiMaps(
                pojoWithMapList
                        .stream()
                        .map(SearchablePreferenceWithMap::pojoEntityMap)
                        .collect(Collectors.toList()));
    }

    private static Optional<String> toString(final CharSequence charSequence) {
        return Optional
                .ofNullable(charSequence)
                .map(CharSequence::toString);
    }
}
