package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_doForward_doBackward_equals_a;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;

import java.util.Optional;

public class OptionalEitherIntegerOrStringConverterTest {

    private final OptionalEitherIntegerOrStringConverter converter = new OptionalEitherIntegerOrStringConverter();

    @Test
    public void shouldConvertFromPresentLeft2StringAndBack() {
        test_a_doForward_doBackward_equals_a(converter, Optional.of(Either.ofLeft(4711)));
    }

    @Test
    public void shouldConvertFromPresentRight2StringAndBack() {
        test_a_doForward_doBackward_equals_a(converter, Optional.of(Either.ofRight("some str")));
    }

    @Test
    public void shouldConvertFromEmpty2StringAndBack() {
        test_a_doForward_doBackward_equals_a(converter, Optional.empty());
    }
}
