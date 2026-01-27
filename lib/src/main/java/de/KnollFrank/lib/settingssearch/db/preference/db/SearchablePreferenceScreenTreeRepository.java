package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenTreeDao;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;

public class SearchablePreferenceScreenTreeRepository<C> {

    private final PreferencesRoomDatabase database;
    private final SearchablePreferenceScreenTreeDao delegate;
    private final TreeProcessorManager<C> treeProcessorManager;

    public SearchablePreferenceScreenTreeRepository(final PreferencesRoomDatabase database,
                                                    final SearchablePreferenceScreenTreeDao delegate,
                                                    final TreeProcessorManager<C> treeProcessorManager) {
        // FK-TODO: nicht die ganze Datenbank übergeben, sondern nur database::runInTransaction
        this.database = database;
        this.delegate = delegate;
        this.treeProcessorManager = treeProcessorManager;
    }

    public void persistOrReplace(final SearchablePreferenceScreenTree<PersistableBundle> searchablePreferenceScreenTree) {
        delegate.persistOrReplace(searchablePreferenceScreenTree);
        treeProcessorManager.removeTreeProcessors();
    }

    public void addTreeTransformer(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        treeProcessorManager.addTreeTransformer(treeTransformer);
    }

    public void addTreeCreator(final SearchablePreferenceScreenTreeCreator<C> TreeCreator) {
        treeProcessorManager.addTreeCreator(TreeCreator);
    }

    public Optional<SearchablePreferenceScreenTree<PersistableBundle>> findTreeById(final Locale id,
                                                                                    final C actualConfiguration,
                                                                                    final FragmentActivity activityContext) {
        updateSearchDatabase(actualConfiguration, activityContext);
        return delegate.findTreeById(id);
    }

    public Set<SearchablePreferenceScreenTree<PersistableBundle>> loadAll(final C actualConfiguration,
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
            database.runInTransaction(
                    () -> treeProcessorManager
                            .applyTreeProcessorsToTrees(
                                    new ArrayList<>(delegate.loadAll()),
                                    actualConfiguration,
                                    activityContext)
                            .forEach(delegate::persistOrReplace));
        }
    }
}
