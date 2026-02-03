package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_convertForward_convertBackward_equals_a;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TestTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TestTreeTransformer;
import de.KnollFrank.settingssearch.Configuration;

public class TreeProcessorClassConverterTest {

    private final TreeProcessorClassConverter converter = new TreeProcessorClassConverter();

    @Test
    public void shouldConvertFromLeftToStringAndBack() {
        final SearchablePreferenceScreenTreeCreator<Configuration> testTreeCreator = new TestTreeCreator<>();
        test_a_convertForward_convertBackward_equals_a(
                Either.ofLeft((Class<? extends SearchablePreferenceScreenTreeCreator<Configuration>>) testTreeCreator.getClass()),
                converter);
    }

    @Test
    public void shouldConvertFromRightToStringAndBack() {
        final SearchablePreferenceScreenTreeTransformer<Configuration> testTreeTransformer = new TestTreeTransformer<>();
        test_a_convertForward_convertBackward_equals_a(
                Either.ofRight((Class<? extends SearchablePreferenceScreenTreeTransformer<Configuration>>) testTreeTransformer.getClass()),
                converter);
    }
}
