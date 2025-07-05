package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jgrapht.Graph;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenEntitiesToGraphConverter;

@Entity
public record SearchablePreferenceScreenGraphEntity(@PrimaryKey int id) {

    public interface DbDataProvider {

        Set<SearchablePreferenceScreenEntity> getNodes(SearchablePreferenceScreenGraphEntity graph);
    }

    public Set<SearchablePreferenceScreenEntity> getNodes(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getNodes(this);
    }

    public Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> asGraph(final de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider dbDataProvider) {
        return SearchablePreferenceScreenEntitiesToGraphConverter.convertScreensToGraph(
                getNodes(dbDataProvider),
                dbDataProvider);
    }
}
