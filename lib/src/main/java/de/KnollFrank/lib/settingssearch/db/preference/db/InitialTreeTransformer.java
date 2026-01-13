package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

class InitialTreeTransformer<C> {

    private final Optional<SearchablePreferenceScreenTreeTransformer<C>> treeTransformer;
    private final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO;
    private final FragmentActivity activityContext;
    private final ConfigurationBundleConverter<C> configurationBundleConverter;

    public InitialTreeTransformer(final Optional<SearchablePreferenceScreenTreeTransformer<C>> treeTransformer,
                                  final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO,
                                  final FragmentActivity activityContext,
                                  final ConfigurationBundleConverter<C> configurationBundleConverter) {
        this.treeTransformer = treeTransformer;
        this.searchablePreferenceScreenGraphDAO = searchablePreferenceScreenGraphDAO;
        this.activityContext = activityContext;
        this.configurationBundleConverter = configurationBundleConverter;
    }

    public void transformAndPersist(final Optional<SearchablePreferenceScreenTree> tree, final C configuration) {
        tree.ifPresent(
                _tree -> {
                    if (!Objects.equals(configuration, configurationBundleConverter.convertBackward(_tree.configuration()))) {
                        searchablePreferenceScreenGraphDAO.persistOrReplace(process(_tree, configuration));
                    }
                });
    }

    private SearchablePreferenceScreenTree process(final SearchablePreferenceScreenTree tree,
                                                   final C configuration) {
        return treeTransformer
                .map(_treeTransformer -> _treeTransformer.transformTree(tree, configuration, activityContext))
                .orElse(tree)
                .asGraphHavingConfiguration(configurationBundleConverter.convertForward(configuration));
    }
}
