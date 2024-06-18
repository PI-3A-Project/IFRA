package br.com.ifra;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    private boolean isFavorited = false;
    private static final String PREFS_NAME = "favorites_prefs";

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
        // Inflar o layout do fragmento
        View view = inflater.inflate(R.layout.cat_fragment_card_select, container, false);

        // Carregar a animação de entrada
        Animation animacaoEntrada = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        view.startAnimation(animacaoEntrada);

        // Configurar o resto do seu fragmento

        return view;
    }
    private void updateFabIcon(FloatingActionButton fab, boolean isFavorited) {
        if (isFavorited) {
            fab.setImageResource(R.drawable.favorito_marcado);
        } else {
            fab.setImageResource(R.drawable.favorito);
        }
    }

    private String getFavoriteKey(SelectableCardsAdapter.Item item) {
        // Suponha que cada item tenha um identificador único, como um ID
        return "favorite_" + item.getId();
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
        TextView publicadora = view.findViewById(R.id.cat_card_editora_detalhes);
        TextView idioma = view.findViewById(R.id.cat_card_idioma_detalhes);
        TextView paginas = view.findViewById(R.id.cat_card_paginas_detalhes);
        TextView ano = view.findViewById(R.id.cat_card_ano_detalhes);
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
        publicadora.setText(item.getPublicadora());
        idioma.setText(item.getIdioma());
        paginas.setText(item.getPaginas());
        ano.setText(item.getAno());

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
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final boolean[] isFavorited = {preferences.getBoolean(getFavoriteKey(item), false)};
        updateFabIcon(fab, isFavorited[0]);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null && item != null) {
                    SharedPreferences.Editor editor = preferences.edit();
                    if (isFavorited[0]) {
                        adapter.removeFromFavoritos(item);
                        Toast.makeText(getActivity(), "Item removido dos favoritos!", Toast.LENGTH_SHORT).show();
                        fab.setImageResource(R.drawable.favorito);
                        editor.putBoolean(getFavoriteKey(item), false);
                    } else {
                        adapter.addToFavoritos(item);
                        Toast.makeText(getActivity(), "Item adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
                        fab.setImageResource(R.drawable.favorito_marcado);
                        editor.putBoolean(getFavoriteKey(item), true);
                    }
                    editor.apply();
                    isFavorited[0] = !isFavorited[0];
                }
            }
        });
        if (adapter == null) {
            adapter = new SelectableCardsAdapter();
        }

    }
}