package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;

import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

public class PreferenceFragmentClassConverterTest {

    // FK-TODO: use ConverterTest?
    @Test
    public void shouldConvertFromClass2StringAndBack() {
        // Given
        final Converter<Class<? extends PreferenceFragmentCompat>, String> converter = new PreferenceFragmentClassConverter();
        final Class<? extends PreferenceFragmentCompat> clazz = PrefsFragmentFirst.class;

        // When
        final Class<? extends PreferenceFragmentCompat> clazzActual = converter.doBackward(converter.doForward(clazz));

        // Then
        assertThat(clazzActual == clazz, is(true));
    }
}
