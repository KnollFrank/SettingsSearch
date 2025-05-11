package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static de.KnollFrank.lib.settingssearch.db.preference.dao.BundleTestFactory.createBundle;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.BundleTestFactory.createSomeBundleOfBundles;

import android.os.Bundle;

import com.google.common.testing.EqualsTester;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BundleWithEqualityTest {

    @Test
    public void testEquals() {
        new EqualsTester()
                .addEqualityGroup(
                        new BundleWithEquality(new Bundle()),
                        new BundleWithEquality(new Bundle()))
                .addEqualityGroup(
                        new BundleWithEquality(createBundle("someKey", "someValue")),
                        new BundleWithEquality(createBundle("someKey", "someValue")))
                .addEqualityGroup(
                        new BundleWithEquality(createSomeBundleOfBundles()),
                        new BundleWithEquality(createSomeBundleOfBundles()))
                .testEquals();
    }
}