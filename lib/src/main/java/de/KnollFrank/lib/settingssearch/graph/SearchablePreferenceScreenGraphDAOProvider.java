package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.AssetsUtils;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;

public class SearchablePreferenceScreenGraphDAOProvider {

    public enum Mode {
        COMPUTE_AND_PERSIST_GRAPH,
        LOAD_GRAPH
    }

    private final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider;
    private final PreferenceManager preferenceManager;

    public SearchablePreferenceScreenGraphDAOProvider(final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider,
                                                      final PreferenceManager preferenceManager) {
        this.searchablePreferenceScreenGraphProvider = searchablePreferenceScreenGraphProvider;
        this.preferenceManager = preferenceManager;
    }

    // FK-TODO: mode nicht als Methodenparameter übergenben, sondern im Konstruktor übergeben.
    public Graph<PreferenceScreenWithHostClass, PreferenceEdge> getSearchablePreferenceScreenGraph(final String rootPreferenceFragmentClassName, final Mode mode) {
        final String fileName = "searchablePreferenceScreenGraph.json";
        return switch (mode) {
            case COMPUTE_AND_PERSIST_GRAPH ->
                    computeAndPersistGraph(rootPreferenceFragmentClassName, fileName);
            case LOAD_GRAPH -> loadGraph(fileName);
        };
    }

    private Graph<PreferenceScreenWithHostClass, PreferenceEdge> computeAndPersistGraph(final String rootPreferenceFragmentClassName, final String fileName) {
        final Graph<PreferenceScreenWithHostClass, PreferenceEdge> searchablePreferenceScreenGraph = searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph(rootPreferenceFragmentClassName);
        persist(searchablePreferenceScreenGraph, fileName);
        return searchablePreferenceScreenGraph;
    }

    private void persist(final Graph<PreferenceScreenWithHostClass, PreferenceEdge> searchablePreferenceScreenGraph, final String fileName) {
        SearchablePreferenceScreenGraphDAO.persist(
                searchablePreferenceScreenGraph,
                getFileOutputStream(fileName));
        // then copy /data/data/de.KnollFrank.settingssearch/files/searchablePreferenceScreenGraph.json from device to /home/frankknoll/AndroidStudioProjects/SettingsSearch/app/src/main/assets
    }

    private FileOutputStream getFileOutputStream(final String fileName) {
        try {
            return preferenceManager.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Graph<PreferenceScreenWithHostClass, PreferenceEdge> loadGraph(final String fileName) {
        return SearchablePreferenceScreenGraphDAO.load(
                getInputStream(fileName),
                preferenceManager);
    }

    private InputStream getInputStream(final String fileName) {
        return AssetsUtils.open(
                preferenceManager.getContext().getAssets(),
                new File(fileName));
    }

}
