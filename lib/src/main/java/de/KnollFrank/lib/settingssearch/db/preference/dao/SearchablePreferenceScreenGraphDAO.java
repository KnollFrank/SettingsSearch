package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;
import de.KnollFrank.lib.settingssearch.graph.GraphForLocale;

public class SearchablePreferenceScreenGraphDAO {

    private final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter;
    private final SearchablePreferenceScreenGraphEntityDAO delegate;

    public SearchablePreferenceScreenGraphDAO(final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter, final SearchablePreferenceScreenGraphEntityDAO delegate) {
        this.entityGraphPojoGraphConverter = entityGraphPojoGraphConverter;
        this.delegate = delegate;
    }

    public void persist(final GraphForLocale graphForLocale) {
        delegate.persist(entityGraphPojoGraphConverter.doBackward(graphForLocale));
    }

    public Optional<GraphForLocale> load() {
        // FK-TODO: cache persisted and loaded graph?
        return delegate
                .load()
                .map(entityGraphPojoGraphConverter::doForward);
    }
}
