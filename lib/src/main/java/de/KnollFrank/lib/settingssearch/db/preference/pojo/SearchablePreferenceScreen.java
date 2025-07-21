package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public record SearchablePreferenceScreen(String id,
                                         Class<? extends PreferenceFragmentCompat> host,
                                         Optional<String> title,
                                         Optional<String> summary,
                                         Set<SearchablePreference> allPreferences) {

    public SearchablePreferenceScreen {
        allPreferences.forEach(preference -> preference.setHost(this));
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
}
