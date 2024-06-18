/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.ifra.search;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.search.SearchView.TransitionState;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.ifra.R;
import br.com.ifra.card.CardUtils;
import br.com.ifra.data.adapter.SelectableCardsAdapter;
import br.com.ifra.data.model.dto.ReturnDTO;
import br.com.ifra.data.model.dto.volume.IsbnDTO;
import br.com.ifra.data.model.dto.volume.ListVolumeDTO;
import br.com.ifra.data.model.dto.volume.VolumeDTO;
import br.com.ifra.service.Service;

/**
 * Provides utility methods for the search demo.
 */
public final class SearchUtils {
    private SearchUtils() {
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (activity.getWindow() != null) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    public static void setUpSearchBar(@NonNull Activity activity, @NonNull SearchBar searchBar) {
        searchBar.inflateMenu(R.menu.cat_searchbar_menu);
        searchBar.setOnMenuItemClickListener(
                menuItem -> {
                    showSnackbar(activity, menuItem);
                    return true;
                });
    }

    private String originalSearchText = "";

    public static void setUpSearchView(@NonNull ViewGroup suggestionContainer,
                                       @NonNull AppCompatActivity activity,
                                       @NonNull SearchBar searchBar,
                                       @NonNull SearchView searchView,
                                       @NonNull Chip advanced,
                                       @NonNull Chip intitle,
                                       @NonNull Chip inauthor,
                                       @NonNull Chip inpublisher,
                                       @NonNull Chip subject,
                                       @NonNull Chip isbn,
                                       @NonNull Chip lccn,
                                       @NonNull Chip oclc
    ) {
        searchView.inflateMenu(R.menu.cat_searchview_menu);
        searchView.setOnMenuItemClickListener(
                menuItem -> {
                    showSnackbar(activity, menuItem);
                    return true;
                });
        searchView
                .getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                submitSearchQuery(suggestionContainer, searchBar, searchView, buildQueryString(searchView.getText().toString(), intitle.isChecked(), inauthor.isChecked(), inpublisher.isChecked(), subject.isChecked(), isbn.isChecked(), lccn.isChecked(), oclc.isChecked()));
                                //disableChips(intitle, inauthor, inpublisher, subject, isbn, lccn, oclc);
                                return true;
                            }
                            return false;
                        });
        OnBackPressedCallback onBackPressedCallback =
                new OnBackPressedCallback(/* enabled= */ false) {
                    @Override
                    public void handleOnBackPressed() {
                        searchView.hide();
                    }
                };
        activity.getOnBackPressedDispatcher().addCallback(activity, onBackPressedCallback);
        searchView.addTransitionListener(
                (searchView1, previousState, newState) -> {
                    onBackPressedCallback.setEnabled(newState == TransitionState.SHOWN);
                    if (newState == TransitionState.SHOWN) {
                        // Change status bar color to match suggestion container
                        setStatusBarColor(activity, activity.getResources().getColor(R.color.md_theme_surfaceContainerHigh_highContrast));
                    } else if (newState == TransitionState.HIDDEN) {
                        // Revert status bar color to original
                        int suggestionContainerColor = suggestionContainer.getSolidColor();
                        setStatusBarColor(activity, suggestionContainerColor);
                    }
                });


        // Listener para o Chip advanced
        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(searchView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_bottom_sheet_filters);

                // Set any necessary dialog properties here, such as onClickListeners for buttons

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                //Toast.makeText(activity, "BottomSheetBehavior not initialized!", Toast.LENGTH_SHORT).show();

            }
        });

        // Listener para o Chip intitle
        intitle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Reagir à mudança de estado do Chip intitle
            updateSearchQuery(suggestionContainer, searchBar, searchView, searchView.getText().toString(), intitle.isChecked(), inauthor.isChecked(), inpublisher.isChecked(), subject.isChecked(), isbn.isChecked(), lccn.isChecked(), oclc.isChecked());
        });

        // Listener para o Chip inauthor
        inauthor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Reagir à mudança de estado do Chip inauthor
            updateSearchQuery(suggestionContainer, searchBar, searchView, searchView.getText().toString(), intitle.isChecked(), inauthor.isChecked(), inpublisher.isChecked(), subject.isChecked(), isbn.isChecked(), lccn.isChecked(), oclc.isChecked());
        });

        // Listener para o Chip inpublisher
        inpublisher.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Reagir à mudança de estado do Chip inpublisher
            updateSearchQuery(suggestionContainer, searchBar, searchView, searchView.getText().toString(), intitle.isChecked(), inauthor.isChecked(), inpublisher.isChecked(), subject.isChecked(), isbn.isChecked(), lccn.isChecked(), oclc.isChecked());
        });

        // Listener para o Chip subject
        subject.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Reagir à mudança de estado do Chip subject
            updateSearchQuery(suggestionContainer, searchBar, searchView, searchView.getText().toString(), intitle.isChecked(), inauthor.isChecked(), inpublisher.isChecked(), subject.isChecked(), isbn.isChecked(), lccn.isChecked(), oclc.isChecked());
        });

        // Listener para o Chip isbn
        isbn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Reagir à mudança de estado do Chip isbn
            updateSearchQuery(suggestionContainer, searchBar, searchView, searchView.getText().toString(), intitle.isChecked(), inauthor.isChecked(), inpublisher.isChecked(), subject.isChecked(), isbn.isChecked(), lccn.isChecked(), oclc.isChecked());
        });

        // Listener para o Chip lccn
        lccn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Reagir à mudança de estado do Chip lccn
            updateSearchQuery(suggestionContainer, searchBar, searchView, searchView.getText().toString(), intitle.isChecked(), inauthor.isChecked(), inpublisher.isChecked(), subject.isChecked(), isbn.isChecked(), lccn.isChecked(), oclc.isChecked());
        });

        // Listener para o Chip oclc
        oclc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Reagir à mudança de estado do Chip oclc
            updateSearchQuery(suggestionContainer, searchBar, searchView, searchView.getText().toString(), intitle.isChecked(), inauthor.isChecked(), inpublisher.isChecked(), subject.isChecked(), isbn.isChecked(), lccn.isChecked(), oclc.isChecked());
        });
    }

    private static void updateSearchQuery(ViewGroup suggestionContainer, SearchBar searchBar, SearchView searchView, String searchText, boolean intitleChecked, boolean inauthorChecked, boolean inpublisherChecked, boolean subjectChecked, boolean isbnChecked, boolean lccnChecked, boolean oclcChecked) {
        String query = buildQueryString(searchText, intitleChecked, inauthorChecked, inpublisherChecked, subjectChecked, isbnChecked, lccnChecked, oclcChecked);
    }

    private static String buildQueryString(String searchText, boolean intitleChecked, boolean inauthorChecked, boolean inpublisherChecked, boolean subjectChecked, boolean isbnChecked, boolean lccnChecked, boolean oclcChecked) {
        StringBuilder queryBuilder = new StringBuilder();

        // Adicionar o texto de busca principal
        queryBuilder.append(searchText);

        // Adicionar os filtros apenas se estiverem marcados
        if (intitleChecked) {
            queryBuilder.append("+intitle:").append(searchText);
        }
        if (inauthorChecked) {
            queryBuilder.append("+inauthor:").append(searchText);
        }
        if (inpublisherChecked) {
            queryBuilder.append("+inpublisher:").append(searchText);
        }
        if (subjectChecked) {
            queryBuilder.append("+subject:").append(searchText);
        }
        if (isbnChecked) {
            queryBuilder.append("+isbn:").append(searchText);
        }
        if (lccnChecked) {
            queryBuilder.append("+lccn:").append(searchText);
        }
        if (oclcChecked) {
            queryBuilder.append("+oclc:").append(searchText);
        }

        return queryBuilder.toString();
    }


    public static void showSnackbar(@NonNull Activity activity, @NonNull MenuItem menuItem) {
        Snackbar.make(
                        activity.findViewById(android.R.id.content), menuItem.getTitle(), Snackbar.LENGTH_SHORT)
                .show();
    }

    public static void startOnLoadAnimation(@NonNull SearchBar searchBar, @Nullable Bundle bundle) {
        // Don't start animation on rotation. Only needed in demo because minIntervalSeconds is 0.
        if (bundle == null) {
            searchBar.startOnLoadAnimation();
        }
    }

    private static List<SuggestionItem> suggestionItems;

    public static void setUpSuggestions(
            @NonNull ViewGroup suggestionContainer,
            @NonNull SearchBar searchBar,
            @NonNull SearchView searchView) {

        addSuggestionItemViews(suggestionContainer, searchBar, searchView);
    }

    private static void addSuggestionTitleView(ViewGroup parent, @StringRes int titleResId) {
        TextView titleView =
                (TextView)
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.cat_search_suggestion_title, parent, false);

        titleView.setText(titleResId);

        parent.addView(titleView);
    }

    private static void addSuggestionItemViews(
            ViewGroup parent,
            SearchBar searchBar,
            SearchView searchView) {
        if (suggestionItems == null) {
            suggestionItems = new ArrayList<>();
        }
        for (SuggestionItem suggestionItem : suggestionItems) {
            addSuggestionItemView(parent, suggestionItem, searchBar, searchView);
        }
    }

    private static void addSuggestionItemView(
            ViewGroup parent, SuggestionItem suggestionItem, SearchBar searchBar, SearchView searchView) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_search_suggestion_item, parent, false);

        List<String> recentes = printSuggestionItemValues(parent);

        // Verifica se o título contém "+", e se sim, pega somente a parte anterior ao primeiro "+"
        String titleWithoutPlus = suggestionItem.getTitle().split("\\+")[0].trim();

        // Verifica se já existe um item com o título sem considerar o que vem após o "+"
        if (recentes.contains(titleWithoutPlus) || TextUtils.isEmpty(titleWithoutPlus)) {
            return;
        }

        // Verifica se já existe um item com o título completo na lista de sugestões
        if (suggestionItems.stream().noneMatch(item -> item.getTitle().equals(suggestionItem.getTitle()))) {
            suggestionItems.add(suggestionItem);
        }

        ImageView iconView = view.findViewById(R.id.cat_searchbar_suggestion_icon);
        TextView titleView = view.findViewById(R.id.cat_searchbar_suggestion_title);

        iconView.setImageResource(suggestionItem.getIconResId());
        titleView.setText(titleWithoutPlus); // Define o título sem considerar o que vem após o "+"

        view.setOnClickListener(v -> submitSearchQuery(parent, searchBar, searchView, titleWithoutPlus));

        if (parent.getChildCount() > 5) {
            parent.removeViewAt(parent.getChildCount() - 1);
        }

        // Verifica se é a primeira sugestão a ser adicionada
        if (printSuggestionItemValues(parent).isEmpty()) {
            addSuggestionTitleView(parent, R.string.cat_searchview_suggestion_section_title);
        }
        parent.addView(view, 1);
    }


    private static List<String> printSuggestionItemValues(ViewGroup parent) {
        List<String> recentes = new ArrayList<>();
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            if (child != null) {
//                ImageView iconView = child.findViewById(R.id.cat_searchbar_suggestion_icon);
                TextView titleView = child.findViewById(R.id.cat_searchbar_suggestion_title);

                if (titleView != null) {
                    String title = titleView.getText().toString();
                    recentes.add(title);
                    Log.d("", title);
                }
            }
        }

        return recentes;
    }

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static CircularProgressIndicator progressIndicator;

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    private static ListVolumeDTO listaVolumes;

    private static TextView titleRecycleView;

    private static void submitSearchQuery(ViewGroup suggestionContainer, SearchBar searchBar, SearchView searchView, String query) {
        titleRecycleView.setText(" ");
        // Verifica se a query está nula ou vazia
        if (query == null || query.isEmpty()) {
            searchView.hide();
            return;
        }

        CardUtils.getAdapter().getItems().clear();
        CardUtils.getRecyclerView().setAdapter(CardUtils.getAdapter());

        searchView.hide();
        ProgressoBusca.visivel();
        executorService.execute(() -> {
            listaVolumes = executeInBackground(suggestionContainer, searchBar, searchView, query);

            mainHandler.post(() -> {
                if (listaVolumes != null && listaVolumes.getListaVolumes() != null && !listaVolumes.getListaVolumes().isEmpty()) {

                    List<SelectableCardsAdapter.Item> items = new ArrayList<>();


                    for (VolumeDTO volume : listaVolumes.getListaVolumes()) {
                        if (volume.getIdentificador() == null) {
                            continue;
                        }
                        String autoresString = null;
                        if (volume.getAutores() != null) {
                            autoresString = String.join(", ", volume.getAutores());
                        } else {
                            autoresString = "Autores não definidos";
                        }

                        String isbn_10 = null;
                        String isbn_13 = null;
                        String linkImg = null;
                        if (volume.getLinks() != null) {
                            linkImg = volume.getLinks().getUrlCapaPequena() != null ? volume.getLinks().getUrlCapaPequena() : volume.getLinks().getUrlCapaNormal();
                        }

                        for (IsbnDTO isbn : volume.getIdentificador()) {
                            if (isbn.getTipo().equals("ISBN_10")) {
                                isbn_10 = isbn.getNumeroISBN();
                            } else if (isbn.getTipo().equals("ISBN_13")) {
                                isbn_13 = isbn.getNumeroISBN();
                            }
                        }

                        if (isbn_10 != null || isbn_13 != null) {
                            items.add(new SelectableCardsAdapter.Item(
                                    volume.getTitulo(),
                                    autoresString,
                                    isbn_10,
                                    isbn_13,
                                    linkImg,
                                    volume.getDescricao(),
                                    volume.getDataPublicacao(),
                                    volume.getEditora(),
                                    volume.getPaginas(),
                                    volume.getLingua(),
                                    volume.getId()
                            ));
                        }
                    }

                    CardUtils.getAdapter().setItems(items);
                    CardUtils.getRecyclerView().setAdapter(CardUtils.getAdapter());
                    titleRecycleView.setText("Resultados");
                } else {
                    titleRecycleView.setText("Nenhum resultado encontrado");
                }
                ProgressoBusca.gone();
            });
        });
        if (query.contains("+")) {
            String primeiraPalavra = query.substring(0, query.indexOf("+"));
            searchBar.setText(primeiraPalavra);
        }
    }

    public static void disableChips(Chip intitle,
                                    Chip inauthor,
                                    Chip inpublisher,
                                    Chip subject,
                                    Chip isbn,
                                    Chip lccn,
                                    Chip oclc) {
        intitle.setChecked(false);
        inauthor.setChecked(false);
        inpublisher.setChecked(false);
        subject.setChecked(false);
        isbn.setChecked(false);
        lccn.setChecked(false);
        oclc.setChecked(false);
    }

    private static ListVolumeDTO executeInBackground(final ViewGroup suggestionContainer, final SearchBar searchBar, final SearchView searchView, final String query) {
        ReturnDTO<ListVolumeDTO> resutadoBusca = Service.buscarVolume(query);

        assert resutadoBusca != null;
        mainHandler.post(() -> {
            if (resutadoBusca.isSucesso() && resutadoBusca.getRetorno() != null && resutadoBusca.getRetorno().getListaVolumes() != null && !resutadoBusca.getRetorno().getListaVolumes().isEmpty()) {
                SuggestionItem suggestionItems =
                        new SuggestionItem(
                                R.drawable.ic_schedule_vd_theme_24,
                                query,
                                "");
                addSuggestionItemView(suggestionContainer, suggestionItems, searchBar, searchView);
            }
        });

        return resutadoBusca.getRetorno();
    }

    public static CircularProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public static void setProgressIndicator(CircularProgressIndicator progressIndicator) {
        SearchUtils.progressIndicator = progressIndicator;
    }

    public static TextView getTitleRecycleView() {
        return titleRecycleView;
    }

    public static void setTitleRecycleView(TextView titleRecycleView) {
        SearchUtils.titleRecycleView = titleRecycleView;
    }
}
