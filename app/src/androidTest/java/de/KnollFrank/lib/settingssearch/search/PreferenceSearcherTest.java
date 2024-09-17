package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static de.KnollFrank.lib.settingssearch.search.provider.BuiltinPreferenceDescriptionsFactory.createBuiltinPreferenceDescriptions;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PreferenceScreensProvider;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.MergedPreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreensMerger;
import de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescription;
import de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescriptions;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProviderInternal;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.settingssearch.preference.fragment.CustomDialogFragment;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;
import de.KnollFrank.settingssearch.test.TestActivity;

public class PreferenceSearcherTest {

	@Test
	public void shouldSearchAndFindTitle() {
		final String keyword = "fourth";
		final String keyOfPreference = "fourthfile";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final CheckBoxPreference preference = new CheckBoxPreference(context);
							preference.setKey(keyOfPreference);
							preference.setTitle(String.format("Checkbox %s file", keyword));
							return preference;
						}),
				(preference, host) -> true,
				keyword,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndFindTitleOfPreferenceCategory() {
		final String keyword = "This is a preference category";
		final String keyOfPreference = "keyOfPreferenceCategory";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final PreferenceCategory preference = new PreferenceCategory(context);
							preference.setKey(keyOfPreference);
							preference.setTitle(String.format(keyword));
							return preference;
						}),
				(preference, host) -> true,
				keyword,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndNotFindNonSearchablePreference() {
		final String keyword = "fourth";
		final String keyOfPreference = "fourthfile";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final CheckBoxPreference preference = new CheckBoxPreference(context);
							preference.setKey(keyOfPreference);
							preference.setTitle(String.format("Checkbox %s file", keyword));
							return preference;
						}),
				(preference, host) -> !keyOfPreference.equals(preference.getKey()),
				keyword,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								not(hasItem(keyOfPreference))));
	}

	@Test
	public void shouldSearchAndFindSummary() {
		final String keyword = "fourth";
		final String keyOfPreference = "fourthfile";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final CheckBoxPreference preference = new CheckBoxPreference(context);
							preference.setKey(keyOfPreference);
							preference.setSummary(String.format("Checkbox %s file", keyword));
							return preference;
						}),
				(preference, host) -> true,
				keyword,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndFindListPreference_entries() {
		final String keyword = "entry of some ListPreference";
		final String keyOfPreference = "keyOfSomeListPreference";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final ListPreference preference = new ListPreference(context);
							preference.setKey(keyOfPreference);
							preference.setSummary("This allows to select from a list");
							preference.setTitle("Select list preference");
							preference.setEntryValues(new String[]{"some entry value"});
							preference.setEntries(new String[]{keyword});
							return preference;
						}),
				(preference, host) -> true,
				keyword,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndFindListPreference_dialogTitle() {
		final String keyword = "this is the dialog title";
		final String keyOfPreference = "keyOfSomeListPreference";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final ListPreference preference = new ListPreference(context);
							preference.setKey(keyOfPreference);
							preference.setSummary("This allows to select from a list");
							preference.setTitle("Select list preference");
							preference.setEntries(new String[]{"some entry"});
							preference.setEntryValues(new String[]{"some entry value"});
							preference.setDialogTitle(keyword);
							return preference;
						}),
				(preference, host) -> true,
				keyword,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndFindSwitchPreference_summaryOff() {
		final String summaryOff = "switch is off";
		final String keyOfPreference = "keyOfSomeSwitchPreference";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final SwitchPreference preference = new SwitchPreference(context);
							preference.setKey(keyOfPreference);
							preference.setSummary("This allows to use a switch");
							preference.setSummaryOff(summaryOff);
							preference.setSummaryOn("switch is on");
							return preference;
						}),
				(preference, host) -> true,
				summaryOff,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndFindSwitchPreference_summaryOn() {
		final String summaryOn = "switch is on";
		final String keyOfPreference = "keyOfSomeSwitchPreference";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final SwitchPreference preference = new SwitchPreference(context);
							preference.setKey(keyOfPreference);
							preference.setSummary("This allows to use a switch");
							preference.setSummaryOff("switch is off");
							preference.setSummaryOn(summaryOn);
							return preference;
						}),
				(preference, host) -> true,
				summaryOn,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndFindCustomReversedListPreferenceViaReversedKeyword() {
		final String keyword = "Windows Live";
		final String keyOfPreference = "keyOfReversedListPreference";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final ReversedListPreference preference = new ReversedListPreference(context);
							preference.setKey(keyOfPreference);
							preference.setSummary("summary of ReversedListPreference");
							preference.setTitle("title of ReversedListPreference");
							preference.setEntryValues(new String[]{"some entry value"});
							preference.setEntries(new String[]{keyword});
							return preference;
						}),
				(preference, host) -> true,
				ReversedListPreference.getReverse(keyword).toString(),
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndFindInCustomDialogPreference() {
		final String keyword = "some text in a custom dialog";
		final String keyOfPreference = "keyOfCustomDialogPreference";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final CustomDialogPreference preference = new CustomDialogPreference(context);
							preference.setKey(keyOfPreference);
							preference.setSummary("summary of CustomDialogPreference");
							preference.setTitle("title of CustomDialogPreference");
							return preference;
						}),
				(preference, host) -> true,
				keyword,
				(hostOfPreference, preference) ->
						preference instanceof CustomDialogPreference || "keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey()) ?
								Optional.of(
										new PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<>(
												new CustomDialogFragment(),
                                                CustomDialogFragment::getSearchableInfo)) :
								Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndFindInPreferenceWithOnPreferenceClickListener() {
		final String keyword = "some text in a custom dialog";
		final String keyOfPreference = "keyOfPreferenceWithOnPreferenceClickListener";
		testSearch(
				new PrefsFragmentFirst(),
				(preference, host) -> true,
				keyword,
				(hostOfPreference, preference) ->
						preference instanceof CustomDialogPreference || "keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey()) ?
								Optional.of(
										new PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<>(
												new CustomDialogFragment(),
                                                CustomDialogFragment::getSearchableInfo)) :
								Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndFindMultiSelectListPreference() {
		final String keyword = "entry of some MultiSelectListPreference";
		final String keyOfPreference = "keyOfSomeMultiSelectListPreference";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final MultiSelectListPreference preference = new MultiSelectListPreference(context);
							preference.setKey(keyOfPreference);
							preference.setSummary("This allows to select multiple entries from a list");
							preference.setTitle("Multi select list preference");
							preference.setEntries(new String[]{keyword});
							preference.setEntryValues(new String[]{"some entry value"});
							return preference;
						}),
				(preference, host) -> true,
				keyword,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndFindMultiSelectListPreference_dialogTitle() {
		final String keyword = "dialog title of some MultiSelectListPreference";
		final String keyOfPreference = "keyOfSomeMultiSelectListPreference";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final MultiSelectListPreference preference = new MultiSelectListPreference(context);
							preference.setKey(keyOfPreference);
							preference.setSummary("This allows to select multiple entries from a list");
							preference.setTitle("Multi select list preference");
							preference.setEntryValues(new String[]{"some entry value"});
							preference.setEntries(new String[]{"some entry"});
							preference.setDialogTitle(keyword);
							return preference;
						}),
				(preference, host) -> true,
				keyword,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								hasItem(keyOfPreference)));
	}

	@Test
	public void shouldSearchAndNotFind() {
		final String keyword = "non_existing_keyword";
		final String keyOfPreference = "fourthfile";
		testSearch(
				PreferenceFragment.fromSinglePreference(
						context -> {
							final CheckBoxPreference preference = new CheckBoxPreference(context);
							preference.setKey(keyOfPreference);
							preference.setSummary("This checkbox is a preference coming from a fourth file");
							preference.setTitle("Checkbox fourth file");
							return preference;
						}),
				(preference, host) -> true,
				keyword,
				(hostOfPreference, preference) -> Optional.empty(),
				preferenceMatches ->
						assertThat(
								getKeys(preferenceMatches),
								not(hasItem(keyOfPreference))));
	}

	@Test
	public void shouldSearchAndFindPreferenceWithTwoDifferentPreferencePaths() {
		PreferenceSearcherTestCaseTwoDifferentPreferencePaths.shouldSearchAndFindPreferenceWithTwoDifferentPreferencePaths();
	}

	static void testSearch(final PreferenceFragmentCompat preferenceFragment,
						   final IsPreferenceSearchable isPreferenceSearchable,
						   final String keyword,
						   final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
						   final Consumer<List<PreferenceMatch>> checkPreferenceMatches) {
		try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
			scenario.onActivity(fragmentActivity -> {
				// Given
				final MergedPreferenceScreen mergedPreferenceScreen =
						getMergedPreferenceScreen(
								preferenceFragment,
								isPreferenceSearchable,
								fragmentActivity,
								preferenceDialogAndSearchableInfoProvider,
								createFragmentFactoryReturning(preferenceFragment));
				final PreferenceSearcher preferenceSearcher =
						new PreferenceSearcher(
								mergedPreferenceScreen,
								new SearchableInfoAttribute(),
								getSearchableInfoProviderInternal(
										mergedPreferenceScreen,
										ImmutableList
												.<PreferenceDescription<? extends Preference>>builder()
												.addAll(createBuiltinPreferenceDescriptions())
												.add(new PreferenceDescription<>(
														ReversedListPreference.class,
														new ReversedListPreferenceSearchableInfoProvider()))
												.build()));

				// When
				final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(keyword);

				// Then
				checkPreferenceMatches.accept(preferenceMatches);
			});
		}
	}

	private static FragmentFactory createFragmentFactoryReturning(final PreferenceFragmentCompat preferenceFragment) {
		final DefaultFragmentFactory defaultFragmentFactory = new DefaultFragmentFactory();
		return (fragmentClassName, src, context) ->
				preferenceFragment.getClass().getName().equals(fragmentClassName) ?
						preferenceFragment :
						defaultFragmentFactory.instantiate(fragmentClassName, src, context);
	}

	private static MergedPreferenceScreen getMergedPreferenceScreen(
			final PreferenceFragmentCompat preferenceFragment,
			final IsPreferenceSearchable isPreferenceSearchable,
			final FragmentActivity fragmentActivity,
			final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
			final FragmentFactory fragmentFactory) {
		final DefaultFragmentInitializer defaultFragmentInitializer =
				new DefaultFragmentInitializer(
						fragmentActivity.getSupportFragmentManager(),
						TestActivity.FRAGMENT_CONTAINER_VIEW);
		final Fragments fragments =
				new Fragments(
						new FragmentFactoryAndInitializerWithCache(
								new FragmentFactoryAndInitializer(
										fragmentFactory,
										defaultFragmentInitializer)),
						fragmentActivity);
		final MergedPreferenceScreenProvider mergedPreferenceScreenProvider =
				new MergedPreferenceScreenProvider(
						fragments,
						defaultFragmentInitializer,
						new PreferenceScreensProvider(new PreferenceScreenWithHostProvider(fragments)),
						new PreferenceScreensMerger(fragmentActivity),
						isPreferenceSearchable,
						new SearchableInfoAttribute(),
						preferenceDialogAndSearchableInfoProvider,
						preferenceScreenGraph -> {
						},
						false);
		return mergedPreferenceScreenProvider.getMergedPreferenceScreen(preferenceFragment.getClass().getName());
	}

	private static SearchableInfoProviderInternal getSearchableInfoProviderInternal(
			final MergedPreferenceScreen mergedPreferenceScreen,
			final List<PreferenceDescription<? extends Preference>> preferenceDescriptions) {
		return new SearchableInfoProviderInternal(
				Maps.merge(
						ImmutableList.of(
								PreferenceDescriptions.getSearchableInfoProviderByPreferenceClass(preferenceDescriptions),
								PreferenceDescriptions.getSearchableInfoProviderByPreferenceClass(mergedPreferenceScreen.getPreferenceDescriptions())),
						SearchableInfoProvider::mergeWith));
	}

	static List<String> getKeys(final List<PreferenceMatch> preferenceMatches) {
		return preferenceMatches
				.stream()
				.map(PreferenceMatch::preference)
				.map(Preference::getKey)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}