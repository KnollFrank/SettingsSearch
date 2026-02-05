package de.KnollFrank.settingssearch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableMap;
import com.google.common.graph.ImmutableValueGraph;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivitySearchDatabaseConfigs;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PrincipalAndProxy;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeCreatorDescription;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeTransformerDescription;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.graph.GraphConverterFactory;
import de.KnollFrank.lib.settingssearch.graph.TreeBuilderListener;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializer;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnectedToPreferenceProvider;
import de.KnollFrank.settingssearch.SettingsActivity.SettingsFragment;
import de.KnollFrank.settingssearch.SettingsActivity2.SettingsFragment2;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.settingssearch.preference.fragment.CustomDialogFragment;
import de.KnollFrank.settingssearch.preference.fragment.ItemFragment;
import de.KnollFrank.settingssearch.preference.fragment.ItemFragment3;
import de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFifth;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentSecond;
import de.KnollFrank.settingssearch.preference.fragment.SearchDatabaseRootedAtPrefsFragmentFifthAdapter;

public class SearchDatabaseConfigFactory {

    private SearchDatabaseConfigFactory() {
    }

    public static SearchDatabaseConfig<Configuration> createSearchDatabaseConfig() {
        return SearchDatabaseConfig
                .builder(
                        new FragmentClassOfActivity<>(
                                PrefsFragmentFirst.class,
                                new ActivityDescription(
                                        PreferenceSearchExample.class,
                                        new PersistableBundle())),
                        new TreeProcessorFactory<Configuration>() {

                            @Override
                            public SearchablePreferenceScreenTreeCreator<Configuration> createTreeCreator(final TreeCreatorDescription<Configuration> treeCreatorDescription) {
                                throw new IllegalArgumentException(treeCreatorDescription.toString());
                            }

                            @Override
                            public SearchablePreferenceScreenTreeTransformer<Configuration> createTreeTransformer(final TreeTransformerDescription<Configuration> treeTransformerDescription) {
                                if (SearchDatabaseRootedAtPrefsFragmentFifthAdapter.class.equals(treeTransformerDescription.treeTransformer())) {
                                    return new SearchDatabaseRootedAtPrefsFragmentFifthAdapter();
                                }
                                throw new IllegalArgumentException(treeTransformerDescription.toString());
                            }
                        })
                .withFragmentFactory(
                        new FragmentFactory() {

                            @Override
                            public <T extends Fragment> FragmentOfActivity<T> instantiate(
                                    final FragmentClassOfActivity<T> fragmentClass,
                                    final Optional<PreferenceOfHostOfActivity> src,
                                    final Context context,
                                    final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
                                if (PreferenceFragmentWithSinglePreference.class.equals(fragmentClass.fragment()) &&
                                        src.isPresent() &&
                                        PrefsFragmentFifth.KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS.equals(src.orElseThrow().preference().getKey())) {
                                    return new FragmentOfActivity<>(
                                            Classes.instantiateFragmentClass(
                                                    fragmentClass.fragment(),
                                                    Optional.of(PrefsFragmentFifth.createArguments4PreferenceWithoutExtras(src.orElseThrow().preference(), context))),
                                            fragmentClass.activityOfFragment());
                                } else if (ItemFragment3.class.equals(fragmentClass.fragment()) && src.isPresent() && "preferenceWithIntent3".equals(src.orElseThrow().preference().getKey())) {
                                    final Preference preference = src.orElseThrow().preference();
                                    PrefsFragmentFirst
                                            .markExtrasOfPreferenceConnectingSrcWithDst(
                                                    preference,
                                                    src.orElseThrow().hostOfPreference(),
                                                    fragmentClass.fragment());
                                    return new FragmentOfActivity<>(
                                            Classes.instantiateFragmentClass(
                                                    fragmentClass.fragment(),
                                                    Optional.of(preference.getExtras())),
                                            fragmentClass.activityOfFragment());
                                }
                                return new DefaultFragmentFactory().instantiate(fragmentClass, src, context, instantiateAndInitializeFragment);
                            }
                        })
                .withActivitySearchDatabaseConfigs(
                        new ActivitySearchDatabaseConfigs(
                                ImmutableMap
                                        .<Class<? extends Activity>, Class<? extends PreferenceFragmentCompat>>builder()
                                        .put(SettingsActivity.class, SettingsFragment.class)
                                        .put(SettingsActivity2.class, SettingsFragment2.class)
                                        .put(SettingsActivity3.class, ItemFragment3.PreferenceFragment3.class)
                                        .build(),
                                Set.of(
                                        new PrincipalAndProxy<>(ItemFragment.class, ItemFragment.PreferenceFragment.class),
                                        new PrincipalAndProxy<>(ItemFragment3.class, ItemFragment3.PreferenceFragment3.class))))
                .withActivityInitializerByActivity(
                        ImmutableMap
                                .<Class<? extends Activity>, ActivityInitializer<?>>builder()
                                .put(
                                        SettingsActivity.class,
                                        new ActivityInitializer<PrefsFragmentFirst>() {

                                            @Override
                                            public void beforeStartActivity(final PrefsFragmentFirst src) {
                                                Log.i(this.getClass().getSimpleName(), "beforeStartActivity called");
                                            }

                                            @Override
                                            public Optional<Bundle> createExtras(final PrefsFragmentFirst src) {
                                                return Optional.of(PrefsFragmentFirst.createExtrasForSettingsActivity());
                                            }
                                        })
                                .build())
                .withSearchableInfoProvider(new ReversedListPreferenceSearchableInfoProvider())
                .withPreferenceFragmentConnectedToPreferenceProvider(
                        new PreferenceFragmentConnectedToPreferenceProvider() {

                            @Override
                            public Optional<Class<? extends PreferenceFragmentCompat>> getPreferenceFragmentConnectedToPreference(
                                    final Preference preference,
                                    final PreferenceFragmentCompat hostOfPreference) {
                                return PrefsFragmentFirst.NON_STANDARD_LINK_TO_SECOND_FRAGMENT.equals(preference.getKey()) ?
                                        Optional.of(PrefsFragmentSecond.class) :
                                        Optional.empty();
                            }
                        })
                .withPreferenceDialogAndSearchableInfoProvider(
                        new PreferenceDialogAndSearchableInfoProvider() {

                            @Override
                            public Optional<PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<?>> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(
                                    final Preference preference,
                                    final PreferenceFragmentCompat hostOfPreference) {
                                return preference instanceof CustomDialogPreference || "keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey()) ?
                                        Optional.of(
                                                new PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<>(
                                                        new CustomDialogFragment(),
                                                        CustomDialogFragment::getSearchableInfo)) :
                                        Optional.empty();
                            }
                        })
                .withPreferenceScreenTreeBuilderListener(
                        new TreeBuilderListener<>() {

                            @Override
                            public void onStartBuildTree(final PreferenceScreenOfHostOfActivity treeRoot) {
                                Log.i(this.getClass().getSimpleName(), "onStartComputePreferences");
                            }

                            @Override
                            public void onStartBuildSubtree(final PreferenceScreenOfHostOfActivity subtreeRoot) {
                            }

                            @Override
                            public void onFinishBuildSubtree(final PreferenceScreenOfHostOfActivity subtreeRoot) {
                            }

                            @Override
                            @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
                            public void onFinishBuildTree(final Tree<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> preferenceScreenTree) {
                                Log.i(this.getClass().getSimpleName(), "onFinishComputePreferences");
                                Log.i(
                                        this.getClass().getSimpleName(),
                                        PreferenceScreenGraphToDOTConverter.graphToDOT(
                                                GraphConverterFactory
                                                        .createPreferenceScreenWithHostGraphConverter()
                                                        .toJGraphT(preferenceScreenTree.graph())));
                            }
                        })
                .build();
    }
}
