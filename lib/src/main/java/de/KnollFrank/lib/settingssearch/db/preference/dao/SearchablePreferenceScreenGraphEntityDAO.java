package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;

@Dao
public abstract class SearchablePreferenceScreenGraphEntityDAO implements SearchablePreferenceScreenGraphEntity.DbDataProvider {

    private final SearchablePreferenceScreenEntityDAO screenDAO;
    private final SearchablePreferenceEntityDAO preferenceDAO;

    public SearchablePreferenceScreenGraphEntityDAO(final PreferencesRoomDatabase preferencesRoomDatabase) {
        this.screenDAO = preferencesRoomDatabase.searchablePreferenceScreenEntityDAO();
        this.preferenceDAO = preferencesRoomDatabase.searchablePreferenceEntityDAO();
    }

    public void persist(final GraphAndDbDataProvider graphAndDbDataProvider) {
        removeIfPresent(graphAndDbDataProvider.graph());
        _persist(graphAndDbDataProvider);
    }

    public Optional<GraphAndDbDataProvider> findGraphById(final Locale id) {
        return this
                ._findGraphById(id)
                .map(this::createGraphAndDbDataProvider);
    }

    public Set<GraphAndDbDataProvider> loadAll() {
        return this
                ._loadAll()
                .stream()
                .map(this::createGraphAndDbDataProvider)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<SearchablePreferenceScreenEntity> getNodes(final SearchablePreferenceScreenGraphEntity graph) {
        // FK-TODO: add cache?
        return screenDAO.findSearchablePreferenceScreensByGraphId(graph.id());
    }

    public void removeAll() {
        screenDAO.removeAll();
        _removeAll();
    }

    @Insert
    protected abstract void persist(SearchablePreferenceScreenGraphEntity graph);

    @Delete
    protected abstract void _remove(SearchablePreferenceScreenGraphEntity graph);

    @Query("DELETE FROM SearchablePreferenceScreenGraphEntity")
    protected abstract void _removeAll();

    @Query("SELECT * FROM SearchablePreferenceScreenGraphEntity WHERE id = :id")
    protected abstract Optional<SearchablePreferenceScreenGraphEntity> _findGraphById(Locale id);

    @Query("SELECT * FROM SearchablePreferenceScreenGraphEntity")
    protected abstract List<SearchablePreferenceScreenGraphEntity> _loadAll();

    private GraphAndDbDataProvider createGraphAndDbDataProvider(final SearchablePreferenceScreenGraphEntity graph) {
        return new GraphAndDbDataProvider(
                graph,
                DbDataProviderFactory.createDbDataProvider(this, screenDAO, preferenceDAO));
    }

    private void removeIfPresent(final SearchablePreferenceScreenGraphEntity graph) {
        this
                .findGraphById(graph.id())
                .map(GraphAndDbDataProvider::graph)
                .ifPresent(this::remove);
    }

    // FK-TODO: refactor
    private void remove(final SearchablePreferenceScreenGraphEntity graph) {
        // 1. Alle zu löschenden Screens, die zum Graphen gehören, effizient laden.
        final Set<SearchablePreferenceScreenEntity> screensToRemove = screenDAO.findSearchablePreferenceScreensByGraphId(graph.id());
        if (screensToRemove.isEmpty()) {
            _remove(graph); // Nur den Graphen selbst löschen und fertig.
            return;
        }

        // 2. Alle Preferences, die zu diesen Screens gehören, in einem Rutsch holen.
        // Der Cache in screenDAO hilft hier, die performante JOIN-Abfrage nur einmal auszuführen.
        final Set<SearchablePreferenceEntity> preferencesToRemove =
                screensToRemove
                        .stream()
                        .flatMap(screen -> screen.getAllPreferencesOfPreferenceHierarchy(screenDAO).stream())
                        .collect(Collectors.toSet());

        // 3. Alle Preferences mit EINER einzigen, schnellen DELETE-Anweisung löschen.
        preferenceDAO.remove(preferencesToRemove);

        // 4. Alle Screens mit EINER einzigen, schnellen DELETE-Anweisung löschen.
        final Set<String> screenIdsToRemove =
                screensToRemove
                        .stream()
                        .map(SearchablePreferenceScreenEntity::id)
                        .collect(Collectors.toSet());
        screenDAO._removeByIds(screenIdsToRemove); // Direkter Aufruf der performanten Methode

        // Wichtig: Da wir die öffentliche `remove` des screenDAO umgangen haben,
        // müssen wir dessen Cache manuell invalidieren.
        screenDAO.invalidateCaches();

        // 5. Zum Schluss den Graphen selbst löschen.
        _remove(graph);
    }

    // FK-TODO: refactor
    private void _persist(final GraphAndDbDataProvider graphAndDbDataProvider) {
        // 1. Alle zu persistierenden Screens aus dem Graphen holen
        final Set<SearchablePreferenceScreenEntity> screensToPersist = getScreens(graphAndDbDataProvider);

        // 2. Den ScreenDAO aufrufen, der nun die gesamte Batch-Logik effizient handhabt
        screenDAO.persist(
                screensToPersist,
                graphAndDbDataProvider.dbDataProvider());

        // 3. Den Graphen selbst persistieren
        persist(graphAndDbDataProvider.graph());
    }

    private static Set<SearchablePreferenceScreenEntity> getScreens(final GraphAndDbDataProvider graphAndDbDataProvider) {
        return graphAndDbDataProvider
                .graph()
                .getNodes(graphAndDbDataProvider.dbDataProvider());
    }
}
