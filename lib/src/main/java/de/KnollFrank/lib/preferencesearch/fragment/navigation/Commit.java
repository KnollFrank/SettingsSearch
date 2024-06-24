package de.KnollFrank.lib.preferencesearch.fragment.navigation;

import androidx.fragment.app.FragmentTransaction;

import java.util.function.Consumer;

public enum Commit {

    COMMIT_ASYNC(FragmentTransaction::commit),
    COMMIT_NOW(FragmentTransaction::commitNow);

    private final Consumer<FragmentTransaction> commit;

    Commit(final Consumer<FragmentTransaction> commit) {
        this.commit = commit;
    }

    public void commit(final FragmentTransaction fragmentTransaction) {
        commit.accept(fragmentTransaction);
    }
}
