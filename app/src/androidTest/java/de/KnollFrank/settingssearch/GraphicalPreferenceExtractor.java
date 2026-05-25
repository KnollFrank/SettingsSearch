package de.KnollFrank.settingssearch;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.graph.PreferencesOfFragment;
import de.KnollFrank.lib.settingssearch.graph.UiCrawler;

public class GraphicalPreferenceExtractor {

    // FK-TODO: refactor
    public static Optional<PreferencesOfFragment> extract(final Context context, final Fragment fragment) {
        final List<Preference> preferences = new ArrayList<>();

        // Safely check for a visible RecyclerView
        final RecyclerView recyclerView = findVisibleRecyclerView();
        if (recyclerView == null) {
            return Optional.empty();
        }

        final Matcher<View> recyclerViewMatcher =
                allOf(
                        isAssignableFrom(RecyclerView.class),
                        isDisplayed());
        final int itemCount =
                recyclerView.getAdapter() != null ?
                        recyclerView.getAdapter().getItemCount() :
                        0;
        for (int i = 0; i < itemCount; i++) {
            final int position = i;

            Espresso.onView(recyclerViewMatcher).perform(RecyclerViewActions.scrollToPosition(position));

            // FK-TODO: use AtomicReferences for capturedTitle, capturedSummary and isCheckable analogous to GenerateDatabaseTest.generateDatabaseViaUiCrawler()
            final String[] capturedTitle = {null};
            final String[] capturedSummary = {null};
            final boolean[] isCheckable = {false};

            Espresso
                    .onView(recyclerViewMatcher)
                    .perform(
                            new ViewAction() {

                                @Override
                                public Matcher<View> getConstraints() {
                                    return isAssignableFrom(RecyclerView.class);
                                }

                                @Override
                                public String getDescription() {
                                    return "extract data from item at position " + position;
                                }

                                @Override
                                public void perform(final UiController uiController, final View view) {
                                    final RecyclerView rv = (RecyclerView) view;
                                    final RecyclerView.ViewHolder viewHolder = rv.findViewHolderForAdapterPosition(position);
                                    if (viewHolder != null) {
                                        final Preference virtualPreference = new Preference(context);
                                        extractDataFromView(viewHolder.itemView, virtualPreference);
                                        capturedTitle[0] = virtualPreference.getTitle() != null ? virtualPreference.getTitle().toString() : null;
                                        capturedSummary[0] = virtualPreference.getSummary() != null ? virtualPreference.getSummary().toString() : null;
                                        isCheckable[0] = containsCheckable(viewHolder.itemView);
                                    }
                                }
                            });

            if (capturedTitle[0] != null) {
                // Try to find a real preference object to enrich with metadata (like list entries)
                final Preference realPreference = findMatchingPreference(fragment, capturedTitle[0], capturedSummary[0]);
                final Preference discoveredPreference;
                if (realPreference != null) {
                    discoveredPreference = realPreference;
                } else {
                    discoveredPreference = new Preference(context);
                    discoveredPreference.setTitle(capturedTitle[0]);
                    discoveredPreference.setSummary(capturedSummary[0]);
                    // Mark for discovery if it's purely virtual and not checkable
                    if (!isCheckable[0]) {
                        discoveredPreference.setFragment(UiCrawler.DISCOVERY_DUMMY);
                    }
                    // For purely virtual items, ensure a unique, non-null key
                    discoveredPreference.setKey("graphical_item_" + capturedTitle[0] + "_" + i);
                }
                preferences.add(discoveredPreference);
            }
        }

        return Optional.of(
                new PreferencesOfFragment(
                        preferences,
                        Optional.empty(),
                        Optional.empty()));
    }

    private static Preference findMatchingPreference(final Fragment fragment, final String title, final String summary) {
        if (!(fragment instanceof final PreferenceFragmentCompat p)) {
            // FK-TODO: return Optional<Preference>
            return null;
        }
        final PreferenceScreen screen = p.getPreferenceScreen();
        if (screen == null) {
            return null;
        }
        final List<Preference> all = Preferences.getChildrenRecursively(screen);
        for (final Preference pRef : all) {
            final String pTitle = Strings.toString(Optional.ofNullable(pRef.getTitle())).orElse("");
            final String pSummary = Strings.toString(Optional.ofNullable(pRef.getSummary())).orElse("");

            final String targetTitle = title != null ? title : "";
            final String targetSummary = summary != null ? summary : "";

            if (pTitle.equals(targetTitle) && pSummary.equals(targetSummary)) {
                return pRef;
            }
        }
        return null;
    }

    private static RecyclerView findVisibleRecyclerView() {
        // FK-TODO: use AtomicReferences for result analogous to GenerateDatabaseTest.generateDatabaseViaUiCrawler()
        final RecyclerView[] result = {null};
        try {
            Espresso
                    .onView(ViewMatchers.isRoot())
                    .perform(
                            new ViewAction() {

                                @Override
                                public Matcher<View> getConstraints() {
                                    return isDisplayed();
                                }

                                @Override
                                public String getDescription() {
                                    return "find visible RecyclerView";
                                }

                                @Override
                                public void perform(final UiController uiController, final View view) {
                                    result[0] = findRecyclerViewRecursively(view);
                                }
                            });
        } catch (final Exception e) {
            // FK-TODO: return Optional<RecyclerView>
            return null;
        }
        return result[0];
    }

    private static RecyclerView findRecyclerViewRecursively(final View view) {
        if (view instanceof final RecyclerView recyclerView && view.getVisibility() == View.VISIBLE) {
            return recyclerView;
        } else if (view instanceof final ViewGroup viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                final RecyclerView found = findRecyclerViewRecursively(viewGroup.getChildAt(i));
                if (found != null) {
                    return found;
                }
            }
        }
        // FK-TODO: return Optional<RecyclerView>
        return null;
    }

    private static boolean containsCheckable(final View view) {
        if (view instanceof android.widget.Checkable) {
            return true;
        } else if (view instanceof final ViewGroup viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (containsCheckable(viewGroup.getChildAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void extractDataFromView(final View view, final Preference preference) {
        final List<TextView> textViews = findTextViews(view);
        if (textViews.isEmpty()) {
            return;
        }

        // Try to find by standard IDs first
        final TextView titleView = view.findViewById(android.R.id.title);
        final TextView summaryView = view.findViewById(android.R.id.summary);

        // Also check for the app's specific IDs for ItemFragment
        final TextView customTitleView = view.findViewById(R.id.title);
        final TextView customSummaryView = view.findViewById(R.id.summary);

        if (titleView != null) {
            preference.setTitle(titleView.getText());
        } else if (customTitleView != null) {
            preference.setTitle(customTitleView.getText());
        } else {
            // Fallback: Use the first TextView as title
            preference.setTitle(textViews.get(0).getText());
        }

        if (summaryView != null) {
            preference.setSummary(summaryView.getText());
        } else if (customSummaryView != null) {
            preference.setSummary(customSummaryView.getText());
        } else if (textViews.size() > 1) {
            // Fallback: Use the second TextView as summary if available
            preference.setSummary(textViews.get(1).getText());
        }
    }

    private static List<TextView> findTextViews(final View view) {
        final List<TextView> result = new ArrayList<>();
        if (view instanceof final TextView textView) {
            result.add(textView);
        } else if (view instanceof final ViewGroup viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                result.addAll(findTextViews(viewGroup.getChildAt(i)));
            }
        }
        return result;
    }
}
