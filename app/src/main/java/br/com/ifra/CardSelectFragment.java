package br.com.ifra;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;

import br.com.ifra.data.adapter.SelectableCardsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CardSelectFragment extends Fragment {

    SelectableCardsAdapter.Item item;
    private SelectableCardsAdapter adapter; // Adicione esta linha

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Serializable serializable = getArguments().getSerializable("item_data");
            if (serializable instanceof SelectableCardsAdapter.Item) {
                item = (SelectableCardsAdapter.Item) serializable;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cat_fragment_card_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imagemView = view.findViewById(R.id.cat_card_image_detalhes);
        TextView titulo = view.findViewById(R.id.cat_card_title_detalhes);
        TextView autores = view.findViewById(R.id.cat_card_autores_detalhes);
        TextView isbn_10 = view.findViewById(R.id.cat_card_isbn10_detalhes);
        TextView isbn_13 = view.findViewById(R.id.cat_card_isbn13_detalhes);
        TextView detalhes = view.findViewById(R.id.detalhes);
        Button btnBack = view.findViewById(R.id.btn_back);

        Glide.with(view.getContext())
                .load(item.getImagemUrl())
                .placeholder(R.drawable.baseline_call_to_action)
                .error(R.drawable.hide_image)
                .into(imagemView);

        titulo.setText(item.getTitle());
        autores.setText(item.getAutor());
        isbn_10.setText(item.getIsbn_10());
        isbn_13.setText(item.getIsbn_13());
        detalhes.setText(item.getDetalhes());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    // Handle case where there is no previous fragment in the back stack
                    // Optionally, you could finish the activity or handle it as per your need
                }
            }
        });
        FloatingActionButton fab = view.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adiciona o item aos favoritos
                if (adapter != null && item != null) {
                    adapter.addToFavoritos(item);
                    Toast.makeText(getActivity(), "Item adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (adapter == null) {
            adapter = new SelectableCardsAdapter();
        }

    }
}