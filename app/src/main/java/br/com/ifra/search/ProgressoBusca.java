package br.com.ifra.search;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class ProgressoBusca {
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static CircularProgressIndicator progressIndicator;

    public static CircularProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public static void setProgressIndicator(CircularProgressIndicator progressIndicator) {
        ProgressoBusca.progressIndicator = progressIndicator;
    }

    public static void visivel() {
        mainHandler.post(() -> progressIndicator.setVisibility(View.VISIBLE));
    }

    public static void gone() {
        mainHandler.post(() -> progressIndicator.setVisibility(View.GONE));
    }
}
