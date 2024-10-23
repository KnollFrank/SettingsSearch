package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import org.jgrapht.Graph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class ComputeAndPersist implements SearchablePreferenceScreenGraphProvider {

    private final SearchablePreferenceScreenGraphProvider delegate;
    private final Context context;

    public ComputeAndPersist(final SearchablePreferenceScreenGraphProvider delegate, final Context context) {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    public Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getSearchablePreferenceScreenGraph() {
        final var searchablePreferenceScreenGraph = delegate.getSearchablePreferenceScreenGraph();
        POJOGraphDAO.persist(searchablePreferenceScreenGraph, getFileOutputStream());
        // then copy /data/data/de.KnollFrank.settingssearch/files/searchablePreferenceScreenGraph.json from device to /home/frankknoll/AndroidStudioProjects/SettingsSearch/app/src/main/res/raw-<country code>
        return searchablePreferenceScreenGraph;
    }

    private FileOutputStream getFileOutputStream() {
        try {
            return context.openFileOutput("searchable_preference_screen_graph.json", Context.MODE_PRIVATE);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
