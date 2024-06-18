package br.com.ifra.card;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import br.com.ifra.CardSelectFragment;
import br.com.ifra.R;
import br.com.ifra.data.adapter.SelectableCardsAdapter;
import br.com.ifra.interfaces.AlternateFragment;

public class CardUtils {
    private static SelectableCardsAdapter adapter;
    private static SelectionTracker<Long> selectionTracker;
    private static RecyclerView recyclerView;

    public static void setUpRecyclerView(Activity activity) {
        if (adapter == null) {
            adapter = new SelectableCardsAdapter();
        }
        recyclerView.setAdapter(adapter);

        if (selectionTracker == null) {
            selectionTracker =
                    new SelectionTracker.Builder<>(
                            "card_selection",
                            recyclerView,
                            new SelectableCardsAdapter.KeyProvider(adapter),
                            new SelectableCardsAdapter.DetailsLookup(recyclerView),
                            StorageStrategy.createLongStorage())
                            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                            .build();
            adapter.setSelectionTracker(selectionTracker);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    public static List<SelectableCardsAdapter.Item> getListaItens() {
        return adapter.getItems();
    }

    public static SelectableCardsAdapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(SelectableCardsAdapter adapter) {
        CardUtils.adapter = adapter;
    }

    public static SelectionTracker<Long> getSelectionTracker() {
        return selectionTracker;
    }

    public static void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        CardUtils.selectionTracker = selectionTracker;
    }

    public static RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public static void setRecyclerView(RecyclerView recyclerView) {
        CardUtils.recyclerView = recyclerView;
    }
}
