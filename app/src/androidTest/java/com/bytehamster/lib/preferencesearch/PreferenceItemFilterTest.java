package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PreferenceItemFilterTest {

    @Test
    public void shouldGetSearchablePreferences() {
        // Given
        final SearchPreference nonSearchablePreference = new SearchPreference(TestUtils.getContext());
        final CheckBoxPreference searchablePreference = new CheckBoxPreference(TestUtils.getContext());
        final List<Preference> preferences = Arrays.asList(nonSearchablePreference, searchablePreference);

        // When
        final List<Preference> searchablePreferences = PreferenceItemFilter.getSearchablePreferences(preferences);

        // Then
        assertThat(searchablePreferences, contains(searchablePreference));
    }
}