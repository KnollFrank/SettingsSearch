package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_convertForward_convertBackward_equals_a;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;

public class EitherIntegerOrStringConverterTest {

    private final EitherIntegerOrStringConverter converter = new EitherIntegerOrStringConverter();

    @Test
    public void shouldConvertFromLeftToStringAndBack() {
        test_a_convertForward_convertBackward_equals_a(Either.ofLeft(815), converter);
    }

    @Test
    public void shouldConvertFromRightToStringAndBack() {
        test_a_convertForward_convertBackward_equals_a(Either.ofRight("some string"), converter);
    }
}
