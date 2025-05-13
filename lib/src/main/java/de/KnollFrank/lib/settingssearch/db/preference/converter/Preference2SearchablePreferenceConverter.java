package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import com.codepoetics.ambivalence.Either;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Intents;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.search.provider.IconProvider;

public class Preference2SearchablePreferenceConverter {

    private final IconProvider iconProvider;
    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;
    private final IdGenerator idGenerator;

    public Preference2SearchablePreferenceConverter(final IconProvider iconProvider,
                                                    final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider,
                                                    final IdGenerator idGenerator) {
        this.iconProvider = iconProvider;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
        this.idGenerator = idGenerator;
    }

    public record SearchablePreferenceWithMap(SearchablePreference searchablePreference,
                                              BiMap<SearchablePreference, Preference> pojoEntityMap) {
    }

    public SearchablePreferenceWithMap convert2POJO(final Preference preference,
                                                    final int searchablePreferenceScreenId,
                                                    final PreferenceFragmentCompat hostOfPreference,
                                                    final Optional<Integer> parentIdOfPreference,
                                                    final Optional<SearchablePreference> predecessorOfPreference) {
        final int id = idGenerator.nextId();
        final BiMap<SearchablePreference, Preference> searchablePreferences =
                convertChildren2POJOs(
                        preference,
                        searchablePreferenceScreenId,
                        hostOfPreference,
                        Optional.of(id),
                        predecessorOfPreference);
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        id,
                        preference.getKey(),
                        Strings.toString(Optional.ofNullable(preference.getTitle())),
                        Strings.toString(Optional.ofNullable(preference.getSummary())),
                        getIconResourceIdOrIconPixelData(preference, hostOfPreference),
                        preference.getLayoutResource(),
                        preference.getWidgetLayoutResource(),
                        Optional.ofNullable(preference.getFragment()),
                        getClassNameOfReferencedActivity(preference),
                        preference.isVisible(),
                        searchableInfoAndDialogInfoProvider.getSearchableInfo(preference, hostOfPreference),
                        preference.getExtras(),
                        parentIdOfPreference,
                        predecessorOfPreference.map(SearchablePreference::getId),
                        searchablePreferenceScreenId);
        return new SearchablePreferenceWithMap(
                searchablePreference,
                ImmutableBiMap
                        .<SearchablePreference, Preference>builder()
                        .putAll(searchablePreferences)
                        .put(searchablePreference, preference)
                        .buildOrThrow());
    }

    public BiMap<SearchablePreference, Preference> convert2POJOs(final List<Preference> preferences,
                                                                 final int searchablePreferenceScreenId,
                                                                 final PreferenceFragmentCompat hostOfPreferences,
                                                                 final Optional<Integer> parentIdOfPreferences,
                                                                 final Optional<SearchablePreference> predecessorOfPreferences) {
        final List<SearchablePreferenceWithMap> pojoWithMapList =
                preferences
                        .stream()
                        .map(preference -> convert2POJO(preference, searchablePreferenceScreenId, hostOfPreferences, parentIdOfPreferences, predecessorOfPreferences))
                        .collect(Collectors.toList());
        return getPojoEntityMap(pojoWithMapList);
    }

    private BiMap<SearchablePreference, Preference> convertChildren2POJOs(final Preference preference,
                                                                          final int searchablePreferenceScreenId,
                                                                          final PreferenceFragmentCompat hostOfPreference,
                                                                          final Optional<Integer> idOfPreference,
                                                                          final Optional<SearchablePreference> predecessorOfPreference) {
        return preference instanceof final PreferenceGroup preferenceGroup ?
                convert2POJOs(
                        Preferences.getImmediateChildren(preferenceGroup),
                        searchablePreferenceScreenId,
                        hostOfPreference,
                        idOfPreference,
                        predecessorOfPreference) :
                ImmutableBiMap.<SearchablePreference, Preference>builder().build();
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

    private static BiMap<SearchablePreference, Preference> getPojoEntityMap(final List<SearchablePreferenceWithMap> pojoWithMapList) {
        return Maps.mergeBiMaps(
                pojoWithMapList
                        .stream()
                        .map(SearchablePreferenceWithMap::pojoEntityMap)
                        .collect(Collectors.toList()));
    }

    private static Optional<String> getClassNameOfReferencedActivity(final Preference preference) {
        return Optional
                .ofNullable(preference.getIntent())
                .map(Intents::getClassName);
    }
}
