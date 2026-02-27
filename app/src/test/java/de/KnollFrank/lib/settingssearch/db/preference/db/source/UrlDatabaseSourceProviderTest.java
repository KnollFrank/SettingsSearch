package de.KnollFrank.lib.settingssearch.db.preference.db.source;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class UrlDatabaseSourceProviderTest {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void shouldReadFromUrl() throws IOException {
        // Given
        final String content = "Content created dynamically within the test.";
        final URL url = createUrlProvidingContent(content);
        final DatabaseSourceProvider provider = new UrlDatabaseSourceProvider(url);

        // When
        final InputStream databaseSource = provider.getDatabaseSource();

        // Then
        assertThat(read(databaseSource), is(content));
    }

    private URL createUrlProvidingContent(final String content) throws IOException {
        return createFileWithContent(content).toURI().toURL();
    }

    private File createFileWithContent(final String content) throws IOException {
        final File file = tempFolder.newFile();
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        return file;
    }

    private static String read(final InputStream inputStream) throws IOException {
        try (final InputStream _inputStream = inputStream) {
            return new String(_inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
