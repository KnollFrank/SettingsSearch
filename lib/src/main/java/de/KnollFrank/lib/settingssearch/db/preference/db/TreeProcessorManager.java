package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.dao.TreeProcessorDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;

class TreeProcessorManager<C> {

    private final TreeProcessorDAO<C> treeProcessorDAO;
    private final TreeProcessorExecutor<C> treeProcessorExecutor;

    public TreeProcessorManager(final TreeProcessorDAO<C> treeProcessorDAO,
                                final TreeProcessorExecutor<C> treeProcessorExecutor) {
        this.treeProcessorDAO = treeProcessorDAO;
        this.treeProcessorExecutor = treeProcessorExecutor;
    }

    public void addTreeCreator(final SearchablePreferenceScreenTreeCreator<C> treeCreator) {
        treeProcessorDAO.addTreeCreator(treeCreator);
    }

    public void addTreeTransformer(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        treeProcessorDAO.addTreeTransformer(treeTransformer);
    }

    public void removeTreeProcessors() {
        treeProcessorDAO.removeTreeProcessors();
    }

    public boolean hasTreeProcessors() {
        return treeProcessorDAO.hasTreeProcessors();
    }

    public List<SearchablePreferenceScreenTree<PersistableBundle>> applyTreeProcessorsToTrees(
            final List<SearchablePreferenceScreenTree<PersistableBundle>> trees,
            final C configuration,
            final FragmentActivity activityContext) {
        final var processedTrees =
                treeProcessorExecutor.applyTreeProcessorsToTrees(
                        treeProcessorDAO.getTreeProcessors(),
                        trees,
                        configuration,
                        activityContext);
        treeProcessorDAO.removeTreeProcessors();
        return processedTrees;
    }
}
