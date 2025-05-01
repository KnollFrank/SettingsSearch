package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import java.util.Optional;

// FK-TODO: DRY with OptionalIntegerConverterTest
public class OptionalStringConverterTest {

    @Test
    public void shouldConvertFromPresentString2StringAndBack() {
        shouldConvertFromOptionalString2StringAndBack(Optional.of("some string"));
    }

    @Test
    public void shouldConvertFromEmptyString2StringAndBack() {
        shouldConvertFromOptionalString2StringAndBack(Optional.empty());
    }

    private static void shouldConvertFromOptionalString2StringAndBack(final Optional<String> optionalString) {
        // Given
        final OptionalStringConverter converter = new OptionalStringConverter();

        // When
        final Optional<String> optionalStringActual = converter.fromString(converter.toString(optionalString));

        // Then
        assertThat(optionalStringActual, is(optionalString));
    }
}
