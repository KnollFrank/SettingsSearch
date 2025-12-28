package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import com.codepoetics.ambivalence.Either;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.KnollFrank.lib.settingssearch.common.Intents;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.common.converter.BundleConverter;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.search.provider.IconProvider;

public class PreferenceToSearchablePreferenceConverter {

    private final IconProvider iconProvider;
    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;

    public PreferenceToSearchablePreferenceConverter(final IconProvider iconProvider,
                                                     final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider) {
        this.iconProvider = iconProvider;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
    }

    public record SearchablePreferenceWithMap(SearchablePreference searchablePreference,
                                              BiMap<SearchablePreference, Preference> pojoEntityMap) {
    }

    public SearchablePreferenceWithMap convertPreference(
            final Preference preference,
            final List<Integer> indexPathOfPreference,
            final String searchablePreferenceScreenId,
            final PreferenceFragmentCompat hostOfPreference,
            final Optional<SearchablePreference> predecessorOfPreference,
            final Locale locale) {
        final BiMap<SearchablePreference, Preference> searchablePreferences =
                convertChildrenOfPreference(
                        preference,
                        indexPathOfPreference,
                        searchablePreferenceScreenId,
                        hostOfPreference,
                        predecessorOfPreference,
                        locale);
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        join(searchablePreferenceScreenId, indexPathOfPreference),
                        preference.getKey(),
                        Strings.toString(Optional.ofNullable(preference.getTitle())),
                        Strings.toString(Optional.ofNullable(preference.getSummary())),
                        getIconResourceIdOrIconPixelData(preference, hostOfPreference),
                        preference.getLayoutResource(),
                        preference.getWidgetLayoutResource(),
                        Optional.ofNullable(preference.getFragment()),
                        getClassNameOfReferencedActivity(preference),
                        preference.isVisible(),
                        BundleConverter.toPersistableBundle(preference.getExtras()),
                        searchableInfoAndDialogInfoProvider.getSearchableInfo(preference, hostOfPreference),
                        searchablePreferences.keySet(),
                        predecessorOfPreference);
        return new SearchablePreferenceWithMap(
                searchablePreference,
                ImmutableBiMap
                        .<SearchablePreference, Preference>builder()
                        .putAll(searchablePreferences)
                        .put(searchablePreference, preference)
                        .buildOrThrow());
    }

    public BiMap<SearchablePreference, Preference> convertPreferences(
            final List<Preference> preferences,
            final List<Integer> indexPathOfParentOfPreferences,
            final String searchablePreferenceScreenId,
            final PreferenceFragmentCompat hostOfPreferences,
            final Optional<SearchablePreference> predecessorOfPreferences,
            final Locale locale) {
        final List<SearchablePreferenceWithMap> pojoWithMapList =
                IntStream
                        .range(0, preferences.size())
                        .mapToObj(
                                index ->
                                        convertPreference(
                                                preferences.get(index),
                                                append(indexPathOfParentOfPreferences, index),
                                                searchablePreferenceScreenId,
                                                hostOfPreferences,
                                                predecessorOfPreferences,
                                                locale))
                        .collect(Collectors.toList());
        return getPojoEntityMap(pojoWithMapList);
    }

    private BiMap<SearchablePreference, Preference> convertChildrenOfPreference(
            final Preference preference,
            final List<Integer> indexPathOfPreference,
            final String searchablePreferenceScreenId,
            final PreferenceFragmentCompat hostOfPreference,
            final Optional<SearchablePreference> predecessorOfPreference,
            final Locale locale) {
        return preference instanceof final PreferenceGroup preferenceGroup ?
                convertPreferences(
                        Preferences.getImmediateChildren(preferenceGroup),
                        indexPathOfPreference,
                        searchablePreferenceScreenId,
                        hostOfPreference,
                        predecessorOfPreference,
                        locale) :
                ImmutableBiMap.of();
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
                .flatMap(Intents::getClassName);
    }

    private static String join(final String prefix, final List<Integer> ints) {
        return prefix + "-" + join(ints);
    }

    private static String join(final List<Integer> ints) {
        return ints
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
    }

    private static List<Integer> append(final List<Integer> integers, final int integer) {
        return ImmutableList
                .<Integer>builder()
                .addAll(integers)
                .add(integer)
                .build();
    }
}
