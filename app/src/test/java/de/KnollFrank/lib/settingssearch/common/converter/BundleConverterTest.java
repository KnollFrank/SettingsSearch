package de.KnollFrank.lib.settingssearch.common.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BundleConverterTest {

    @Test
    public void toPersistableBundle_shouldConvertSupportedPrimitiveTypes() {
        // Given
        final Bundle bundle = new Bundle();
        bundle.putString("stringKey", "stringValue");
        bundle.putInt("intKey", 123);
        bundle.putLong("longKey", 456L);
        bundle.putDouble("doubleKey", 7.89);
        bundle.putBoolean("boolKey", true);

        // When
        final PersistableBundle result = BundleConverter.toPersistableBundle(bundle);

        // Then
        assertThat(result.getString("stringKey"), is(bundle.getString("stringKey")));
        assertThat(result.getInt("intKey"), is(bundle.getInt("intKey")));
        assertThat(result.getLong("longKey"), is(bundle.getLong("longKey")));
        assertThat(result.getDouble("doubleKey"), is(bundle.getDouble("doubleKey")));
        assertThat(result.getBoolean("boolKey"), is(bundle.getBoolean("boolKey")));
        assertThat(result.size(), is(5));
    }

    @Test
    public void toPersistableBundle_shouldConvertSupportedArrayTypes() {
        // Given
        final Bundle bundle = new Bundle();
        bundle.putStringArray("stringArrayKey", new String[]{"a", "b"});
        bundle.putIntArray("intArrayKey", new int[]{1, 2});
        bundle.putLongArray("longArrayKey", new long[]{3L, 4L});
        bundle.putDoubleArray("doubleArrayKey", new double[]{5.5, 6.6});
        bundle.putBooleanArray("boolArrayKey", new boolean[]{true, false});

        // When
        final PersistableBundle result = BundleConverter.toPersistableBundle(bundle);

        // Then
        assertThat(result.getStringArray("stringArrayKey"), is(bundle.getStringArray("stringArrayKey")));
        assertThat(result.getIntArray("intArrayKey"), is(bundle.getIntArray("intArrayKey")));
        assertThat(result.getLongArray("longArrayKey"), is(bundle.getLongArray("longArrayKey")));
        assertThat(result.getDoubleArray("doubleArrayKey"), is(bundle.getDoubleArray("doubleArrayKey")));
        assertThat(result.getBooleanArray("boolArrayKey"), is(bundle.getBooleanArray("boolArrayKey")));
        assertThat(result.size(), is(5));
    }

    @Test
    public void toPersistableBundle_shouldRecursivelyConvertNestedBundle() {
        // Given
        final Bundle innerBundle = new Bundle();
        innerBundle.putString("innerString", "nestedValue");
        innerBundle.putInt("innerInt", 99);

        final Bundle outerBundle = new Bundle();
        outerBundle.putBundle("nestedBundleKey", innerBundle);
        outerBundle.putString("outerString", "outerValue");

        // When
        final PersistableBundle result = BundleConverter.toPersistableBundle(outerBundle);

        // Then
        assertThat(result.getString("outerString"), is(outerBundle.getString("outerString")));

        final PersistableBundle nestedResult = result.getPersistableBundle("nestedBundleKey");
        assertThat(nestedResult, is(notNullValue()));
        assertThat(nestedResult.getString("innerString"), is(innerBundle.getString("innerString")));
        assertThat(nestedResult.getInt("innerInt"), is(innerBundle.getInt("innerInt")));
        assertThat(nestedResult.size(), is(2));
        assertThat(result.size(), is(2));
    }

    @Test
    public void toPersistableBundle_shouldConvertNullValueToPlaceholderString() {
        // Given
        final Bundle bundle = new Bundle();
        bundle.putString("keyWithNullValue", null);

        // When
        final PersistableBundle result = BundleConverter.toPersistableBundle(bundle);

        // Then
        assertThat(result.size(), is(1));
        assertThat(result.getString("keyWithNullValue"), is("<null>"));
    }

    @Test
    public void toPersistableBundle_shouldSerializeUnsupportedParcelableToJson() {
        // Given
        final Bundle bundle = new Bundle();
        bundle.putParcelable("pointKey", new Point(10, 20));

        // When
        final PersistableBundle result = BundleConverter.toPersistableBundle(bundle);

        // Then
        assertThat(result.size(), is(1));
        assertThat(result.getString("pointKey"), is("{\"x\":10,\"y\":20}"));
    }

    @Test
    public void toPersistableBundle_shouldHandleCircularReference() {
        // Given
        final Bundle bundle = new Bundle();
        final UnserializableObject unserializable = new UnserializableObject();
        bundle.putSerializable("unserializableKey", unserializable);

        // When
        final PersistableBundle result = BundleConverter.toPersistableBundle(bundle);

        // Then
        assertThat(result.size(), is(1));
        assertThat(result.getString("unserializableKey"), is("{}"));
    }

    @Test
    public void toPersistableBundle_shouldPreserveKeyOfNestedBundleWithOnlyUnsupportedTypes() {
        // Given
        final Bundle outerBundle = new Bundle();
        final Bundle innerBundle = new Bundle();
        innerBundle.putParcelable("unsupported", new Point(1, 2));
        outerBundle.putBundle("nestedKey", innerBundle);

        // When
        final PersistableBundle result = BundleConverter.toPersistableBundle(outerBundle);

        // Then
        assertThat(result.size(), is(1));
        assertThat(result.containsKey("nestedKey"), is(true));
        final PersistableBundle nestedResult = result.getPersistableBundle("nestedKey");
        assertThat(nestedResult, is(notNullValue()));
        assertThat(nestedResult.size(), is(1));
        assertThat(nestedResult.getString("unsupported"), is("{\"x\":1,\"y\":2}"));
    }

    private static class UnserializableObject implements java.io.Serializable {

        @SuppressWarnings("unused")
        private final UnserializableObject self = this;
    }
}
