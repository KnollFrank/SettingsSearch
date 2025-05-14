package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_doForward_doBackward_equals_a;

import org.junit.Test;

import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

public class PreferenceFragmentClassConverterTest {

    @Test
    public void shouldConvertFromClass2StringAndBack() {
        test_a_doForward_doBackward_equals_a(PrefsFragmentFirst.class, new PreferenceFragmentClassConverter());
    }
}
