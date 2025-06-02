package de.KnollFrank.lib.settingssearch.fragment.navigation;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import de.KnollFrank.lib.settingssearch.PreferenceEntityPath;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

// FK-TODO: remove PreferencePathNavigator and replace with adapted PreferencePath
public class PreferencePathPointer {

    public final PreferenceEntityPath preferenceEntityPath;
    public final int indexWithinPreferencePath;

    public static PreferencePathPointer of(final PreferenceEntityPath preferenceEntityPath,
                                           final int indexWithinPreferencePath) {
        return PreferencePathPointer
                .tryCreatePreferencePathPointer(preferenceEntityPath, indexWithinPreferencePath)
                .orElseThrow(IllegalArgumentException::new);
    }

    private PreferencePathPointer(final PreferenceEntityPath preferenceEntityPath,
                                  final int indexWithinPreferencePath) {
        this.preferenceEntityPath = preferenceEntityPath;
        this.indexWithinPreferencePath = indexWithinPreferencePath;
    }

    public SearchablePreferenceEntity dereference() {
        return Lists
                .getElementAtIndex(preferenceEntityPath.preferences(), indexWithinPreferencePath)
                .orElseThrow();
    }

    public Optional<PreferencePathPointer> next() {
        return tryCreatePreferencePathPointer(preferenceEntityPath, indexWithinPreferencePath + 1);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PreferencePathPointer that = (PreferencePathPointer) o;
        return indexWithinPreferencePath == that.indexWithinPreferencePath && Objects.equals(preferenceEntityPath, that.preferenceEntityPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preferenceEntityPath, indexWithinPreferencePath);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PreferencePathPointer.class.getSimpleName() + "[", "]")
                .add("preferenceEntityPath=" + preferenceEntityPath)
                .add("indexWithinPreferencePath=" + indexWithinPreferencePath)
                .toString();
    }

    private static Optional<PreferencePathPointer> tryCreatePreferencePathPointer(
            final PreferenceEntityPath preferenceEntityPath,
            final int indexWithinPreferencePath) {
        return Lists.getElementAtIndex(preferenceEntityPath.preferences(), indexWithinPreferencePath).isPresent() ?
                Optional.of(new PreferencePathPointer(preferenceEntityPath, indexWithinPreferencePath)) :
                Optional.empty();
    }
}
