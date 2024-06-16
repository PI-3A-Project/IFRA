package br.com.ifra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import br.com.ifra.card.CardUtils;
import br.com.ifra.search.ProgressoBusca;
import br.com.ifra.search.SearchUtils;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cat_search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CircularProgressIndicator progressIndicator = view.findViewById(R.id.circular_indicator);
        ProgressoBusca.setProgressIndicator(progressIndicator);

        SearchBar searchBar = view.findViewById(R.id.cat_search_bar);
        SearchView searchView = view.findViewById(R.id.cat_search_view);
        LinearLayout suggestionContainer = view.findViewById(R.id.cat_search_view_suggestion_container);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        SearchUtils.setUpSearchBar(getActivity(), searchBar);
        SearchUtils.setUpSearchView(suggestionContainer, (AppCompatActivity) getActivity(), searchBar, searchView);
        SearchUtils.setUpSuggestions(suggestionContainer, searchBar, searchView);
        SearchUtils.startOnLoadAnimation(searchBar, savedInstanceState);

        CardUtils.setRecyclerView(recyclerView);
        CardUtils.setUpRecyclerView(getActivity());
    }
}
