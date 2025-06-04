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
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
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

    public record SearchablePreferenceWithMap(SearchablePreferenceEntity searchablePreference,
                                              BiMap<SearchablePreferenceEntity, Preference> pojoEntityMap) {
    }

    public SearchablePreferenceWithMap convertPreference(final Preference preference,
                                                         final String searchablePreferenceScreenId,
                                                         final PreferenceFragmentCompat hostOfPreference,
                                                         final Optional<Integer> parentIdOfPreference,
                                                         final Optional<SearchablePreferenceEntity> predecessorOfPreference) {
        final int id = idGenerator.nextId();
        final BiMap<SearchablePreferenceEntity, Preference> searchablePreferences =
                convertChildrenOfPreference(
                        preference,
                        searchablePreferenceScreenId,
                        hostOfPreference,
                        Optional.of(id),
                        predecessorOfPreference);
        final SearchablePreferenceEntity searchablePreference =
                new SearchablePreferenceEntity(
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
                        parentIdOfPreference,
                        predecessorOfPreference.map(SearchablePreferenceEntity::getId),
                        searchablePreferenceScreenId);
        return new SearchablePreferenceWithMap(
                searchablePreference,
                ImmutableBiMap
                        .<SearchablePreferenceEntity, Preference>builder()
                        .putAll(searchablePreferences)
                        .put(searchablePreference, preference)
                        .buildOrThrow());
    }

    public BiMap<SearchablePreferenceEntity, Preference> convertPreferences(final List<Preference> preferences,
                                                                            final String searchablePreferenceScreenId,
                                                                            final PreferenceFragmentCompat hostOfPreferences,
                                                                            final Optional<Integer> parentIdOfPreferences,
                                                                            final Optional<SearchablePreferenceEntity> predecessorOfPreferences) {
        final List<SearchablePreferenceWithMap> pojoWithMapList =
                preferences
                        .stream()
                        .map(preference -> convertPreference(preference, searchablePreferenceScreenId, hostOfPreferences, parentIdOfPreferences, predecessorOfPreferences))
                        .collect(Collectors.toList());
        return getPojoEntityMap(pojoWithMapList);
    }

    private BiMap<SearchablePreferenceEntity, Preference> convertChildrenOfPreference(final Preference preference,
                                                                                      final String searchablePreferenceScreenId,
                                                                                      final PreferenceFragmentCompat hostOfPreference,
                                                                                      final Optional<Integer> idOfPreference,
                                                                                      final Optional<SearchablePreferenceEntity> predecessorOfPreference) {
        return preference instanceof final PreferenceGroup preferenceGroup ?
                convertPreferences(
                        Preferences.getImmediateChildren(preferenceGroup),
                        searchablePreferenceScreenId,
                        hostOfPreference,
                        idOfPreference,
                        predecessorOfPreference) :
                ImmutableBiMap.<SearchablePreferenceEntity, Preference>builder().build();
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

    private static BiMap<SearchablePreferenceEntity, Preference> getPojoEntityMap(final List<SearchablePreferenceWithMap> pojoWithMapList) {
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
