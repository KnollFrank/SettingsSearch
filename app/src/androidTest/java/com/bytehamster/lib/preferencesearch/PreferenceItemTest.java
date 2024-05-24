package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Parcel;

import com.bytehamster.lib.preferencesearch.PreferenceParserTest.PrefsFragment;

import org.junit.Test;

public class PreferenceItemTest {

    @Test
    public void testPreferenceItemParcelable() {
        // Given
        final PreferenceItem preferenceItem =
                new PreferenceItem(
                        "title",
                        "summary",
                        "key",
                        "breadcrumbs",
                        "keywords",
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
