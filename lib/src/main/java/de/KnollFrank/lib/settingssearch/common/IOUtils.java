package de.KnollFrank.lib.settingssearch.common;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class IOUtils {

    public static void persist(final String source, final OutputStream sink) {
        try (final Writer writer = new OutputStreamWriter(sink, StandardCharsets.UTF_8)) {
            writer.write(source);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(final ByteArrayOutputStream outputStream) {
        return new String(outputStream.toByteArray(), UTF_8);
    }

    public static Writer getWriter(final OutputStream outputStream) {
        return new BufferedWriter(new OutputStreamWriter(outputStream, UTF_8));
    }

    public static Reader getReader(final InputStream inputStream) {
        return new InputStreamReader(inputStream);
    }

    public static FileOutputStream getFileOutputStream(final File file) {

        try {
            return new FileOutputStream(file);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileInputStream getFileInputStream(final File file) {
        try {
            return new FileInputStream(file);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
