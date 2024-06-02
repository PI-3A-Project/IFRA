package br.com.ifra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import br.com.ifra.search.SearchDemoUtils;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.cat_search_fragment);


        SearchBar searchBar = findViewById(R.id.cat_search_bar);
        SearchView searchView = findViewById(R.id.cat_search_view);
        LinearLayout suggestionContainer = findViewById(R.id.cat_search_view_suggestion_container);

        SearchDemoUtils.setUpSearchBar(this, searchBar);
        SearchDemoUtils.setUpSearchView(suggestionContainer, this, searchBar, searchView);
        SearchDemoUtils.setUpSuggestions(suggestionContainer, searchBar, searchView);
        SearchDemoUtils.startOnLoadAnimation(searchBar, savedInstanceState);

        DynamicColors.applyToActivitiesIfAvailable(this.getApplication(), R.style.Theme_IFRA);
    }
}

