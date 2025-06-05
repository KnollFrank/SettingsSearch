package de.KnollFrank.lib.settingssearch.fragment.navigation;

import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEntityPath;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

// FK-TODO: remove PreferencePathNavigator and replace with adapted PreferencePath
public class PreferencePathPointer {

    public final PreferenceEntityPath preferencePath;
    public final int indexWithinPreferencePath;

    public static PreferencePathPointer of(final PreferenceEntityPath preferencePath,
                                           final int indexWithinPreferencePath) {
        return PreferencePathPointer
                .tryCreatePreferencePathPointer(preferencePath, indexWithinPreferencePath)
                .orElseThrow(IllegalArgumentException::new);
    }

    private PreferencePathPointer(final PreferenceEntityPath preferencePath,
                                  final int indexWithinPreferencePath) {
        this.preferencePath = preferencePath;
        this.indexWithinPreferencePath = indexWithinPreferencePath;
    }

    public SearchablePreferenceEntity dereference() {
        return Lists
                .getElementAtIndex(preferencePath.preferences(), indexWithinPreferencePath)
                .orElseThrow();
    }

    public Optional<PreferencePathPointer> next() {
        return tryCreatePreferencePathPointer(preferencePath, indexWithinPreferencePath + 1);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PreferencePathPointer that = (PreferencePathPointer) o;
        return indexWithinPreferencePath == that.indexWithinPreferencePath && Objects.equals(preferencePath, that.preferencePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preferencePath, indexWithinPreferencePath);
    }

    @Override
    public String toString() {
        return "PreferencePathPointer{" +
                "preferencePath=" + preferencePath +
                ", indexWithinPreferencePath=" + indexWithinPreferencePath +
                '}';
    }

    private static Optional<PreferencePathPointer> tryCreatePreferencePathPointer(
            final PreferenceEntityPath preferencePath,
            final int indexWithinPreferencePath) {
        return Lists.getElementAtIndex(preferencePath.preferences(), indexWithinPreferencePath).isPresent() ?
                Optional.of(new PreferencePathPointer(preferencePath, indexWithinPreferencePath)) :
                Optional.empty();
    }
}
