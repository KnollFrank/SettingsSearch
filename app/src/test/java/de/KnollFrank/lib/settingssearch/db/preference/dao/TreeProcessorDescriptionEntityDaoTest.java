package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.TreeProcessorDescriptionEntityMatchers.hasSameContentAs;

import android.os.PersistableBundle;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TestTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescription;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescriptionEntity;
import de.KnollFrank.settingssearch.Configuration;

@RunWith(RobolectricTestRunner.class)
public class TreeProcessorDescriptionEntityDaoTest extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldPersistAndGet() {
        // Given
        final TreeProcessorDescriptionEntityDao treeProcessorDescriptionEntityDao = preferencesRoomDatabase.treeProcessorDescriptionEntityDao();
        final TreeProcessorDescriptionEntity entity = createSomeTreeProcessorDescriptionEntity();

        // When
        treeProcessorDescriptionEntityDao.persist(entity);

        // Then
        final List<TreeProcessorDescriptionEntity> all = treeProcessorDescriptionEntityDao.getAll();
        assertThat(all, contains(hasSameContentAs(entity)));
    }

    @Test
    public void shouldDeleteAll() {
        // Given
        final TreeProcessorDescriptionEntityDao treeProcessorDescriptionEntityDao = preferencesRoomDatabase.treeProcessorDescriptionEntityDao();
        treeProcessorDescriptionEntityDao.persist(createSomeTreeProcessorDescriptionEntity());

        // When
        treeProcessorDescriptionEntityDao.deleteAll();

        // Then
        assertThat(treeProcessorDescriptionEntityDao.getAll(), is(empty()));
    }

    private static TreeProcessorDescriptionEntity createSomeTreeProcessorDescriptionEntity() {
        final TestTreeCreator<Configuration> testTreeCreator = new TestTreeCreator<>();
        final PersistableBundle params = createSomePersistableBundle();
        return TreeProcessorDescriptionEntity.of(
                new TreeProcessorDescription<>(
                        Either.ofLeft((Class<? extends SearchablePreferenceScreenTreeCreator<Configuration>>) testTreeCreator.getClass()),
                        params));
    }

    private static PersistableBundle createSomePersistableBundle() {
        final PersistableBundle params = new PersistableBundle();
        params.putString("key", "value");
        return params;
    }
}
