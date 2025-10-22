package de.KnollFrank.lib.settingssearch.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.PersistableBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PersistableBundleEqualityTest {

    @Test
    public void shouldReturnTrueForTwoNullBundles() {
        assertThat(PersistableBundleEquality.areBundlesEqual(null, null), is(true));
    }

    @Test
    public void shouldReturnFalseIfOneBundleIsNull() {
        assertThat(PersistableBundleEquality.areBundlesEqual(new PersistableBundle(), null), is(false));
        assertThat(PersistableBundleEquality.areBundlesEqual(null, new PersistableBundle()), is(false));
    }

    @Test
    public void shouldReturnTrueForTwoEmptyBundles() {
        assertThat(PersistableBundleEquality.areBundlesEqual(new PersistableBundle(), new PersistableBundle()), is(true));
    }

    @Test
    public void shouldReturnFalseForBundlesOfDifferentSize() {
        // Given
        final PersistableBundle one = new PersistableBundle();
        one.putString("key1", "value1");

        final PersistableBundle two = new PersistableBundle();
        one.putString("key1", "value1");
        one.putString("key2", "value2");

        // Then
        assertThat(PersistableBundleEquality.areBundlesEqual(one, two), is(false));
    }

    @Test
    public void shouldReturnFalseForBundlesWithDifferentKeys() {
        // Given
        final PersistableBundle one = new PersistableBundle();
        one.putString("key1", "value");

        final PersistableBundle two = new PersistableBundle();
        two.putString("key_different", "value");

        // Then
        assertThat(PersistableBundleEquality.areBundlesEqual(one, two), is(false));
    }

    @Test
    public void shouldReturnTrueForEqualBundlesWithSimpleTypes() {
        // Given
        final PersistableBundle one = new PersistableBundle();
        one.putString("string", "hello");
        one.putInt("int", 123);
        one.putLong("long", 456L);
        one.putDouble("double", 78.9);
        one.putBoolean("boolean", true);

        final PersistableBundle two = new PersistableBundle();
        two.putString("string", "hello");
        two.putInt("int", 123);
        two.putLong("long", 456L);
        two.putDouble("double", 78.9);
        two.putBoolean("boolean", true);

        // Then
        assertThat(PersistableBundleEquality.areBundlesEqual(one, two), is(true));
    }

    @Test
    public void shouldReturnFalseForBundlesWithDifferentValues() {
        // Given
        final PersistableBundle one = new PersistableBundle();
        one.putString("string", "hello");

        final PersistableBundle two = new PersistableBundle();
        two.putString("string", "world");

        // Then
        assertThat(PersistableBundleEquality.areBundlesEqual(one, two), is(false));
    }

    @Test
    public void shouldReturnTrueForEqualBundlesWithArrays() {
        // Given
        final PersistableBundle one = new PersistableBundle();
        one.putStringArray("strings", new String[]{"a", "b"});
        one.putIntArray("ints", new int[]{1, 2});
        one.putLongArray("longs", new long[]{3L, 4L});
        one.putDoubleArray("doubles", new double[]{5.5, 6.6});
        one.putBooleanArray("booleans", new boolean[]{true, false});

        final PersistableBundle two = new PersistableBundle();
        two.putStringArray("strings", new String[]{"a", "b"});
        two.putIntArray("ints", new int[]{1, 2});
        two.putLongArray("longs", new long[]{3L, 4L});
        two.putDoubleArray("doubles", new double[]{5.5, 6.6});
        two.putBooleanArray("booleans", new boolean[]{true, false});

        // Then
        assertThat(PersistableBundleEquality.areBundlesEqual(one, two), is(true));
    }

    @Test
    public void shouldReturnFalseForBundlesWithDifferentArrayValues() {
        // Given
        final PersistableBundle one = new PersistableBundle();
        one.putStringArray("strings", new String[]{"a", "b"});

        final PersistableBundle two = new PersistableBundle();
        two.putStringArray("strings", new String[]{"a", "c"}); // different value

        // Then
        assertThat(PersistableBundleEquality.areBundlesEqual(one, two), is(false));
    }

    @Test
    public void shouldReturnTrueForEqualNestedBundles() {
        // Given
        final PersistableBundle one = new PersistableBundle();
        final PersistableBundle nestedOne = new PersistableBundle();
        nestedOne.putString("nestedKey", "nestedValue");
        one.putPersistableBundle("nestedBundle", nestedOne);

        final PersistableBundle two = new PersistableBundle();
        final PersistableBundle nestedTwo = new PersistableBundle();
        nestedTwo.putString("nestedKey", "nestedValue");
        two.putPersistableBundle("nestedBundle", nestedTwo);

        // Then
        assertThat(PersistableBundleEquality.areBundlesEqual(one, two), is(true));
    }

    @Test
    public void shouldReturnFalseForDifferentNestedBundles() {
        // Given
        final PersistableBundle one = new PersistableBundle();
        final PersistableBundle nestedOne = new PersistableBundle();
        nestedOne.putString("nestedKey", "nestedValue1");
        one.putPersistableBundle("nestedBundle", nestedOne);

        final PersistableBundle two = new PersistableBundle();
        final PersistableBundle nestedTwo = new PersistableBundle();
        nestedTwo.putString("nestedKey", "nestedValue2"); // different value
        two.putPersistableBundle("nestedBundle", nestedTwo);

        // Then
        assertThat(PersistableBundleEquality.areBundlesEqual(one, two), is(false));
    }
}
