package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;

public class SearchablePreferenceScreenGraphDAO {

    private final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter;
    private final SearchablePreferenceScreenGraphEntityDAO delegate;

    public SearchablePreferenceScreenGraphDAO(final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter, final SearchablePreferenceScreenGraphEntityDAO delegate) {
        this.entityGraphPojoGraphConverter = entityGraphPojoGraphConverter;
        this.delegate = delegate;
    }

    public void persist(final SearchablePreferenceScreenGraph searchablePreferenceScreenGraph) {
        delegate.persist(entityGraphPojoGraphConverter.doBackward(searchablePreferenceScreenGraph));
    }

    public Optional<SearchablePreferenceScreenGraph> findGraphById(final Locale id) {
        // FK-TODO: cache persisted and loaded graph?
        return delegate
                .findGraphById(id)
                .map(entityGraphPojoGraphConverter::doForward);
    }

    public Set<SearchablePreferenceScreenGraph> loadAll() {
        return delegate
                .loadAll()
                .stream()
                .map(entityGraphPojoGraphConverter::doForward)
                .collect(Collectors.toSet());
    }

    public void removeAll() {
        delegate.removeAll();
    }
}
