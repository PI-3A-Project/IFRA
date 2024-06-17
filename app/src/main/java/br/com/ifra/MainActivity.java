package br.com.ifra;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;

import java.io.Serializable;
import java.util.List;

import br.com.ifra.card.CardUtils;
import br.com.ifra.data.adapter.SelectableCardsAdapter;
import br.com.ifra.interfaces.AlternateFragment;

public class MainActivity extends AppCompatActivity implements AlternateFragment {

    private SearchFragment searchFragment;

    private BooksFragment booksFragment;

    private CardSelectFragment cardSelectFragment;

    private int idFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        idFrameLayout = R.id.main_frame;

        searchFragment = new SearchFragment();
        booksFragment = new BooksFragment();
        cardSelectFragment = new CardSelectFragment();

        CardUtils.setAlternate(this);

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

    @Override
    public void onItemClick(int position) {
        if(CardUtils.getListaItens() != null && CardUtils.getListaItens().size() >= position) {
            SelectableCardsAdapter.Item item = CardUtils.getListaItens().get(position);

            // Replace the fragment
            Bundle args = new Bundle();
            args.putSerializable("item_data", item); // Pass item data to the fragment if needed
            cardSelectFragment.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(idFrameLayout, cardSelectFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}