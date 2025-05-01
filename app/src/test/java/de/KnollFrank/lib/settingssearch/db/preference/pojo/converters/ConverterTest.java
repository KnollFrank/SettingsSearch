package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ConverterTest {

    public static <A, B> void test_a_doForward_doBackward_equals_a(
            // Given
            final Converter<A, B> converter,
            final A a) {
        // When
        final A aActual = converter.doBackward(converter.doForward(a));

        // Then
        assertThat(aActual, is(a));
    }
}
