package de.KnollFrank.lib.settingssearch.graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

public class EntityTreePojoTreeConverter implements Converter<TreeAndDbDataProvider, SearchablePreferenceScreenTree> {

    @Override
    public SearchablePreferenceScreenTree convertForward(final TreeAndDbDataProvider treeAndDbDataProvider) {
        return new SearchablePreferenceScreenTree(
                EntityGraphToPojoGraphTransformer.toPojoGraph(
                        treeAndDbDataProvider.asGraph(),
                        treeAndDbDataProvider.dbDataProvider()),
                treeAndDbDataProvider.tree().id(),
                treeAndDbDataProvider.tree().configuration());
    }

    @Override
    public TreeAndDbDataProvider convertBackward(final SearchablePreferenceScreenTree pojoTree) {
        return PojoGraphToEntityGraphTransformer.toEntityGraph(
                pojoTree.tree(),
                pojoTree.locale(),
                pojoTree.configuration());
    }
}
