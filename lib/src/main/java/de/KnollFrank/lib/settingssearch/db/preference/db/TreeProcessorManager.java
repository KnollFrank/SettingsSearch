package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import com.codepoetics.ambivalence.Either;

import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.dao.TreeProcessorDao;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;

public class TreeProcessorManager<C> {

    private final TreeProcessorDao<C> treeProcessorDao;
    private final TreeProcessorExecutor<C> treeProcessorExecutor;

    public TreeProcessorManager(final TreeProcessorDao<C> treeProcessorDao,
                                final TreeProcessorExecutor<C> treeProcessorExecutor) {
        this.treeProcessorDao = treeProcessorDao;
        this.treeProcessorExecutor = treeProcessorExecutor;
    }

    public void addTreeCreator(final SearchablePreferenceScreenTreeCreator<C> treeCreator) {
        treeProcessorDao.addTreeCreator(treeCreator);
    }

    public void addTreeTransformer(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        treeProcessorDao.addTreeTransformer(treeTransformer);
    }

    public List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> getTreeProcessors() {
        return treeProcessorDao.getTreeProcessors();
    }

    public void removeTreeProcessors() {
        treeProcessorDao.removeTreeProcessors();
    }

    public boolean hasTreeProcessors() {
        return treeProcessorDao.hasTreeProcessors();
    }

    public List<SearchablePreferenceScreenTree<PersistableBundle>> applyTreeProcessorsToTrees(
            final List<SearchablePreferenceScreenTree<PersistableBundle>> trees,
            final C configuration,
            final FragmentActivity activityContext) {
        final var processedTrees =
                treeProcessorExecutor.applyTreeProcessorsToTrees(
                        treeProcessorDao.getTreeProcessors(),
                        trees,
                        configuration,
                        activityContext);
        treeProcessorDao.removeTreeProcessors();
        return processedTrees;
    }
}
