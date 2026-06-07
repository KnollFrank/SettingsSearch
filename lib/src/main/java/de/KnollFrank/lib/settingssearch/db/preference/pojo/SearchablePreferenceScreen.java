package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableSet;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.common.Sets;

public record SearchablePreferenceScreen(String id,
                                         FragmentClassOfActivity<? extends PreferenceFragmentCompat> host,
                                         Optional<String> title,
                                         Optional<String> summary,
                                         Set<SearchablePreference> immediatePreferences) {

    public Set<SearchablePreference> allPreferencesOfPreferenceHierarchy() {
        return getRecursivePreferences(immediatePreferences);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        final SearchablePreferenceScreen that = (SearchablePreferenceScreen) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private static Set<SearchablePreference> getRecursivePreferences(final Set<SearchablePreference> searchablePreference) {
        return Sets.union(
                searchablePreference
                        .stream()
                        .map(SearchablePreferenceScreen::getRecursivePreferences)
                        .collect(Collectors.toSet()));
    }

    // FK-TODO: make instance method of SearchablePreference or even better return Tree rooted at searchablePreference
    private static Set<SearchablePreference> getRecursivePreferences(final SearchablePreference searchablePreference) {
        return ImmutableSet
                .<SearchablePreference>builder()
                .add(searchablePreference)
                .addAll(getRecursivePreferences(searchablePreference.getImmediateChildren()))
                .build();
    }
}
