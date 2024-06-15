package br.com.ifra.search;

import androidx.annotation.DrawableRes;

public class SuggestionItem {
    @DrawableRes
    private final int iconResId;
    private final String title;
    private final String subtitle;

    SuggestionItem(int iconResId, String title, String subtitle) {
        this.iconResId = iconResId;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
