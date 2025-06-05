package de.KnollFrank.settingssearch.preference.fragment;

import static de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference.ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.SubtreeReplacer;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProviderFactory;
import de.KnollFrank.settingssearch.PreferenceSearchExample;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.SearchDatabaseConfigFactory;
import de.KnollFrank.settingssearch.SettingsActivity;
import de.KnollFrank.settingssearch.SettingsActivity3;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;

public class PrefsFragmentFirst extends PreferenceFragmentCompat implements OnPreferenceClickListener {

    public static final String BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS = "summaryOfSrcPreferenceWithExtras";
    public static final String BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "summaryOfSrcPreferenceWithoutExtras";
    public static final String KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "keyOfSrcPreferenceWithoutExtras";
    public static final String NON_STANDARD_LINK_TO_SECOND_FRAGMENT = "non_standard_link_to_second_fragment";
    public static final String ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY = "addPreferenceToPreferenceFragmentWithSinglePreferenceKey";
    private static final String KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER = "keyOfPreferenceWithOnPreferenceClickListener";
    public static final String SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS = "summary of src preference with extras";
    public static final String SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "summary of src preference without extras";
    public static final String SUMMARY_CHANGING_PREFERENCE_KEY = "summaryChangingPreference";

    private final @IdRes int DUMMY_FRAGMENT_CONTAINER_VIEW = View.generateViewId();

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        getPreferenceScreen().addPreference(createAddPreferenceToP1CheckBoxPreference());
        getPreferenceScreen().addPreference(createPreferenceWithoutExtrasConnectedToPreferenceFragmentWithSinglePreference());
        getPreferenceScreen().addPreference(createPreferenceWithExtrasConnectedToPreferenceFragmentWithSinglePreference());
        {
            final Preference preference = getPreferenceScreen().findPreference(NON_STANDARD_LINK_TO_SECOND_FRAGMENT);
            preference.setIcon(R.drawable.face);
            markExtrasOfPreferenceConnectingSrcWithDst(preference, this, PrefsFragmentSecond.class);
        }
        getPreferenceScreen().findPreference("preferenceWithIntent").setIntent(createIntent(SettingsActivity.class, createExtrasForSettingsActivity()));
        getPreferenceScreen().findPreference("preferenceWithIntent3").setIntent(new Intent(getContext(), SettingsActivity3.class));
        configureSummaryChangingPreference();
        setOnPreferenceClickListeners();
    }

    public static void markExtrasOfPreferenceConnectingSrcWithDst(final Preference preference,
                                                                  final PreferenceFragmentCompat src,
                                                                  final Class<?> dst) {
        preference.getExtras().putBoolean(
                preference.getKey() + ": " + src.getClass().getName() + " -> " + dst.getName(),
                true);
    }

    private CheckBoxPreference createAddPreferenceToP1CheckBoxPreference() {
        final CheckBoxPreference checkBoxPreference = new CheckBoxPreference(requireContext());
        checkBoxPreference.setKey(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY);
        checkBoxPreference.setTitle("add preference to P1");
        checkBoxPreference.setOnPreferenceClickListener(
                new OnPreferenceClickListener() {

                    private final SubtreeReplacer<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> subtreeReplacer =
                            new SubtreeReplacer<>(
                                    () -> new DefaultDirectedGraph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge>(SearchablePreferenceEntityEdge.class),
                                    edge -> new SearchablePreferenceEntityEdge(edge.preference));

                    @Override
                    public boolean onPreferenceClick(@NonNull final Preference preference) {
                        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> newPojoGraph =
                                subtreeReplacer.replaceSubtreeWithTree(
                                        getPojoGraph(),
                                        getPojoScreenRootedAt(PrefsFragmentFirst.this),
                                        getPojoGraphRootedAt(
                                                // FK-TODO: dieser PreferenceScreenWithHost muß über einen SearchablePreferenceScreen berechnet werden können. Führe dazu eine neue Klasse ein.
                                                new PreferenceScreenWithHost(
                                                        getPreferenceScreen(),
                                                        PrefsFragmentFirst.this)));
                        getAppDatabase().searchablePreferenceScreenGraphDAO().persist(newPojoGraph);
                        return true;
                    }

                    private Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> getPojoGraph() {
                        return getAppDatabase()
                                .searchablePreferenceScreenGraphDAO()
                                .load();
                    }

                    private SearchablePreferenceScreenEntity getPojoScreenRootedAt(final PrefsFragmentFirst preferenceFragment) {
                        return Iterables.getOnlyElement(
                                getAppDatabase()
                                        .searchablePreferenceScreenEntityDAO()
                                        .findSearchablePreferenceScreensByHost(preferenceFragment.getClass()));
                    }

                    private Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> getPojoGraphRootedAt(final PreferenceScreenWithHost root) {
                        return SearchablePreferenceScreenGraphProviderFactory
                                .createSearchablePreferenceScreenGraphProvider(
                                        PrefsFragmentFirst.this,
                                        getAppDatabase().searchablePreferenceEntityDAO(),
                                        DUMMY_FRAGMENT_CONTAINER_VIEW,
                                        SearchDatabaseConfigFactory.createSearchDatabaseConfig())
                                .getSearchablePreferenceScreenGraph(root);
                    }
                });
        return checkBoxPreference;
    }

    private void configureSummaryChangingPreference() {
        final SwitchPreference summaryChangingPreference = getPreferenceScreen().findPreference(SUMMARY_CHANGING_PREFERENCE_KEY);
        summaryChangingPreference.setSummary(getSummary(summaryChangingPreference.isChecked()));
        summaryChangingPreference.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(@NonNull final Preference preference, final Object checked) {
                        setSummary(preference, getSummary((boolean) checked));
                        return true;
                    }

                    private void setSummary(final Preference preference, final String summary) {
                        preference.setSummary(summary);
                        setSummary(
                                this
                                        .findSearchablePreference(PrefsFragmentFirst.class, preference.getKey())
                                        .orElseThrow(),
                                summary);
                    }

                    private Optional<SearchablePreferenceEntity> findSearchablePreference(final Class<? extends PreferenceFragmentCompat> hostOfPreference, final String keyOfPreference) {
                        return SearchablePreferences.findPreferenceByKey(
                                getPreferences(hostOfPreference),
                                keyOfPreference);
                    }

                    private Set<SearchablePreferenceEntity> getPreferences(final Class<? extends PreferenceFragmentCompat> host) {
                        return Iterables
                                .getOnlyElement(
                                        getAppDatabase()
                                                .searchablePreferenceScreenEntityDAO()
                                                .findSearchablePreferenceScreensByHost(host))
                                .getAllPreferences(getAppDatabase().searchablePreferenceScreenEntityDAO());
                    }

                    private void setSummary(final SearchablePreferenceEntity searchablePreference, final String summary) {
                        searchablePreference.setSummary(Optional.of(summary));
                        getAppDatabase().searchablePreferenceEntityDAO().update(searchablePreference);
                    }
                });
    }

    private DAOProvider getAppDatabase() {
        return ((PreferenceSearchExample) requireActivity())
                .getAppDatabase()
                .orElseThrow();
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
            show(PrefsFragmentSecond.class.getName(), preference.getExtras());
            return true;
        }
        if (KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS.equals(preference.getKey())) {
            show(
                    PreferenceFragmentWithSinglePreference.class.getName(),
                    createArguments4PreferenceWithoutExtras(preference, requireContext()));
            return true;
        }
        return false;
    }

    public static Bundle createArguments4PreferenceWithoutExtras(final @NonNull Preference preference,
                                                                 final Context context) {
        return createArguments4PreferenceWithoutExtras(preference.getSummary().toString(), context);
    }

    private static Bundle createArguments4PreferenceWithoutExtras(final String summary, final Context context) {
        final Bundle arguments = new Bundle();
        arguments.putString(BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS, summary);
        arguments.putBoolean(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE, PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY, false));
        return arguments;
    }

    private Preference createPreferenceWithExtrasConnectedToPreferenceFragmentWithSinglePreference() {
        final Preference preference = new Preference(requireContext());
        preference.setFragment(PreferenceFragmentWithSinglePreference.class.getName());
        preference.setTitle("preference with extras from src to dst");
        preference.setKey("keyOfSrcPreferenceWithExtras");
        final String summary = SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS;
        preference.setSummary(summary);
        preference.getExtras().putString(BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS, summary);
        return preference;
    }

    private Preference createPreferenceWithoutExtrasConnectedToPreferenceFragmentWithSinglePreference() {
        final Preference preference = new Preference(requireContext());
        preference.setFragment(PreferenceFragmentWithSinglePreference.class.getName());
        preference.setTitle("P1: preference without extras from src to dst");
        preference.setKey(KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS);
        preference.setSummary(SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS);
        return preference;
    }

    private void setOnPreferenceClickListeners() {
        Stream
                .of(
                        KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER,
                        NON_STANDARD_LINK_TO_SECOND_FRAGMENT,
                        KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS)
                .<Preference>map(this::findPreference)
                .forEach(preference -> preference.setOnPreferenceClickListener(this));
    }

    // adapted from PreferenceFragmentCompat.onPreferenceTreeClick()
    private void show(final String classNameOfFragment2Show, final Bundle arguments) {
        final FragmentManager fragmentManager = getParentFragmentManager();
        final Fragment fragment =
                fragmentManager.getFragmentFactory().instantiate(
                        requireActivity().getClassLoader(),
                        classNameOfFragment2Show);
        fragment.setArguments(arguments);
        fragment.setTargetFragment(this, 0);
        fragmentManager.beginTransaction()
                // Attempt to replace this fragment in its root view - developers should
                // implement onPreferenceStartFragment in their activity so that they can
                // customize this behaviour and handle any transitions between fragments
                .replace(((View) requireView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit();
    }
}
