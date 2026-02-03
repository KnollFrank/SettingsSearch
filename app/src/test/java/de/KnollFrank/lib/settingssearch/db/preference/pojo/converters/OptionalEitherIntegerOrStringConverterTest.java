package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_convertForward_convertBackward_equals_a;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;

import java.util.Optional;

public class OptionalEitherIntegerOrStringConverterTest {

    private final OptionalEitherIntegerOrStringConverter converter = new OptionalEitherIntegerOrStringConverter();

    @Test
    public void shouldConvertFromPresentLeftToStringAndBack() {
        test_a_convertForward_convertBackward_equals_a(Optional.of(Either.ofLeft(4711)), converter);
    }

    @Test
    public void shouldConvertFromPresentRightToStringAndBack() {
        test_a_convertForward_convertBackward_equals_a(Optional.of(Either.ofRight("some str")), converter);
    }

    @Test
    public void shouldConvertFromEmptyToStringAndBack() {
        test_a_convertForward_convertBackward_equals_a(Optional.empty(), converter);
    }
}
