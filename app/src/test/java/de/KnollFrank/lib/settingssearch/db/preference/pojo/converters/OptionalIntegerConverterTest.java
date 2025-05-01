package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import java.util.Optional;

public class OptionalIntegerConverterTest {

    @Test
    public void shouldConvertFromPresentInteger2StringAndBack() {
        shouldConvertFromOptionalInteger2StringAndBack(Optional.of(4711));
    }

    @Test
    public void shouldConvertFromEmptyInteger2StringAndBack() {
        shouldConvertFromOptionalInteger2StringAndBack(Optional.empty());
    }

    private static void shouldConvertFromOptionalInteger2StringAndBack(final Optional<Integer> optionalInteger) {
        // Given
        final OptionalIntegerConverter converter = new OptionalIntegerConverter();

        // When
        final Optional<Integer> optionalIntegerActual = converter.fromString(converter.toString(optionalInteger));

        // Then
        assertThat(optionalIntegerActual, is(optionalInteger));
    }
}
