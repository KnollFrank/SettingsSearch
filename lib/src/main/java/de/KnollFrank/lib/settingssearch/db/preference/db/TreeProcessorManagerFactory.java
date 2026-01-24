package de.KnollFrank.lib.settingssearch.db.preference.db;

import de.KnollFrank.lib.settingssearch.db.preference.dao.TreeProcessorDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class TreeProcessorManagerFactory {

    private TreeProcessorManagerFactory() {
    }

    public static <C> TreeProcessorManager<C> createTreeProcessorManager(final ConfigurationBundleConverter<C> configurationBundleConverter) {
        final TreeProcessorDAO<C> treeProcessorDAO = new TreeProcessorDAO<>();
        return new TreeProcessorManager<>(
                treeProcessorDAO,
                new TreeProcessorExecutor<>(
                        treeProcessorDAO,
                        configurationBundleConverter));
    }
}
