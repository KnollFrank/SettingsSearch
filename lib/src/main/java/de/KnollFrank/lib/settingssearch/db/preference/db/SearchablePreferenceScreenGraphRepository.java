package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenGraphCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenGraphTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.graph.ComputePreferencesListener;

public class SearchablePreferenceScreenGraphRepository<C> {

    private final SearchablePreferenceScreenGraphDAO delegate;
    private final GraphProcessorManager<C> graphProcessorManager;

    public static <C> SearchablePreferenceScreenGraphRepository<C> of(final SearchablePreferenceScreenGraphDAO delegate,
                                                                      final ComputePreferencesListener computePreferencesListener) {
        return new SearchablePreferenceScreenGraphRepository<>(delegate, new GraphProcessorManager<>(computePreferencesListener));
    }

    private SearchablePreferenceScreenGraphRepository(final SearchablePreferenceScreenGraphDAO delegate,
                                                      final GraphProcessorManager<C> graphProcessorManager) {
        this.delegate = delegate;
        this.graphProcessorManager = graphProcessorManager;
    }

    public void persistOrReplace(final SearchablePreferenceScreenTree searchablePreferenceScreenTree) {
        delegate.persistOrReplace(searchablePreferenceScreenTree);
        graphProcessorManager.removeGraphProcessors();
    }

    public void addGraphTransformer(final SearchablePreferenceScreenGraphTransformer<C> graphTransformer) {
        graphProcessorManager.addGraphTransformer(graphTransformer);
    }

    public void addGraphCreator(final SearchablePreferenceScreenGraphCreator<C> graphCreator) {
        graphProcessorManager.addGraphCreator(graphCreator);
    }

    public Optional<SearchablePreferenceScreenTree> findGraphById(final Locale id,
                                                                  final C actualConfiguration,
                                                                  final FragmentActivity activityContext) {
        updateSearchDatabase(actualConfiguration, activityContext);
        return delegate.findGraphById(id);
    }

    public Set<SearchablePreferenceScreenTree> loadAll(final C actualConfiguration,
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
                    .forEach(delegate::persistOrReplace);
        }
    }
}
