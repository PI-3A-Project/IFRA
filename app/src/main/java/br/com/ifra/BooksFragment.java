package br.com.ifra;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.List;

import br.com.ifra.card.CardUtils;
import br.com.ifra.data.adapter.SelectableCardsAdapter;
import br.com.ifra.search.ProgressoBusca;
import br.com.ifra.search.SearchUtils;

public class BooksFragment extends Fragment {
    private RecyclerView recyclerView;
    private SelectableCardsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cat_book_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa o adaptador com a lista de favoritos
        List<SelectableCardsAdapter.Item> favoritos = getFavoritos();
        adapter = new SelectableCardsAdapter();  // Passa null se não houver listener
        adapter.setItems(favoritos);

        recyclerView.setAdapter(adapter);
    }

    private List<SelectableCardsAdapter.Item> getFavoritos() {
        // Aqui você pode obter a lista de favoritos de onde você estiver armazenando,
        // por exemplo, uma variável estática ou uma base de dados.
        // Para este exemplo, vamos assumir que é uma lista estática no adapter.
        return SelectableCardsAdapter.getFavoritosStatic();
    }
}