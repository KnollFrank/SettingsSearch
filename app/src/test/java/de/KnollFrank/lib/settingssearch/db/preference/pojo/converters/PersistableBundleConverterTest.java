package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;

import android.os.PersistableBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.matcher.BundleMatchers;

@RunWith(RobolectricTestRunner.class)
public class PersistableBundleConverterTest {

    @Test
    public void shouldConvertPersistableBundleAndBack() {
        // FK-TODO: use test_a_convertForward_convertBackward_equals_a
        // Given
        final PersistableBundleConverter converter = new PersistableBundleConverter();
        final PersistableBundle bundle = PersistableBundleTestFactory.createSomePersistableBundle();

        // When
        final PersistableBundle bundleActual = converter.convertBackward(converter.convertForward(bundle));

        // Then
        assertThat(bundleActual, BundleMatchers.isEqualTo(bundle));
    }
}
