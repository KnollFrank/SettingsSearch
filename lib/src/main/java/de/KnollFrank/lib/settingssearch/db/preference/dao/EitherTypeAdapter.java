package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.codepoetics.ambivalence.Either;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

class EitherTypeAdapter extends TypeAdapter<Either<Integer, String>> {

    @Override
    public void write(final JsonWriter out, final Either<Integer, String> value) throws IOException {
        out.beginObject();
        out.name(value.join(integer -> "left", string -> "right"));
        writeValue(out, value);
        out.endObject();
    }

    @Override
    public Either<Integer, String> read(final JsonReader in) throws IOException {
        // FK-TODO: refactor
        Either<Integer, String> either;
        in.beginObject();
        final String name = in.nextName();
        // FK-TODO: use switch
        if ("left".equals(name)) {
            either = Either.ofLeft(in.nextInt());
        } else if ("right".equals(name)) {
            either = Either.ofRight(in.nextString());
        } else {
            throw new IOException();
        }
        in.endObject();
        return either;
    }

    private static void writeValue(final JsonWriter out,
                                   final Either<Integer, String> either) throws IOException {
        if (either.isLeft()) {
            out.value(getLeft(either));
        } else {
            out.value(getRight(either));
        }
    }

    private static Integer getLeft(final Either<Integer, String> either) {
        return either.left().toOptional().orElseThrow();
    }

    private static String getRight(final Either<Integer, String> either) {
        return either.right().toOptional().orElseThrow();
    }
}
