package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.EitherMatchers.isLeft;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.EitherMatchers.isRight;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TestTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TestTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorFactoryTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.TreeProcessorDescriptionConverter;

@RunWith(RobolectricTestRunner.class)
// FK-TODO: refine tests
public class TreeProcessorDaoTest<C> extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldReturnEmptyListWhenNoProcessorsAreAdded() {
        // Given
        final TreeProcessorDao<C> treeProcessorDao = createTreeProcessorDao();

        // When
        final var treeProcessors = treeProcessorDao.getTreeProcessors();

        // Then
        assertThat(treeProcessors, is(empty()));
    }

    @Test
    public void shouldAddTreeCreator() {
        // Given
        final TreeProcessorDao<C> treeProcessorDao = createTreeProcessorDao();
        final SearchablePreferenceScreenTreeCreator<C> treeCreator = new TestTreeCreator<>();

        // When
        treeProcessorDao.addTreeCreator(treeCreator);

        // Then
        final List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> processors = treeProcessorDao.getTreeProcessors();
        assertThat(processors, is(not(empty())));
        assertThat(processors.get(0), isLeft());
    }

    @Test
    public void shouldAddTreeTransformer() {
        // Given
        final TreeProcessorDao<C> treeProcessorDao = createTreeProcessorDao();
        final SearchablePreferenceScreenTreeTransformer<C> treeTransformer = new TestTreeTransformer<>();

        // When
        treeProcessorDao.addTreeTransformer(treeTransformer);

        // Then
        final List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> processors = treeProcessorDao.getTreeProcessors();
        assertThat(processors, is(not(empty())));
        assertThat(processors.get(0), isRight());
    }

    @Test
    public void shouldRemoveAllTreeProcessors() {
        // Given
        final TreeProcessorDao<C> treeProcessorDao = createTreeProcessorDao();
        treeProcessorDao.addTreeCreator(new TestTreeCreator<>());
        treeProcessorDao.addTreeTransformer(new TestTreeTransformer<>());

        // When
        treeProcessorDao.removeTreeProcessors();

        // Then
        assertThat(treeProcessorDao.getTreeProcessors(), is(empty()));
    }

    @Test
    public void shouldIndicateWhetherItHasTreeProcessors() {
        // Given
        final TreeProcessorDao<C> treeProcessorDao = createTreeProcessorDao();
        assertThat(treeProcessorDao.hasTreeProcessors(), is(false));

        // When
        treeProcessorDao.addTreeCreator(new TestTreeCreator<>());

        // Then
        assertThat(treeProcessorDao.hasTreeProcessors(), is(true));

        // When
        treeProcessorDao.removeTreeProcessors();

        // Then
        assertThat(treeProcessorDao.hasTreeProcessors(), is(false));
    }

    private TreeProcessorDao<C> createTreeProcessorDao() {
        return new TreeProcessorDao<>(
                preferencesRoomDatabase.treeProcessorDescriptionEntityDao(),
                new TreeProcessorDescriptionConverter<>(TreeProcessorFactoryTestFactory.createTreeProcessorFactory()));
    }
}
