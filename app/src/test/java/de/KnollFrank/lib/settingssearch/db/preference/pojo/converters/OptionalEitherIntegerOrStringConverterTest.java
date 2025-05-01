package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;

import java.util.Optional;

public class OptionalEitherIntegerOrStringConverterTest {

    @Test
    public void shouldConvertFromPresentLeft2StringAndBack() {
        // Given
        final Optional<Either<Integer, String>> presentLeft = Optional.of(Either.ofLeft(4711));
        final OptionalEitherIntegerOrStringConverter converter = new OptionalEitherIntegerOrStringConverter();

        // When
        final Optional<Either<Integer, String>> presentLeftActual = converter.fromString(converter.toString(presentLeft));

        // Then
        assertThat(presentLeftActual, is(presentLeft));
    }

    @Test
    public void shouldConvertFromPresentRight2StringAndBack() {
        // Given
        final Optional<Either<Integer, String>> presentRight = Optional.of(Either.ofRight("some str"));
        final OptionalEitherIntegerOrStringConverter converter = new OptionalEitherIntegerOrStringConverter();

        // When
        final Optional<Either<Integer, String>> presentRightActual = converter.fromString(converter.toString(presentRight));

        // Then
        assertThat(presentRightActual, is(presentRight));
    }

    @Test
    public void shouldConvertFromEmpty2StringAndBack() {
        // Given
        final Optional<Either<Integer, String>> empty = Optional.empty();
        final OptionalEitherIntegerOrStringConverter converter = new OptionalEitherIntegerOrStringConverter();

        // When
        final Optional<Either<Integer, String>> emptyActual = converter.fromString(converter.toString(empty));

        // Then
        assertThat(emptyActual, is(empty));
    }
}
