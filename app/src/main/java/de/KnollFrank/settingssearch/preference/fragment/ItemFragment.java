package de.KnollFrank.settingssearch.preference.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.InitializePreferenceFragmentWithFragmentBeforeOnCreate;
import de.KnollFrank.lib.settingssearch.results.ItemOfRecyclerViewHighlighter;
import de.KnollFrank.lib.settingssearch.results.PositionOfSettingProvider;
import de.KnollFrank.lib.settingssearch.results.Setting;
import de.KnollFrank.lib.settingssearch.results.SettingHighlighter;
import de.KnollFrank.lib.settingssearch.results.SettingHighlighterProvider;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.preference.custom.Iterables;
import de.KnollFrank.settingssearch.preference.fragment.placeholder.PlaceholderContent;

public class ItemFragment extends Fragment implements SettingHighlighterProvider, PositionOfSettingProvider {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public ItemFragment() {
    }

    @SuppressWarnings("unused")
    public static ItemFragment newInstance(final int columnCount) {
        final ItemFragment fragment = new ItemFragment();
        {
            final Bundle args = new Bundle();
            args.putInt(ARG_COLUMN_COUNT, columnCount);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        if (view instanceof final RecyclerView recyclerView) {
            final Context context = recyclerView.getContext();
            recyclerView.setLayoutManager(
                    mColumnCount <= 1 ?
                            new LinearLayoutManager(context) :
                            new GridLayoutManager(context, mColumnCount));
            recyclerView.setAdapter(new ItemRecyclerViewAdapter(getItems()));
        }
        return view;
    }

    public List<PlaceholderContent.PlaceholderItem> getItems() {
        return PlaceholderContent.ITEMS;
    }

    @Override
    public OptionalInt getPositionOfSetting(final Setting setting) {
        return Iterables.indexOf(
                getItems(),
                placeholderItem -> placeholderItem.key().equals(setting.getKey()));
    }

    @Override
    public SettingHighlighter getSettingHighlighter() {
        return new ItemOfRecyclerViewHighlighter((RecyclerView) getView(), this, Duration.ofSeconds(1));
    }

    public static class PreferenceFragment extends PreferenceFragmentCompat implements InitializePreferenceFragmentWithFragmentBeforeOnCreate<ItemFragment> {

        private List<PlaceholderContent.PlaceholderItem> items;

        @Override
        public void initializePreferenceFragmentWithFragmentBeforeOnCreate(final ItemFragment itemFragment) {
            items = itemFragment.getItems();
        }

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            screen.setTitle("screen title PreferenceFragment");
            screen.setSummary("screen summary");
            PreferenceFragment
                    .asPreferences(items, context)
                    .forEach(screen::addPreference);
            setPreferenceScreen(screen);
        }

        private static List<Preference> asPreferences(final List<PlaceholderContent.PlaceholderItem> items, final Context context) {
            return items
                    .stream()
                    .map(placeholderItem -> asPreference(placeholderItem, context))
                    .collect(Collectors.toList());
        }

        private static Preference asPreference(final PlaceholderContent.PlaceholderItem placeholderItem,
                                               final Context context) {
            final Preference preference = new Preference(context);
            preference.setKey(placeholderItem.key());
            preference.setTitle(placeholderItem.title());
            preference.setSummary(placeholderItem.summary());
            placeholderItem
                    .intentFactory()
                    .map(intentFactory -> intentFactory.apply(context))
                    .ifPresent(preference::setIntent);
            return preference;
        }
    }
}