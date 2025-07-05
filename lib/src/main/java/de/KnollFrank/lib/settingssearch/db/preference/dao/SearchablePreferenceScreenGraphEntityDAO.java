package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;
import de.KnollFrank.lib.settingssearch.graph.GraphAndDbDataProvider;

@Dao
public abstract class SearchablePreferenceScreenGraphEntityDAO implements SearchablePreferenceScreenGraphEntity.DbDataProvider {

    private final SearchablePreferenceScreenEntityDAO screenDAO;
    private final SearchablePreferenceEntityDAO preferenceDAO;

    public SearchablePreferenceScreenGraphEntityDAO(final AppDatabase appDatabase) {
        this.screenDAO = appDatabase.searchablePreferenceScreenEntityDAO();
        this.preferenceDAO = appDatabase.searchablePreferenceEntityDAO();
    }

    public void persist(final GraphAndDbDataProvider graphAndDbDataProvider) {
        removePersistedGraph();
        _persist(graphAndDbDataProvider);
    }

    public Optional<GraphAndDbDataProvider> load() {
        return this
                ._load()
                .map(graph ->
                             new GraphAndDbDataProvider(
                                     graph,
                                     DbDataProviderFactory.createDbDataProvider(this, screenDAO, preferenceDAO)));
    }

    @Override
    public Set<SearchablePreferenceScreenEntity> getNodes(final SearchablePreferenceScreenGraphEntity graph) {
        return screenDAO.loadAll();
    }

    @Insert
    protected abstract void persist(SearchablePreferenceScreenGraphEntity graph);

    @Query("SELECT * FROM SearchablePreferenceScreenGraphEntity")
    protected abstract Optional<SearchablePreferenceScreenGraphEntity> _load();

    @Query("DELETE FROM SearchablePreferenceScreenGraphEntity")
    protected abstract void _removeAll();

    private void removePersistedGraph() {
        screenDAO.removeAll();
        _removeAll();
    }

    private void _persist(final GraphAndDbDataProvider graphAndDbDataProvider) {
        screenDAO.persist(
                graphAndDbDataProvider
                        .graph()
                        .getNodes(graphAndDbDataProvider.dbDataProvider()),
                graphAndDbDataProvider.dbDataProvider());
        persist(graphAndDbDataProvider.graph());
    }
}
