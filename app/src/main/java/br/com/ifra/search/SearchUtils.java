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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

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

    public static void setUpSearchView(@NonNull ViewGroup suggestionContainer,
                                       @NonNull AppCompatActivity activity,
                                       @NonNull SearchBar searchBar,
                                       @NonNull SearchView searchView) {
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
                                submitSearchQuery(suggestionContainer, searchBar, searchView, searchView.getText().toString());
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
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cat_search_suggestion_item, parent, false);

        List<String> recentes = printSuggestionItemValues(parent);

        if (recentes.contains(suggestionItem.getTitle())) {
            return;
        }

        if (suggestionItems.stream().noneMatch(item -> item.getTitle().equals(suggestionItem.getTitle()))) {
            suggestionItems.add(suggestionItem);
        }

        ImageView iconView = view.findViewById(R.id.cat_searchbar_suggestion_icon);
        TextView titleView = view.findViewById(R.id.cat_searchbar_suggestion_title);

        iconView.setImageResource(suggestionItem.getIconResId());
        titleView.setText(suggestionItem.getTitle());

        view.setOnClickListener(v -> submitSearchQuery(parent, searchBar, searchView, suggestionItem.getTitle()));

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

    private static void submitSearchQuery(ViewGroup suggestionContainer, SearchBar searchBar, SearchView searchView, String query) {
        searchBar.setText(query);
        searchView.hide();
        ProgressoBusca.visivel();
        executorService.execute(() -> {
            listaVolumes = executeInBackground(suggestionContainer, searchBar, searchView, query);

            mainHandler.post(() -> {
                if (listaVolumes != null && listaVolumes.getListaVolumes() != null && !listaVolumes.getListaVolumes().isEmpty()) {

                    List<SelectableCardsAdapter.Item> items = new ArrayList<>();


                    for (VolumeDTO volume : listaVolumes.getListaVolumes()) {
                        if(volume.getIdentificador() == null) {
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
                            items.add(new SelectableCardsAdapter.Item(volume.getTitulo(), autoresString, isbn_10, isbn_13, linkImg));
                        }
                    }

                    CardUtils.getAdapter().setItems(items);

                    CardUtils.getRecyclerView().setAdapter(CardUtils.getAdapter());
                }
            });

            ProgressoBusca.gone();
        });
    }

    private static ListVolumeDTO executeInBackground(final ViewGroup suggestionContainer, final SearchBar searchBar, final SearchView searchView, final String query) {
        ReturnDTO<ListVolumeDTO> resutadoBusca = Service.buscarVolume(query);

        assert resutadoBusca != null;
        mainHandler.post(() -> {
            if (resutadoBusca.isSucesso()) {
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
}
