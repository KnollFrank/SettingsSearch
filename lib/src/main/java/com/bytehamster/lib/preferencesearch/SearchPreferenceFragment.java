package com.bytehamster.lib.preferencesearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bytehamster.lib.preferencesearch.common.Lists;
import com.bytehamster.lib.preferencesearch.ui.AnimationUtils;
import com.bytehamster.lib.preferencesearch.ui.RevealAnimationSetting;

import java.util.ArrayList;
import java.util.List;

public class SearchPreferenceFragment extends Fragment implements SearchPreferenceAdapter.SearchClickListener {

    public interface HistoryClickListener {

        void onHistoryEntryClicked(String entry);
    }

    /**
     * Default tag used on the library's Fragment transactions with {@link SearchPreferenceFragment}
     */
    public static final String TAG = "SearchPreferenceFragment";

    private static final String SHARED_PREFS_FILE = "preferenceSearch";
    private static final int MAX_HISTORY = 5;
    private PreferenceSearcher preferenceSearcher;
    private List<PreferenceItem> results;
    private List<HistoryItem> history;
    private SharedPreferences prefs;
    private SearchViewHolder viewHolder;
    private SearchConfiguration searchConfiguration;
    private SearchPreferenceAdapter adapter;
    private HistoryClickListener historyClickListener;
    private String searchTermPreset = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getContext().getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        final Bundle arguments = getArguments();
        searchConfiguration = SearchConfigurations.fromBundle(arguments);
        preferenceSearcher = new PreferenceSearcher(PreferenceItemsBundle.readPreferenceItems(arguments));
        loadHistory();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.searchpreference_fragment, container, false);
        viewHolder = new SearchViewHolder(rootView);

        viewHolder.clearButton.setOnClickListener(view -> viewHolder.searchView.setText(""));
        if (searchConfiguration.isHistoryEnabled()) {
            viewHolder.moreButton.setVisibility(View.VISIBLE);
        }
        if (searchConfiguration.getTextHint() != null) {
            viewHolder.searchView.setHint(searchConfiguration.getTextHint());
        }
        if (searchConfiguration.getTextNoResults() != null) {
            viewHolder.noResults.setText(searchConfiguration.getTextNoResults());
        }
        viewHolder.moreButton.setOnClickListener(
                view -> {
                    final PopupMenu popup = new PopupMenu(getContext(), viewHolder.moreButton);
                    popup.getMenuInflater().inflate(R.menu.searchpreference_more, popup.getMenu());
                    popup.setOnMenuItemClickListener(
                            item -> {
                                if (item.getItemId() == R.id.clear_history) {
                                    clearHistory();
                                }
                                return true;
                            });
                    if (searchConfiguration.getTextClearHistory() != null) {
                        popup.getMenu().findItem(R.id.clear_history).setTitle(searchConfiguration.getTextClearHistory());
                    }
                    popup.show();
                });
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchPreferenceAdapter();
        adapter.setSearchConfiguration(searchConfiguration);
        adapter.setOnItemClickListener(this);
        viewHolder.recyclerView.setAdapter(adapter);

        viewHolder.searchView.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(final Editable editable) {
                        updateSearchResults(editable.toString());
                        viewHolder.clearButton.setVisibility(as_VISIBLE_or_GONE(!editable.toString().isEmpty()));
                    }
                });

        if (!searchConfiguration.isSearchBarEnabled()) {
            viewHolder.cardView.setVisibility(View.GONE);
        }

        if (searchTermPreset != null) {
            viewHolder.searchView.setText(searchTermPreset);
        }

        final RevealAnimationSetting anim = searchConfiguration.getRevealAnimationSetting();
        if (anim != null) {
            AnimationUtils.registerCircularRevealAnimation(getContext(), rootView, anim);
        }
        rootView.setOnTouchListener((v, event) -> true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSearchResults(viewHolder.searchView.getText().toString());
        if (searchConfiguration.isSearchBarEnabled()) {
            showKeyboard();
        }
    }

    private void loadHistory() {
        history = new ArrayList<>();
        if (!searchConfiguration.isHistoryEnabled()) {
            return;
        }

        final int size = prefs.getInt(historySizeKey(), 0);
        for (int i = 0; i < size; i++) {
            final String title = prefs.getString(historyEntryKey(i), null);
            history.add(new HistoryItem(title));
        }
    }

    private void saveHistory() {
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(historySizeKey(), history.size());
        for (int i = 0; i < history.size(); i++) {
            editor.putString(historyEntryKey(i), history.get(i).getTerm());
        }
        editor.apply();
    }

    /**
     * Gets the preference key for the history size, prefixed with the history ID, if set.
     *
     * @return the preference key for the history size
     */
    private String historySizeKey() {
        if (searchConfiguration.getHistoryId() != null) {
            return searchConfiguration.getHistoryId() + "_history_size";
        } else {
            return "history_size";
        }
    }

    /**
     * Gets the preference key for a history entry, prefixed with the history ID, if set.
     *
     * @return the preference key for the history entry
     */
    private String historyEntryKey(int i) {
        if (searchConfiguration.getHistoryId() != null) {
            return searchConfiguration.getHistoryId() + "_history_" + i;
        } else {
            return "history_" + i;
        }
    }

    private void clearHistory() {
        viewHolder.searchView.setText("");
        history.clear();
        saveHistory();
        updateSearchResults("");
    }

    private void addHistoryEntry(final String entry) {
        final HistoryItem newItem = new HistoryItem(entry);
        if (!history.contains(newItem)) {
            if (history.size() >= MAX_HISTORY) {
                history.remove(history.size() - 1);
            }
            history.add(0, newItem);
            saveHistory();
            updateSearchResults(viewHolder.searchView.getText().toString());
        }
    }

    private void showKeyboard() {
        viewHolder.searchView.post(() -> {
            viewHolder.searchView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(viewHolder.searchView, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private void hideKeyboard() {
        final View view = getActivity().getCurrentFocus();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setSearchTerm(final String term) {
        if (viewHolder != null) {
            viewHolder.searchView.setText(term);
        } else {
            searchTermPreset = term;
        }
    }

    private void updateSearchResults(final String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            showHistory();
            return;
        }

        results = preferenceSearcher.searchFor(keyword, searchConfiguration.isFuzzySearchEnabled());
        adapter.setContent(new ArrayList<>(results));

        setEmptyViewShown(results.isEmpty());
    }

    private void setEmptyViewShown(final boolean shown) {
        viewHolder.noResults.setVisibility(as_VISIBLE_or_GONE(shown));
        viewHolder.recyclerView.setVisibility(as_VISIBLE_or_GONE(!shown));
    }

    private static int as_VISIBLE_or_GONE(final boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    private void showHistory() {
        viewHolder.noResults.setVisibility(View.GONE);
        viewHolder.recyclerView.setVisibility(View.VISIBLE);

        adapter.setContent(new ArrayList<>(history));
        setEmptyViewShown(history.isEmpty());
    }

    @Override
    public void onItemClicked(final ListItem item, final int position) {
        if (item.getType() == HistoryItem.TYPE) {
            final String text = ((HistoryItem) item).getTerm();
            viewHolder.searchView.setText(text);
            viewHolder.searchView.setSelection(text.length());
            if (historyClickListener != null) {
                historyClickListener.onHistoryEntryClicked(text);
            }
        } else {
            hideKeyboard();
            try {
                final SearchPreferenceResultListener searchPreferenceResultListener = (SearchPreferenceResultListener) getActivity();
                final PreferenceItem preferenceItem = results.get(position);
                addHistoryEntry(preferenceItem.title);
                searchPreferenceResultListener.onSearchResultClicked(getSearchPreferenceResult(preferenceItem));
            } catch (final ClassCastException e) {
                throw new ClassCastException(getActivity().toString() + " must implement SearchPreferenceResultListener");
            }
        }
    }

    public void setHistoryClickListener(final HistoryClickListener historyClickListener) {
        this.historyClickListener = historyClickListener;
    }

    private static class SearchViewHolder {

        public final ImageView clearButton;
        public final ImageView moreButton;
        public final EditText searchView;
        public final RecyclerView recyclerView;
        public final TextView noResults;
        public final CardView cardView;

        public SearchViewHolder(final View root) {
            searchView = root.findViewById(R.id.search);
            clearButton = root.findViewById(R.id.clear);
            recyclerView = root.findViewById(R.id.list);
            moreButton = root.findViewById(R.id.more);
            noResults = root.findViewById(R.id.no_results);
            cardView = root.findViewById(R.id.search_card);
        }
    }

    private static SearchPreferenceResult getSearchPreferenceResult(final PreferenceItem preferenceItem) {
        return new SearchPreferenceResult(
                preferenceItem.key,
                preferenceItem.resId,
                Lists
                        .getLastElement(preferenceItem.keyBreadcrumbs)
                        .orElse(null));
    }
}
