package br.com.ifra;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import br.com.ifra.search.ProgressoBusca;
import br.com.ifra.search.SearchUtils;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.cat_search_fragment);

        CircularProgressIndicator progressIndicator = findViewById(R.id.circular_indicator);
        ProgressoBusca.setProgressIndicator(progressIndicator);

        SearchBar searchBar = findViewById(R.id.cat_search_bar);
        SearchView searchView = findViewById(R.id.cat_search_view);
        LinearLayout suggestionContainer = findViewById(R.id.cat_search_view_suggestion_container);

        SearchUtils.setUpSearchBar(this, searchBar);
        SearchUtils.setUpSearchView(suggestionContainer, this, searchBar, searchView);
        SearchUtils.setUpSuggestions(suggestionContainer, searchBar, searchView);
        SearchUtils.startOnLoadAnimation(searchBar, savedInstanceState);

        DynamicColors.applyToActivitiesIfAvailable(this.getApplication(), R.style.AppTheme);
    }
}

