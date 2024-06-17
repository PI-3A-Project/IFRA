/*
 * Copyright 2019 The Android Open Source Project
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

package br.com.ifra.data.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.ifra.R;
import br.com.ifra.interfaces.AlternateFragment;

/**
 * An Adapter that works with a collection of selectable card items
 */
public class SelectableCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> items;

    private static List<Item> favoritos = new ArrayList<>();
    private SelectionTracker<Long> selectionTracker;
    private AlternateFragment listener;

    public SelectableCardsAdapter(AlternateFragment listener) {
        this.listener = listener;
        this.items = new ArrayList<>();
    }

    public static List<Item> getFavoritosStatic() {
        return favoritos;
    }

    public void addToFavoritos(Item item) {
        if (!favoritos.contains(item)) {
            favoritos.add(item);
            notifyDataSetChanged(); // Atualiza a UI para refletir a mudança
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cat_card_item_view, parent, false);
        return new ItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Item item = items.get(position);

        ((ItemViewHolder) viewHolder).bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final Details details;
        private final MaterialCardView materialCardView;

        private final ImageView imagemView;
        private final TextView titleView;
        private final TextView autoresView;

        private final TextView isbn_10View;

        private final TextView isbn_13View;

        ItemViewHolder(View itemView, final AlternateFragment listener) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.item_card);
            imagemView = itemView.findViewById(R.id.cat_card_image);
            titleView = itemView.findViewById(R.id.cat_card_title);
            autoresView = itemView.findViewById(R.id.cat_card_autores);
            isbn_10View = itemView.findViewById(R.id.cat_card_isbn10);
            isbn_13View = itemView.findViewById(R.id.cat_card_isbn13);

            materialCardView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
            details = new Details();
        }

        private void bind(Item item, int position) {
            details.position = position;
            titleView.setText(item.title);
            autoresView.setText(item.autor);
            isbn_10View.setText(item.isbn_10);
            isbn_13View.setText(item.isbn_13);

            Glide.with(itemView.getContext())
                    .load(item.imagemUrl)
                    .placeholder(R.drawable.baseline_call_to_action)
                    .error(R.drawable.hide_image)
                    .into(imagemView);

            if (selectionTracker != null) {
                bindSelectedState();
            }
        }

        private void bindSelectedState() {
            Long selectionKey = details.getSelectionKey();
            materialCardView.setChecked(selectionTracker.isSelected(selectionKey));
            if (selectionKey != null) {
                addAccessibilityActions(selectionKey);
            }
        }

        private void addAccessibilityActions(@NonNull Long selectionKey) {
            ViewCompat.addAccessibilityAction(
                    materialCardView,
                    materialCardView.getContext().getString(R.string.cat_card_action_select),
                    (view, arguments) -> {
                        selectionTracker.select(selectionKey);
                        return true;
                    });
            ViewCompat.addAccessibilityAction(
                    materialCardView,
                    materialCardView.getContext().getString(R.string.cat_card_action_deselect),
                    (view, arguments) -> {
                        selectionTracker.deselect(selectionKey);
                        return true;
                    });
        }

        ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return details;
        }
    }

    public static class DetailsLookup extends ItemDetailsLookup<Long> {

        private final RecyclerView recyclerView;

        public DetailsLookup(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                if (viewHolder instanceof ItemViewHolder) {
                    return ((ItemViewHolder) viewHolder).getItemDetails();
                }
            }
            return null;
        }
    }

    public static class KeyProvider extends ItemKeyProvider<Long> {

        public KeyProvider(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
            super(ItemKeyProvider.SCOPE_MAPPED);
        }

        @Nullable
        @Override
        public Long getKey(int position) {
            return (long) position;
        }

        @Override
        public int getPosition(@NonNull Long key) {
            long value = key;
            return (int) value;
        }
    }

    public static class Item implements Serializable {

        private final String imagemUrl;
        private final String title;
        private final String autor;

        private final String isbn_10;
        private final String isbn_13;

        private final String detalhes;

        public Item(String title, String autor, String isbn_10, String isbn_13, String imagemUrl, String detalhes) {
            this.title = title;
            this.autor = autor;
            this.isbn_10 = ("ISBN-10 : " + (isbn_10 != null ? isbn_10 : "---"));
            this.isbn_13 = ("ISBN-13 : " + (isbn_13 != null ? isbn_13 : "---"));
            this.imagemUrl = imagemUrl;
            this.detalhes = detalhes;
        }

        public String getImagemUrl() {
            return imagemUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getAutor() {
            return autor;
        }

        public String getIsbn_10() {
            return isbn_10;
        }

        public String getIsbn_13() {
            return isbn_13;
        }

        public String getDetalhes() {
            return detalhes;
        }
    }

    public static class Details extends ItemDetailsLookup.ItemDetails<Long> {

        long position;

        Details() {
        }

        @Override
        public int getPosition() {
            return (int) position;
        }

        @Nullable
        @Override
        public Long getSelectionKey() {
            return position;
        }

        @Override
        public boolean inSelectionHotspot(@NonNull MotionEvent e) {
            return false;
        }

        @Override
        public boolean inDragRegion(@NonNull MotionEvent e) {
            return true;
        }
    }
}
