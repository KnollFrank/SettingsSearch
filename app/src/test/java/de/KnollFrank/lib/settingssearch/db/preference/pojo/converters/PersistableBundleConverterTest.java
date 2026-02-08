package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_convertForward_convertBackward;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.matcher.BundleMatchers;

@RunWith(RobolectricTestRunner.class)
public class PersistableBundleConverterTest {

    @Test
    public void shouldConvertPersistableBundleAndBack() {
        test_a_convertForward_convertBackward(
                PersistableBundleTestFactory.createSomePersistableBundle(),
                new PersistableBundleConverter(),
                BundleMatchers::isEqualTo);
    }
}
