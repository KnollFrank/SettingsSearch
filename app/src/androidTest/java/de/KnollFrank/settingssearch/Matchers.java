package de.KnollFrank.settingssearch;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

class Matchers {

    // adapted from https://stackoverflow.com/a/53289078/12982352
    public static Matcher<View> recyclerViewHasItem(final Matcher<View> matcher) {
        return new BoundedMatcher<>(RecyclerView.class) {

            @Override
            public void describeTo(final Description description) {
                description.appendText("has item: ");
                matcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                final Adapter adapter = view.getAdapter();
                for (int position = 0; position < adapter.getItemCount(); position++) {
                    final int type = adapter.getItemViewType(position);
                    final ViewHolder holder = adapter.createViewHolder(view, type);
                    adapter.onBindViewHolder(holder, position);
                    if (matcher.matches(holder.itemView)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Matcher<View> recyclerViewHasItemCount(final Matcher<Integer> itemCountMatcher) {
        return new BoundedMatcher<>(RecyclerView.class) {

            @Override
            public void describeTo(final Description description) {
                description.appendText("recyclerView's itemCount ");
                itemCountMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                return itemCountMatcher.matches(Objects.requireNonNull(view.getAdapter()).getItemCount());
            }
        };
    }

    public static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<>() {

            @Override
            public void describeTo(final Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(final View view) {
                final ViewParent parent = view.getParent();
                return parent instanceof final ViewGroup viewGroup
                        && parentMatcher.matches(parent)
                        && view.equals(viewGroup.getChildAt(position));
            }
        };
    }
}
