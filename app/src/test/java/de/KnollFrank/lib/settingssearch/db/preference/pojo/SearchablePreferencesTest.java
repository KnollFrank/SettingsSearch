package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreference;

import org.junit.Test;

import java.util.Optional;
import java.util.Set;

public class SearchablePreferencesTest {

    @Test
    public void shouldFindPreferenceByKey() {
        // Given
        final String key = "some key";
        final SearchablePreferenceEntity preference = createSearchablePreference(key);

        // When
        final Optional<SearchablePreferenceEntity> preferenceActual =
                SearchablePreferences.findPreferenceByKey(
                        Set.of(preference),
                        key);

        // Then
        assertThat(preferenceActual, is(Optional.of(preference)));
    }

    @Test
    public void shouldNotFindPreferenceByKey() {
        // Given
        final String nonExistingKey = "nonExistingKey";

        // When
        final Optional<SearchablePreferenceEntity> preferenceActual =
                SearchablePreferences.findPreferenceByKey(
                        Set.of(),
                        nonExistingKey);

        // Then
        assertThat(preferenceActual, is(Optional.empty()));
    }
}
