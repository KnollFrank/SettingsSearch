package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;

import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

public class PreferenceFragmentClassConverterTest {

    @Test
    public void shouldConvertFromClass2StringAndBack() {
        // Given
        final PreferenceFragmentClassConverter converter = new PreferenceFragmentClassConverter();
        final Class<PrefsFragmentFirst> clazz = PrefsFragmentFirst.class;

        // When
        final Class<? extends PreferenceFragmentCompat> clazzActual = converter.fromString(converter.toString(clazz));

        // Then
        assertThat(clazzActual == clazz, is(true));
    }
}
