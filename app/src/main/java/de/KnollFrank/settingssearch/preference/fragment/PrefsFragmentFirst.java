package de.KnollFrank.settingssearch.preference.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.common.graph.ValueGraph;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.common.Locales;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinTrees;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphs;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.settingssearch.Configuration;
import de.KnollFrank.settingssearch.ConfigurationBundleConverter;
import de.KnollFrank.settingssearch.ConfigurationProvider;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.SettingsActivity;
import de.KnollFrank.settingssearch.SettingsActivity3;
import de.KnollFrank.settingssearch.SettingsSearchApplication;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;

public class PrefsFragmentFirst extends PreferenceFragmentCompat implements OnPreferenceClickListener {

    public static final String NON_STANDARD_LINK_TO_SECOND_FRAGMENT = "non_standard_link_to_second_fragment";
    public static final String KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER = "keyOfPreferenceWithOnPreferenceClickListener";
    public static final String SUMMARY_CHANGING_PREFERENCE_KEY = "summaryChangingPreference";

    private final @IdRes int DUMMY_FRAGMENT_CONTAINER_VIEW = View.generateViewId();

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                (ViewGroup) view,
                DUMMY_FRAGMENT_CONTAINER_VIEW);
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final Locale locale = Locales.getCurrentLanguageLocale(getResources());
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        {
            final Preference preference = getPreferenceScreen().findPreference(NON_STANDARD_LINK_TO_SECOND_FRAGMENT);
            preference.setIcon(R.drawable.face);
            markExtrasOfPreferenceConnectingSrcWithDst(preference, this, PrefsFragmentSecond.class);
        }
        getPreferenceScreen().findPreference("preferenceWithIntent").setIntent(createIntent(SettingsActivity.class, createExtrasForSettingsActivity()));
        getPreferenceScreen().findPreference("preferenceWithIntent3").setIntent(new Intent(getContext(), SettingsActivity3.class));
        configureSummaryChangingPreference(locale);
        setOnPreferenceClickListeners();
    }

    public static void markExtrasOfPreferenceConnectingSrcWithDst(final Preference preference,
                                                                  final PreferenceFragmentCompat src,
                                                                  final Class<?> dst) {
        preference.getExtras().putBoolean(
                preference.getKey() + ": " + src.getClass().getName() + " -> " + dst.getName(),
                true);
    }

    private SearchablePreferenceScreenTree getPojoGraph(final Locale locale) {
        return getPreferencesDatabase()
                .searchablePreferenceScreenGraphRepository()
                .findGraphById(locale, null, requireActivity())
                .orElseThrow();
    }

    private void configureSummaryChangingPreference(final Locale locale) {
        final SwitchPreference summaryChangingPreference = getPreferenceScreen().findPreference(SUMMARY_CHANGING_PREFERENCE_KEY);
        summaryChangingPreference.setSummary(getSummary(summaryChangingPreference.isChecked()));
        summaryChangingPreference.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(final @NonNull Preference preference, final Object checked) {
                        return onPreferenceChange(preference, (boolean) checked);
                    }

                    private boolean onPreferenceChange(final @NonNull Preference preference, final boolean checked) {
                        final SearchablePreferenceScreenTree pojoGraph = getPojoGraph(locale);
                        setSummaryOfPreferences(
                                preference,
                                getSummaryChangingPreference(pojoGraph.tree()),
                                getSummary(checked));
                        getPreferencesDatabase()
                                .searchablePreferenceScreenGraphRepository()
                                .persistOrReplace(
                                        getGraphHavingConfiguration(
                                                pojoGraph,
                                                new Configuration(
                                                        ConfigurationProvider
                                                                .getActualConfiguration(requireContext())
                                                                .addPreferenceToPreferenceFragmentWithSinglePreference(),
                                                        checked)));
                        return true;
                    }

                    private SearchablePreferenceScreenTree getGraphHavingConfiguration(final SearchablePreferenceScreenTree graph,
                                                                                       final Configuration configuration) {
                        return graph.asGraphHavingConfiguration(new ConfigurationBundleConverter().convertForward(configuration));
                    }

                    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
                    private SearchablePreferenceOfHostWithinTree getSummaryChangingPreference(final Tree<SearchablePreferenceScreen, SearchablePreference, ? extends ValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoGraph) {
                        return SearchablePreferenceWithinTrees
                                .findPreferenceByKey(
                                        PojoGraphs.getPreferences(pojoGraph),
                                        SUMMARY_CHANGING_PREFERENCE_KEY)
                                .orElseThrow();
                    }

                    private void setSummaryOfPreferences(final Preference preference,
                                                         final SearchablePreferenceOfHostWithinTree searchablePreference,
                                                         final String summary) {
                        preference.setSummary(summary);
                        searchablePreference.searchablePreference().setSummary(Optional.of(summary));
                    }
                });
    }

    private PreferencesDatabase<Configuration> getPreferencesDatabase() {
        return SettingsSearchApplication
                .getInstanceFromContext(requireContext())
                .preferencesDatabaseManager
                .getPreferencesDatabase();
    }

    public static String getSummary(final boolean checked) {
        return checked ?
                "summaryChangingPreference is ON" :
                "summaryChangingPreference is OFF";
    }

    private Intent createIntent(final Class<? extends Activity> activityClass, final Bundle extras) {
        final Intent intent = new Intent(getContext(), activityClass);
        intent.putExtras(extras);
        return intent;
    }

    public static Bundle createExtrasForSettingsActivity() {
        final Bundle bundle = new Bundle();
        bundle.putString(SettingsActivity.SETTINGS_ACTIVITY_MANDATORY_DUMMY_KEY, "some mandatory dummy value");
        return bundle;
    }

    @Override
    public void onDisplayPreferenceDialog(final Preference preference) {
        if (preference instanceof CustomDialogPreference) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public boolean onPreferenceClick(@NonNull final Preference preference) {
        if (KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER.equals(preference.getKey())) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
            return true;
        }
        if (NON_STANDARD_LINK_TO_SECOND_FRAGMENT.equals(preference.getKey())) {
            show(PrefsFragmentSecond.class.getName(), preference.getExtras(), this);
            return true;
        }
        return false;
    }

    private void setOnPreferenceClickListeners() {
        Stream
                .of(
                        KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER,
                        NON_STANDARD_LINK_TO_SECOND_FRAGMENT)
                .<Preference>map(this::findPreference)
                .forEach(preference -> preference.setOnPreferenceClickListener(this));
    }

    // adapted from PreferenceFragmentCompat.onPreferenceTreeClick()
    public static void show(final String classNameOfFragment2Show,
                            final Bundle arguments,
                            final Fragment targetFragment) {
        final FragmentManager fragmentManager = targetFragment.getParentFragmentManager();
        final Fragment fragment2Show =
                fragmentManager
                        .getFragmentFactory()
                        .instantiate(
                                targetFragment.requireActivity().getClassLoader(),
                                classNameOfFragment2Show);
        fragment2Show.setArguments(arguments);
        fragment2Show.setTargetFragment(targetFragment, 0);
        fragmentManager.beginTransaction()
                // Attempt to replace this fragment in its root view - developers should
                // implement onPreferenceStartFragment in their activity so that they can
                // customize this behaviour and handle any transitions between fragments
                .replace(((View) targetFragment.requireView().getParent()).getId(), fragment2Show)
                .addToBackStack(null)
                .commit();
    }
}
