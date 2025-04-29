package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;

public class EitherIntegerOrStringConverterTest {

    @Test
    public void shouldConvertFromLeft2StringAndBack() {
        // Given
        shouldConvertFromEither2StringAndBack(Either.ofLeft(815));
    }

    @Test
    public void shouldConvertFromRight2StringAndBack() {
        // Given
        shouldConvertFromEither2StringAndBack(Either.ofRight("some string"));
    }

    private static void shouldConvertFromEither2StringAndBack(final Either<Integer, String> either) {
        // Given
        final var converter = new EitherIntegerOrStringConverter();

        // When
        final Either<Integer, String> eitherActual = converter.fromString(converter.toString(either));

        // Then
        assertThat(eitherActual, is(either));
    }
}
