package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;
import de.KnollFrank.lib.settingssearch.graph.GraphAndDbDataProvider;

// FK-TODO: remove SearchDatabaseStateDAO and SearchDatabaseState
public class SearchablePreferenceScreenGraphDAO {

    private final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter;
    private final SearchablePreferenceScreenGraphEntityDAO delegate;

    public SearchablePreferenceScreenGraphDAO(final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter, final SearchablePreferenceScreenGraphEntityDAO delegate) {
        this.entityGraphPojoGraphConverter = entityGraphPojoGraphConverter;
        this.delegate = delegate;
    }

    public void persist(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        final var entityGraph = entityGraphPojoGraphConverter.doBackward(pojoGraph);
        delegate.persist(entityGraph);
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> load() {
        // FK-TODO: cache persisted and loaded graph?
        final GraphAndDbDataProvider entityGraph = delegate.load();
        return entityGraphPojoGraphConverter.doForward(entityGraph);
    }
}
