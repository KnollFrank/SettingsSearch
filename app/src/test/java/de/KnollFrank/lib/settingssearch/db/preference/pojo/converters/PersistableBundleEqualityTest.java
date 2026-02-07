package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.PersistableBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.matcher.PersistableBundleEquality;

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
        final PersistableBundle one = createPersistableBundleWithSimpleTypes();
        final PersistableBundle two = createPersistableBundleWithSimpleTypes();

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
        final PersistableBundle one = createPersistableBundleWithArrays();
        final PersistableBundle two = createPersistableBundleWithArrays();

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
        final PersistableBundle one = createPersistableBundleWithNestedBundle();
        final PersistableBundle two = createPersistableBundleWithNestedBundle();

        // Then
        assertThat(PersistableBundleEquality.areBundlesEqual(one, two), is(true));
    }

    @Test
    public void shouldReturnFalseForDifferentNestedBundles() {
        // Given
        final PersistableBundle nestedOne = new PersistableBundle();
        nestedOne.putString("nestedKey", "nestedValue1");
        final PersistableBundle one = new PersistableBundle();
        one.putPersistableBundle("nestedBundle", nestedOne);

        final PersistableBundle nestedTwo = new PersistableBundle();
        nestedTwo.putString("nestedKey", "nestedValue2"); // different value
        final PersistableBundle two = new PersistableBundle();
        two.putPersistableBundle("nestedBundle", nestedTwo);

        // Then
        assertThat(PersistableBundleEquality.areBundlesEqual(one, two), is(false));
    }

    private static PersistableBundle createPersistableBundleWithSimpleTypes() {
        final PersistableBundle bundle = new PersistableBundle();
        bundle.putString("string", "hello");
        bundle.putInt("int", 123);
        bundle.putLong("long", 456L);
        bundle.putDouble("double", 78.9);
        bundle.putBoolean("boolean", true);
        return bundle;
    }

    private static PersistableBundle createPersistableBundleWithArrays() {
        final PersistableBundle bundle = new PersistableBundle();
        bundle.putStringArray("strings", new String[]{"a", "b"});
        bundle.putIntArray("ints", new int[]{1, 2});
        bundle.putLongArray("longs", new long[]{3L, 4L});
        bundle.putDoubleArray("doubles", new double[]{5.5, 6.6});
        bundle.putBooleanArray("booleans", new boolean[]{true, false});
        return bundle;
    }

    private static PersistableBundle createPersistableBundleWithNestedBundle() {
        final PersistableBundle nestedBundle = new PersistableBundle();
        nestedBundle.putString("nestedKey", "nestedValue");

        final PersistableBundle bundle = new PersistableBundle();
        bundle.putPersistableBundle("nestedBundle", nestedBundle);
        return bundle;
    }
}
