package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;

import android.os.PersistableBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PersistableBundleConverterTest {

    @Test
    public void shouldConvertPersistableBundleAndBack() {
        // Given
        final PersistableBundleConverter converter = new PersistableBundleConverter();
        final PersistableBundle bundle = PersistableBundleTestFactory.createSomePersistableBundle();

        // When
        final PersistableBundle bundleActual = converter.doBackward(converter.doForward(bundle));

        // Then
        assertThat(bundleActual, BundleMatchers.isEqualTo(bundle));
    }
}
