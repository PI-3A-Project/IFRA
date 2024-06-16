package br.com.ifra;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity {

    private SearchFragment searchFragment;

    private BooksFragment booksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        int idFrameLayout = R.id.main_frame;

        searchFragment = new SearchFragment();
        booksFragment = new BooksFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.search){
                getSupportFragmentManager().beginTransaction().replace(idFrameLayout, searchFragment).commit();
                return true;
            }
            else if (itemId == R.id.books){
                getSupportFragmentManager().beginTransaction().replace(idFrameLayout, booksFragment).commit();
                return true;
            }
            return false;
        });

        // Set the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(idFrameLayout, searchFragment).commit();
        }

        DynamicColors.applyToActivitiesIfAvailable(this.getApplication(), R.style.AppTheme);
    }
}