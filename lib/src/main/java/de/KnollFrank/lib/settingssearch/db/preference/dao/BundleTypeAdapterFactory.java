package de.KnollFrank.lib.settingssearch.db.preference.dao;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// adapted from https://github.com/google-gson/typeadapters/blob/master/android/src/main/java/BundleTypeAdapterFactory.java

/**
 * Type adapter for Android Bundle. It only stores the actual properties set in the extras
 *
 * @author Inderjeet Singh
 */
// FK-TODO: add unit test
class BundleTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        if (!Bundle.class.isAssignableFrom(type.getRawType())) {
            return null;
        }
        return (TypeAdapter<T>) new TypeAdapter<Bundle>() {

            @Override
            public void write(final JsonWriter out, final Bundle bundle) throws IOException {
                if (bundle == null) {
                    out.nullValue();
                    return;
                }
                out.beginObject();
                for (final String key : bundle.keySet()) {
                    out.name(key);
                    final Object value = bundle.get(key);
                    if (value == null) {
                        out.nullValue();
                    } else {
                        gson.toJson(value, value.getClass(), out);
                    }
                }
                out.endObject();
            }

            @Override
            public Bundle read(final JsonReader in) throws IOException {
                return switch (in.peek()) {
                    case NULL -> {
                        in.nextNull();
                        yield null;
                    }
                    case BEGIN_OBJECT -> toBundle(readObject(in));
                    default -> throw new IOException("expecting object: " + in.getPath());
                };
            }

            private Bundle toBundle(final List<Pair<String, Object>> values) throws IOException {
                final Bundle bundle = new Bundle();
                for (final Pair<String, Object> entry : values) {
                    String key = entry.first;
                    Object value = entry.second;
                    if (value instanceof final String _value) {
                        bundle.putString(key, _value);
                    } else if (value instanceof final Integer _value) {
                        bundle.putInt(key, _value);
                    } else if (value instanceof final Long _value) {
                        bundle.putLong(key, _value);
                    } else if (value instanceof final Double _value) {
                        bundle.putDouble(key, _value);
                    } else if (value instanceof final Parcelable _value) {
                        bundle.putParcelable(key, _value);
                    } else if (value instanceof List) {
                        final List<Pair<String, Object>> objectValues = (List<Pair<String, Object>>) value;
                        final Bundle subBundle = toBundle(objectValues);
                        bundle.putParcelable(key, subBundle);
                    } else {
                        throw new IOException("Unparcelable key, value: " + key + ", " + value);
                    }
                }
                return bundle;
            }

            private List<Pair<String, Object>> readObject(final JsonReader in) throws IOException {
                final List<Pair<String, Object>> object = new ArrayList<>();
                in.beginObject();
                while (in.peek() != JsonToken.END_OBJECT) {
                    switch (in.peek()) {
                        case NAME -> {
                            final String name = in.nextName();
                            final Object value = readValue(in);
                            object.add(new Pair<>(name, value));
                        }
                        case END_OBJECT -> {
                        }
                        default -> throw new IOException("expecting object: " + in.getPath());
                    }
                }
                in.endObject();
                return object;
            }

            private Object readValue(final JsonReader in) throws IOException {
                return switch (in.peek()) {
                    case BEGIN_ARRAY -> readArray(in);
                    case BEGIN_OBJECT -> readObject(in);
                    case BOOLEAN -> in.nextBoolean();
                    case NULL -> {
                        in.nextNull();
                        yield null;
                    }
                    case NUMBER -> readNumber(in);
                    case STRING -> in.nextString();
                    default -> throw new IOException("expecting value: " + in.getPath());
                };
            }

            private Object readNumber(final JsonReader in) throws IOException {
                final double doubleValue = in.nextDouble();
                if (doubleValue - Math.ceil(doubleValue) == 0) {
                    final long longValue = (long) doubleValue;
                    if (Integer.MIN_VALUE <= longValue && longValue <= Integer.MAX_VALUE) {
                        return (int) longValue;
                    }
                    return longValue;
                }
                return doubleValue;
            }

            @SuppressWarnings("rawtypes")
            private List readArray(final JsonReader in) throws IOException {
                final List list = new ArrayList();
                in.beginArray();
                while (in.peek() != JsonToken.END_ARRAY) {
                    final Object element = readValue(in);
                    list.add(element);
                }
                in.endArray();
                return list;
            }
        };
    }
}
