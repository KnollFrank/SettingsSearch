package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;

// FK-TODO: make this class a @Dao and introduce a new @Entity named SearchablePreferenceScreenGraph which references it's root SearchablePreferenceScreen. Then remove SearchDatabaseStateDAO and SearchDatabaseState.
public class SearchablePreferenceScreenGraphDAO {

    private final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter;
    private final SearchablePreferenceScreenEntityGraphDAO delegate;

    public SearchablePreferenceScreenGraphDAO(final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter, final SearchablePreferenceScreenEntityGraphDAO delegate) {
        this.entityGraphPojoGraphConverter = entityGraphPojoGraphConverter;
        this.delegate = delegate;
    }

    public void persist(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        final var entityGraph = entityGraphPojoGraphConverter.doBackward(pojoGraph);
        delegate.persist(entityGraph);
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> load() {
        // FK-TODO: cache persisted and loaded graph?
        final EntityGraphAndDbDataProvider entityGraph = delegate.load();
        return entityGraphPojoGraphConverter.doForward(entityGraph);
    }
}
