package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createScreen;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import com.google.common.graph.ImmutableValueGraph;

import java.util.Locale;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;

public class TestTreeCreator<C> implements SearchablePreferenceScreenTreeCreator<C> {

    @Override
    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> createTree(
            final Locale locale,
            final C targetConfiguration,
            final FragmentActivity activityContext) {
        return new Tree<>(
                SearchablePreferenceScreenGraphTestFactory
                        .createGraphBuilder()
                        .addNode(createScreen("empty screen", Set.of()))
                        .build());
    }

    @Override
    public PersistableBundle getParams() {
        return new PersistableBundle();
    }
}
