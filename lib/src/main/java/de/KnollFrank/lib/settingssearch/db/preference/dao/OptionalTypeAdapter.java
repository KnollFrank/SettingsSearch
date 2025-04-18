package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Optional;

class OptionalTypeAdapter<T> extends TypeAdapter<Optional<T>> {

    private final TypeAdapter<T> elementAdapter;

    public OptionalTypeAdapter(final TypeAdapter<T> elementAdapter) {
        this.elementAdapter = elementAdapter;
    }

    @Override
    public void write(final JsonWriter out, final Optional<T> value) throws IOException {
        out.beginArray();
        writeValue(out, value);
        out.endArray();
    }

    @Override
    public Optional<T> read(final JsonReader in) throws IOException {
        in.beginArray();
        final Optional<T> value = getValue(in);
        in.endArray();
        return value;
    }

    private void writeValue(final JsonWriter out, final Optional<T> value) throws IOException {
        if (value.isPresent()) {
            elementAdapter.write(out, value.orElseThrow());
        }
    }

    private Optional<T> getValue(final JsonReader in) throws IOException {
        return in.hasNext() ?
                Optional.of(elementAdapter.read(in)) :
                Optional.empty();
    }
}
