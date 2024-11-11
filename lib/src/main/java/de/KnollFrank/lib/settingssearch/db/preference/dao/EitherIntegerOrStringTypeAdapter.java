package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.codepoetics.ambivalence.Either;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

class EitherIntegerOrStringTypeAdapter extends TypeAdapter<Either<Integer, String>> {

    private static final String LEFT = "left";
    private static final String RIGHT = "right";

    @Override
    public void write(final JsonWriter out, final Either<Integer, String> value) throws IOException {
        out.beginObject();
        writeName(out, value);
        writeValue(out, value);
        out.endObject();
    }

    @Override
    public Either<Integer, String> read(final JsonReader in) throws IOException {
        in.beginObject();
        final String name = readName(in);
        final Either<Integer, String> either = readValue(in, name);
        in.endObject();
        return either;
    }

    private static void writeName(final JsonWriter out, final Either<Integer, String> value) throws IOException {
        out.name(value.join(integer -> LEFT, string -> RIGHT));
    }

    private static String readName(final JsonReader in) throws IOException {
        return in.nextName();
    }

    private static void writeValue(final JsonWriter out, final Either<Integer, String> value) throws IOException {
        if (value.isLeft()) {
            out.value(getLeft(value));
        } else {
            out.value(getRight(value));
        }
    }

    private static Either<Integer, String> readValue(final JsonReader in, final String name) throws IOException {
        return switch (name) {
            case LEFT -> Either.ofLeft(in.nextInt());
            case RIGHT -> Either.ofRight(in.nextString());
            default -> throw new IOException();
        };
    }

    private static Integer getLeft(final Either<Integer, String> either) {
        return either.left().toOptional().orElseThrow();
    }

    private static String getRight(final Either<Integer, String> either) {
        return either.right().toOptional().orElseThrow();
    }
}
