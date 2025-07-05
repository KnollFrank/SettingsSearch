package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;

public class SearchablePreferenceScreenGraphDAO {

    private final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter;
    private final SearchablePreferenceScreenGraphEntityDAO delegate;

    public SearchablePreferenceScreenGraphDAO(final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter, final SearchablePreferenceScreenGraphEntityDAO delegate) {
        this.entityGraphPojoGraphConverter = entityGraphPojoGraphConverter;
        this.delegate = delegate;
    }

    public void persist(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        delegate.persist(entityGraphPojoGraphConverter.doBackward(pojoGraph));
    }

    public Optional<Graph<SearchablePreferenceScreen, SearchablePreferenceEdge>> load() {
        // FK-TODO: cache persisted and loaded graph?
        return delegate
                .load()
                .map(entityGraphPojoGraphConverter::doForward);
    }
}
