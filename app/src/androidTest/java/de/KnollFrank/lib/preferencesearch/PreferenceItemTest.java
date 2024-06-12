package de.KnollFrank.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Parcel;

import org.junit.Test;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.search.PreferenceSearcherTest.PrefsFragment;

public class PreferenceItemTest {

    @Test
    public void testPreferenceItemParcelable() {
        // Given
        final PreferenceItem preferenceItem =
                new PreferenceItem(
                        Optional.of("title"),
                        Optional.of("summary"),
                        Optional.of("key"),
                        Optional.of("breadcrumbs"),
                        Optional.of("keywords"),
                        Optional.of("entries"),
                        PrefsFragment.class);

        // Obtain a Parcel object and write the parcelable object to it:
        final Parcel parcel = Parcel.obtain();
        preferenceItem.writeToParcel(parcel, 0);

        // After you're done with writing, you need to reset the parcel for reading:
        parcel.setDataPosition(0);

        // Reconstruct object from parcel and asserts:
        final PreferenceItem createdFromParcel = PreferenceItem.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel, is(preferenceItem));
    }
}
