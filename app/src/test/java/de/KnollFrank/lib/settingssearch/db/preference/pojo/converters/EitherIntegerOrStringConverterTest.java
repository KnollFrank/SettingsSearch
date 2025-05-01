package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_doForward_doBackward_equals_a;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;

public class EitherIntegerOrStringConverterTest {

    @Test
    public void shouldConvertFromLeft2StringAndBack() {
        shouldConvertFromEither2StringAndBack(Either.ofLeft(815));
    }

    @Test
    public void shouldConvertFromRight2StringAndBack() {
        shouldConvertFromEither2StringAndBack(Either.ofRight("some string"));
    }

    private static void shouldConvertFromEither2StringAndBack(final Either<Integer, String> either) {
        test_a_doForward_doBackward_equals_a(new EitherIntegerOrStringConverter(), either);
    }
}
