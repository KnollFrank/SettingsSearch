package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderData;
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

    public GraphAndDbDataProvider load() {
        return new GraphAndDbDataProvider(
                _load().orElseThrow(),
                getDbDataProvider());
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

    private DbDataProvider getDbDataProvider() {
        return DbDataProviderFactory.createDbDataProvider(this, screenDAO, preferenceDAO);
    }

    // FK-TODO: remove method and make called methods private again?
    DbDataProviderData createDbDataProviderData() {
        return new DbDataProviderData(
                nodesByGraph(),
                screenDAO.getAllPreferencesBySearchablePreferenceScreen(),
                screenDAO.getHostByPreference(),
                preferenceDAO.getPredecessorByPreference(),
                preferenceDAO.getChildrenByPreference());
    }

    private Map<SearchablePreferenceScreenGraphEntity, Set<SearchablePreferenceScreenEntity>> nodesByGraph() {
        final SearchablePreferenceScreenGraphEntity graph = _load().orElseThrow();
        return Map.of(graph, graph.getNodes(this));
    }
}
