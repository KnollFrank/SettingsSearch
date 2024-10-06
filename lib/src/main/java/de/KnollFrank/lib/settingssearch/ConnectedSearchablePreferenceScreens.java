package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

public record ConnectedSearchablePreferenceScreens(
        Set<PreferenceScreenWithHostClass> connectedSearchablePreferenceScreens,
        Map<Preference, PreferencePath> preferencePathByPreference) {

    public static ConnectedSearchablePreferenceScreens fromSearchablePreferenceScreenGraph(final Graph<PreferenceScreenWithHostClass, PreferenceEdge> searchablePreferenceScreenGraph,
                                                                                           final PreferenceManager preferenceManager) {
        final ConnectedSearchablePreferenceScreens connectedSearchablePreferenceScreens =
                new ConnectedSearchablePreferenceScreens(
                        searchablePreferenceScreenGraph.vertexSet(),
                        PreferencePathByPreferenceProvider.getPreferencePathByPreference(searchablePreferenceScreenGraph));
        // FK-TODO: BEGIN: remove test code
/*
        final var pojo = ConnectedSearchablePreferenceScreens2POJOConverter.convert2POJO(connectedSearchablePreferenceScreens);
        final var outputStream = new ByteArrayOutputStream();
        ConnectedSearchablePreferenceScreensPOJODAO.persist(pojo, outputStream);
        final var pojoLoaded = ConnectedSearchablePreferenceScreensPOJODAO.load(convert(outputStream));
        final var entityLoaded = ConnectedSearchablePreferenceScreensFromPOJOConverter.convertFromPOJO(pojoLoaded, preferenceManager);
        return entityLoaded;
*/
        // FK-TODO: END: remove test code
        return connectedSearchablePreferenceScreens;
    }

    private static InputStream convert(final OutputStream outputStream) {
        return new ByteArrayInputStream(outputStream.toString().getBytes(StandardCharsets.UTF_8));
    }
}
