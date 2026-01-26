package de.KnollFrank.lib.settingssearch.db.preference.db;

import de.KnollFrank.lib.settingssearch.db.preference.dao.TreeProcessorDao;
import de.KnollFrank.lib.settingssearch.db.preference.dao.TreeProcessorDescriptionEntityDao;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.TreeProcessorDescriptionConverter;

public class TreeProcessorManagerFactory {

    public static <C> TreeProcessorManager<C> createTreeProcessorManager(
            final TreeProcessorDescriptionEntityDao treeProcessorDescriptionEntityDao,
            final TreeProcessorFactory<C> treeProcessorFactory,
            final ConfigurationBundleConverter<C> configurationBundleConverter) {
        return new TreeProcessorManager<>(
                new TreeProcessorDao<>(
                        treeProcessorDescriptionEntityDao,
                        new TreeProcessorDescriptionConverter<>(treeProcessorFactory)),
                new TreeProcessorExecutor<>(configurationBundleConverter));
    }
}
