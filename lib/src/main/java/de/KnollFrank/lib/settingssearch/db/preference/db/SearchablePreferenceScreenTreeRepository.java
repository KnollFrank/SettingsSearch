package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.graph.ComputePreferencesListener;

public class SearchablePreferenceScreenTreeRepository<C> {

    private final SearchablePreferenceScreenGraphDAO delegate;
    private final TreeProcessorManager<C> treeProcessorManager;

    public static <C> SearchablePreferenceScreenTreeRepository<C> of(final SearchablePreferenceScreenGraphDAO delegate,
                                                                     final ComputePreferencesListener computePreferencesListener) {
        return new SearchablePreferenceScreenTreeRepository<>(delegate, new TreeProcessorManager<>(computePreferencesListener));
    }

    private SearchablePreferenceScreenTreeRepository(final SearchablePreferenceScreenGraphDAO delegate,
                                                     final TreeProcessorManager<C> treeProcessorManager) {
        this.delegate = delegate;
        this.treeProcessorManager = treeProcessorManager;
    }

    public void persistOrReplace(final SearchablePreferenceScreenTree searchablePreferenceScreenTree) {
        delegate.persistOrReplace(searchablePreferenceScreenTree);
        treeProcessorManager.removeTreeProcessors();
    }

    public void addTreeTransformer(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        treeProcessorManager.addTreeTransformer(treeTransformer);
    }

    public void addTreeCreator(final SearchablePreferenceScreenTreeCreator<C> TreeCreator) {
        treeProcessorManager.addTreeCreator(TreeCreator);
    }

    public Optional<SearchablePreferenceScreenTree> findTreeById(final Locale id,
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
        treeProcessorManager.removeTreeProcessors();
    }

    private void updateSearchDatabase(final C actualConfiguration, final FragmentActivity activityContext) {
        if (treeProcessorManager.hasTreeProcessors()) {
            treeProcessorManager
                    .applyTreeProcessorsToTrees(
                            new ArrayList<>(delegate.loadAll()),
                            actualConfiguration,
                            activityContext)
                    .forEach(delegate::persistOrReplace);
        }
    }
}
