package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.codepoetics.ambivalence.Either;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescription;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescriptionEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

public class TreeProcessorDao<C> {

    private final TreeProcessorDescriptionEntityDao treeProcessorDescriptionEntityDao;
    private final Converter<TreeProcessorDescription<C>, Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> treeProcessorDescriptionConverter;

    public TreeProcessorDao(final TreeProcessorDescriptionEntityDao treeProcessorDescriptionEntityDao,
                            final Converter<TreeProcessorDescription<C>, Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> treeProcessorDescriptionConverter) {
        this.treeProcessorDescriptionEntityDao = treeProcessorDescriptionEntityDao;
        this.treeProcessorDescriptionConverter = treeProcessorDescriptionConverter;
    }

    public List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> getTreeProcessors() {
        return treeProcessorDescriptionEntityDao
                .getAll()
                .stream()
                .map(treeProcessorDescriptionEntity ->
                             treeProcessorDescriptionConverter.convertForward(
                                     treeProcessorDescriptionEntity.treeProcessorDescription()))
                .collect(Collectors.toList());
    }

    public void addTreeCreator(final SearchablePreferenceScreenTreeCreator<C> treeCreator) {
        removeTreeProcessors();
        treeProcessorDescriptionEntityDao.persist(
                TreeProcessorDescriptionEntity.of(
                        treeProcessorDescriptionConverter.convertBackward(
                                Either.ofLeft(treeCreator))));
    }

    public void addTreeTransformer(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        treeProcessorDescriptionEntityDao.persist(
                TreeProcessorDescriptionEntity.of(
                        treeProcessorDescriptionConverter.convertBackward(
                                Either.ofRight(treeTransformer))));
    }

    public void removeTreeProcessors() {
        treeProcessorDescriptionEntityDao.deleteAll();
    }

    public boolean hasTreeProcessors() {
        return !getTreeProcessors().isEmpty();
    }
}
