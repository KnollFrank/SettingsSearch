package de.KnollFrank.lib.settingssearch.test;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.function.Predicate;

public class Matchers {

    public static <VH extends RecyclerView.ViewHolder> boolean recyclerViewHasItem(
            final RecyclerView recyclerView,
            final Predicate<VH> matcher) {
        final Adapter<VH> adapter = recyclerView.getAdapter();
        for (int position = 0; position < adapter.getItemCount(); position++) {
            if (matcher.test(createAndBindViewHolder(recyclerView, adapter, position))) {
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
}
