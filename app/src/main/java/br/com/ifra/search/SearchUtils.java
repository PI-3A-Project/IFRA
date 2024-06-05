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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.search.SearchView.TransitionState;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import br.com.ifra.R;
import br.com.ifra.service.Service;

/** Provides utility methods for the search demo. */
public final class SearchUtils {

  private SearchUtils() {}

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
              submitSearchQuery(suggestionContainer, searchBar, searchView, searchView.getText().toString());
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
        (searchView1, previousState, newState) ->
            onBackPressedCallback.setEnabled(newState == TransitionState.SHOWN));
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

  public static void setUpSuggestions(
      @NonNull ViewGroup suggestionContainer,
      @NonNull SearchBar searchBar,
      @NonNull SearchView searchView) {

    addSuggestionTitleView(
        suggestionContainer, R.string.cat_searchview_suggestion_section_title);
    addSuggestionItemViews(suggestionContainer, new ArrayList<>(), searchBar, searchView);
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
      List<SuggestionItem> suggestionItems,
      SearchBar searchBar,
      SearchView searchView) {
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
    if(recentes.contains(suggestionItem.title)){
      return;
    }

    ImageView iconView = view.findViewById(R.id.cat_searchbar_suggestion_icon);
    TextView titleView = view.findViewById(R.id.cat_searchbar_suggestion_title);

    iconView.setImageResource(suggestionItem.iconResId);
    titleView.setText(suggestionItem.title);

    view.setOnClickListener(v -> submitSearchQuery(parent, searchBar, searchView, suggestionItem.title));

    if(parent.getChildCount() > 5) {
      parent.removeViewAt(parent.getChildCount()-1);
    }
    parent.addView(view, 1);
  }

  private static List<String> printSuggestionItemValues(ViewGroup parent) {
    List<String> recentes = new ArrayList<>();
    int childCount = parent.getChildCount();

    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);

      if(child != null) {
        ImageView iconView = child.findViewById(R.id.cat_searchbar_suggestion_icon);
        TextView titleView = child.findViewById(R.id.cat_searchbar_suggestion_title);

        if(titleView != null) {
          String title = titleView.getText().toString();
          recentes.add(title);
          Log.d("", title);
        }
      }
    }

    return recentes;
  }

  private static void submitSearchQuery(ViewGroup suggestionContainer, SearchBar searchBar, SearchView searchView, String query) {

    Service.buscarVolume(query);

    SuggestionItem suggestionItems =
            new SuggestionItem(
                    R.drawable.ic_schedule_vd_theme_24,
                    query,
                    "");
    addSuggestionItemView(suggestionContainer, suggestionItems, searchBar, searchView);

    searchBar.setText(query);
    searchView.hide();
  }

  private static class SuggestionItem {
    @DrawableRes private final int iconResId;
    private final String title;
    private final String subtitle;

    private SuggestionItem(int iconResId, String title, String subtitle) {
      this.iconResId = iconResId;
      this.title = title;
      this.subtitle = subtitle;
    }
  }
}
