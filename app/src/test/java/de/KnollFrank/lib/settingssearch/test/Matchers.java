package de.KnollFrank.lib.settingssearch.test;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Objects;

public class Matchers {

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

    public static <VH extends RecyclerView.ViewHolder> Matcher<RecyclerView> recyclerViewHasItem(final Matcher<VH> viewHolderMatcher) {
        return new BoundedMatcher<>(RecyclerView.class) {

            @Override
            public void describeTo(final Description description) {
                description.appendText("recyclerView has item ");
                viewHolderMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView recyclerView) {
                final Adapter<VH> adapter = Objects.requireNonNull(recyclerView.getAdapter());
                for (int position = 0; position < adapter.getItemCount(); position++) {
                    if (viewHolderMatcher.matches(createAndBindViewHolder(recyclerView, adapter, position))) {
                        return true;
                    }
                }
                return false;
            }

            private static <VH extends RecyclerView.ViewHolder> @NonNull VH createAndBindViewHolder(
                    final RecyclerView recyclerView,
                    final Adapter<VH> adapter,
                    final int position) {
                final VH holder =
                        adapter.createViewHolder(
                                recyclerView,
                                adapter.getItemViewType(position));
                adapter.onBindViewHolder(holder, position);
                return holder;
            }
        };
    }
}
