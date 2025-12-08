package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PreferenceWithScreens {

    public static List<PreferenceWithScreen> internObjects(final List<PreferenceWithScreen> preferenceWithScreens) {
        final Map<String, SearchablePreferenceScreenEntity> uniqueScreensById = getUniqueScreensById(preferenceWithScreens);
        return preferenceWithScreens
                .stream()
                .map(preferenceWithScreen ->
                             new PreferenceWithScreen(
                                     preferenceWithScreen.preference(),
                                     uniqueScreensById.get(preferenceWithScreen.screen().id())))
                .collect(Collectors.toList());
    }

    private static Map<String, SearchablePreferenceScreenEntity> getUniqueScreensById(final List<PreferenceWithScreen> preferenceWithScreens) {
        return preferenceWithScreens
                .stream()
                .map(PreferenceWithScreen::screen)
                .collect(
                        Collectors.toMap(
                                SearchablePreferenceScreenEntity::id,
                                Function.identity(),
                                (first, second) -> first));
    }
}
