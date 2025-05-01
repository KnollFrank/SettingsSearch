package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ConverterTest {

    public static <A, B> void test_a_doForward_doBackward_equals_a(
            // Given
            final A a,
            final Converter<A, B> converter) {
        // When
        final A aActual = converter.doBackward(converter.doForward(a));

        // Then
        assertThat(aActual, is(a));
    }
}
