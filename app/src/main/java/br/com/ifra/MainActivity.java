package br.com.ifra;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import br.com.ifra.R;

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
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.main_frame);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.search){
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new SearchFragment()).commit();
                //Toast.makeText(MainActivity.this, "Pesquisar clicado", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if (itemId == R.id.books){
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new BooksFragment()).commit();
                //Toast.makeText(MainActivity.this, "Livros clicado", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // Set the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new SearchFragment()).commit();
        }

        DynamicColors.applyToActivitiesIfAvailable(this.getApplication(), R.style.AppTheme);
    }
}