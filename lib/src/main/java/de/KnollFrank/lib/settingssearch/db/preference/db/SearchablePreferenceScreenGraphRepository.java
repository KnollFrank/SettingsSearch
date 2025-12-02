package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

public class SearchablePreferenceScreenGraphRepository<C> {

    private final SearchablePreferenceScreenGraphDAO delegate;
    private final GraphProcessorManager<C> graphProcessorManager = new GraphProcessorManager<>();

    public SearchablePreferenceScreenGraphRepository(final SearchablePreferenceScreenGraphDAO delegate) {
        this.delegate = delegate;
    }

    public void persist(final SearchablePreferenceScreenGraph searchablePreferenceScreenGraph) {
        delegate.persist(searchablePreferenceScreenGraph);
        graphProcessorManager.removeGraphProcessors();
    }

    public void addGraphTransformer(final SearchablePreferenceScreenGraphTransformer<C> graphTransformer) {
        graphProcessorManager.addGraphTransformer(graphTransformer);
    }

    public void addGraphCreator(final SearchablePreferenceScreenGraphCreator<C> graphCreator) {
        graphProcessorManager.addGraphCreator(graphCreator);
    }

    public Optional<SearchablePreferenceScreenGraph> findGraphById(final Locale id,
                                                                   final C actualConfiguration,
                                                                   final FragmentActivity activityContext) {
        updateSearchDatabase(actualConfiguration, activityContext);
        return delegate.findGraphById(id);
    }

    public Set<SearchablePreferenceScreenGraph> loadAll(final C actualConfiguration,
                                                        final FragmentActivity activityContext) {
        updateSearchDatabase(actualConfiguration, activityContext);
        return delegate.loadAll();
    }

    public void removeAll() {
        delegate.removeAll();
        graphProcessorManager.removeGraphProcessors();
    }

    private void updateSearchDatabase(final C actualConfiguration, final FragmentActivity activityContext) {
        if (graphProcessorManager.hasGraphProcessors()) {
            graphProcessorManager
                    .applyGraphProcessorsToGraphs(
                            new ArrayList<>(delegate.loadAll()),
                            actualConfiguration,
                            activityContext)
                    .forEach(delegate::persist);
        }
    }
}
