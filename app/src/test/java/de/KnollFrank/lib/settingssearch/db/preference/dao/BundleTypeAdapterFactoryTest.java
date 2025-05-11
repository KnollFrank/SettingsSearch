package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

// adapted from https://github.com/google-gson/typeadapters/blob/master/android/src/test/java/BundleTypeAdapterFactoryTest.java
@RunWith(RobolectricTestRunner.class)
public class BundleTypeAdapterFactoryTest {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new BundleTypeAdapterFactory())
            .create();

    @Test
    public void testSerialize() {
        // Given
        final Bundle bundle = new Bundle();
        bundle.putInt("a", 1);
        bundle.putString("b", "b value");

        // When
        final JsonObject json = gson.toJsonTree(bundle).getAsJsonObject();

        // Then
        assertThat(json.entrySet().size(), is(2));
        assertThat(json.get("b").getAsString(), is("b value"));
        assertThat(json.get("a").getAsInt(), is(1));
    }

    @Test
    public void testDeserialize() {
        // Given
        final String json = "{'a':1,'b':'abcd'}";

        // When
        final Bundle bundle = gson.fromJson(json, Bundle.class);

        // Then
        assertThat(bundle.getInt("a"), is(1));
        assertThat(bundle.getString("b"), is("abcd"));
    }

    @Test
    public void testSerializeEmpty() {
        // Given
        final Bundle bundle = new Bundle();

        // When
        final String json = gson.toJson(bundle);

        // Then
        assertThat(json, is("{}"));
    }

    @Test
    public void testDeserializeEmpty() {
        // Given
        final String json = "{}";

        // When
        final Bundle bundle = gson.fromJson(json, Bundle.class);

        // Then
        assertThat(bundle.size(), is(0));
    }

    @Test
    public void testSerializeBundleOfBundles() {
        // Given
        final Bundle outer = new Bundle();
        outer.putString("a", "abc");

        final Bundle inner = new Bundle();
        inner.putString("b", "bcd");

        outer.putBundle("c", inner);

        // When
        final JsonObject jsonObj = gson.toJsonTree(outer).getAsJsonObject();

        // Then
        assertThat(jsonObj.get("c").getAsJsonObject().get("b").getAsString(), is("bcd"));
    }

    @Test
    public void testDeserializeBundleOfBundles() {
        // Given
        final String json = "{a:1,b:{c:2},c:{d:3}}";

        // When
        final Bundle bundle = gson.fromJson(json, Bundle.class);

        // Then
        assertThat(bundle.getBundle("c").getInt("d"), is(3));
    }
}