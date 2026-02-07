package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.function.Function;

class ConverterTest {

    private ConverterTest() {
    }

    public static <A, B> void test_a_convertForward_convertBackward_equals_a(
            // Given
            final A a,
            final Converter<A, B> converter) {
        test_a_convertForward_convertBackward(a, converter, Matchers::is);
    }

    public static <A, B> void test_a_convertForward_convertBackward(
            // Given
            final A a,
            final Converter<A, B> converter,
            final Function<A, Matcher<? super A>> is) {
        // When
        final A aActual = converter.convertBackward(converter.convertForward(a));

        // Then
        assertThat(aActual, is.apply(a));
    }
}
