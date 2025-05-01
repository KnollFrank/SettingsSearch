package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;

import java.util.Optional;

public class OptionalEitherIntegerOrStringConverterTest {

    @Test
    public void shouldConvertFromPresentLeft2StringAndBack() {
        shouldConvertOptionalEitherIntegerOrString(Optional.of(Either.ofLeft(4711)));
    }

    @Test
    public void shouldConvertFromPresentRight2StringAndBack() {
        shouldConvertOptionalEitherIntegerOrString(Optional.of(Either.ofRight("some str")));
    }

    @Test
    public void shouldConvertFromEmpty2StringAndBack() {
        shouldConvertOptionalEitherIntegerOrString(Optional.empty());
    }

    private static void shouldConvertOptionalEitherIntegerOrString(final Optional<Either<Integer, String>> value) {
        // Given
        final Converter<Optional<Either<Integer, String>>, String> converter = new OptionalEitherIntegerOrStringConverter();

        // When
        final Optional<Either<Integer, String>> valueActual = converter.doBackward(converter.doForward(value));

        // Then
        assertThat(valueActual, is(value));
    }
}
