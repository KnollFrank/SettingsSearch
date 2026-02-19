package de.KnollFrank.lib.settingssearch.graph;

import android.os.PersistableBundle;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

public class EntityTreePojoTreeConverter implements Converter<TreeAndDbDataProvider, SearchablePreferenceScreenTree<PersistableBundle>> {

    @Override
    public SearchablePreferenceScreenTree<PersistableBundle> convertForward(final TreeAndDbDataProvider treeAndDbDataProvider) {
        return new SearchablePreferenceScreenTree<>(
                EntityTreeToPojoTreeTransformer.toPojoTree(
                        treeAndDbDataProvider.asGraph(),
                        treeAndDbDataProvider.dbDataProvider()),
                treeAndDbDataProvider.tree().id(),
                treeAndDbDataProvider.tree().configuration());
    }

    @Override
    public TreeAndDbDataProvider convertBackward(final SearchablePreferenceScreenTree<PersistableBundle> pojoTree) {
        return PojoGraphToEntityGraphTransformer.toEntityGraph(
                pojoTree.tree(),
                pojoTree.languageCode(),
                pojoTree.configuration());
    }
}
