package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenTreeDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

class InitialTreeTransformer<C> {

    private final Optional<SearchablePreferenceScreenTreeTransformer<C>> treeTransformer;
    private final SearchablePreferenceScreenTreeDAO searchablePreferenceScreenTreeDAO;
    private final FragmentActivity activityContext;
    private final ConfigurationBundleConverter<C> configurationBundleConverter;

    public InitialTreeTransformer(final Optional<SearchablePreferenceScreenTreeTransformer<C>> treeTransformer,
                                  final SearchablePreferenceScreenTreeDAO searchablePreferenceScreenTreeDAO,
                                  final FragmentActivity activityContext,
                                  final ConfigurationBundleConverter<C> configurationBundleConverter) {
        this.treeTransformer = treeTransformer;
        this.searchablePreferenceScreenTreeDAO = searchablePreferenceScreenTreeDAO;
        this.activityContext = activityContext;
        this.configurationBundleConverter = configurationBundleConverter;
    }

    public void transformAndPersist(final Optional<SearchablePreferenceScreenTree<PersistableBundle>> tree, final C configuration) {
        tree.ifPresent(
                _tree -> {
                    if (!Objects.equals(configuration, configurationBundleConverter.convertBackward(_tree.configuration()))) {
                        searchablePreferenceScreenTreeDAO.persistOrReplace(process(_tree, configuration));
                    }
                });
    }

    private SearchablePreferenceScreenTree<PersistableBundle> process(final SearchablePreferenceScreenTree<PersistableBundle> tree,
                                                                      final C configuration) {
        return treeTransformer
                .map(_treeTransformer ->
                             _treeTransformer
                                     .transformTree(
                                             tree.mapConfiguration(configurationBundleConverter::convertBackward),
                                             configuration,
                                             activityContext)
                                     .mapConfiguration(configurationBundleConverter::convertForward))
                .orElse(tree);
    }
}
