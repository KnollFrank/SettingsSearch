package de.KnollFrank.lib.settingssearch.fragment;

import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public final class PreferencePathPointer {

    private final PreferencePath preferencePath;
    private final int indexWithinPreferencePath;

    public PreferencePathPointer(final PreferencePath preferencePath, final int indexWithinPreferencePath) {
        this.preferencePath = preferencePath;
        this.indexWithinPreferencePath = indexWithinPreferencePath;
        if (_dereference().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public SearchablePreference dereference() {
        return _dereference().orElseThrow();
    }

    public Optional<PreferencePathPointer> next() {
        return getElementAtIndex(indexWithinPreferencePath + 1).isPresent() ?
                Optional.of(
                        new PreferencePathPointer(
                                preferencePath,
                                indexWithinPreferencePath + 1)) :
                Optional.empty();
    }

    public PreferencePath preferencePath() {
        return preferencePath;
    }

    public int indexWithinPreferencePath() {
        return indexWithinPreferencePath;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PreferencePathPointer) obj;
        return Objects.equals(this.preferencePath, that.preferencePath) &&
                this.indexWithinPreferencePath == that.indexWithinPreferencePath;
    }

    @Override
    public int hashCode() {
        return Objects.hash(preferencePath, indexWithinPreferencePath);
    }

    @Override
    public String toString() {
        return "PreferencePathPointer[" +
                "preferencePath=" + preferencePath + ", " +
                "indexWithinPreferencePath=" + indexWithinPreferencePath + ']';
    }

    private Optional<SearchablePreference> _dereference() {
        return getElementAtIndex(indexWithinPreferencePath);
    }

    private Optional<SearchablePreference> getElementAtIndex(final int indexWithinPreferencePath) {
        return Lists.getElementAtIndex(
                preferencePath.preferences(),
                indexWithinPreferencePath);
    }
}
