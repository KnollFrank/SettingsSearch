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
        return switch (mode) {
            case COMPUTE_AND_PERSIST_GRAPH -> {
                final Graph<PreferenceScreenWithHostClass, PreferenceEdge> searchablePreferenceScreenGraph = searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph(rootPreferenceFragmentClassName);
                final FileOutputStream fileOutputStream;
                try {
                    fileOutputStream = preferenceManager.getContext().openFileOutput("someFile.json", Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                SearchablePreferenceScreenGraphDAO.persist(searchablePreferenceScreenGraph, fileOutputStream);
                // then copy /data/data/de.KnollFrank.settingssearch/files/someFile.json from device to /home/frankknoll/AndroidStudioProjects/SettingsSearch/app/src/main/assets
                yield searchablePreferenceScreenGraph;
            }
            case LOAD_GRAPH -> {
                final InputStream fileInputStream =
                        AssetsUtils.open(
                                preferenceManager.getContext().getAssets(),
                                new File("someFile.json"));
                yield SearchablePreferenceScreenGraphDAO.load(fileInputStream, preferenceManager);
            }
        };
    }
}
