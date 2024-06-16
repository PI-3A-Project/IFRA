package br.com.ifra;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.com.ifra.search.SuggestionItem;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<SuggestionItem>> suggestionItems;

    public LiveData<List<SuggestionItem>> getSuggestionItems() {
        if (suggestionItems == null) {
            suggestionItems = new MutableLiveData<>(new ArrayList<>());
        }
        return suggestionItems;
    }

    public void addSuggestionItem(SuggestionItem item) {
        List<SuggestionItem> currentItems = suggestionItems.getValue();
        if (currentItems != null && currentItems.stream().noneMatch(i -> i.getTitle().equals(item.getTitle()))) {
            currentItems.add(item);
            suggestionItems.setValue(currentItems);
        }
    }

}
