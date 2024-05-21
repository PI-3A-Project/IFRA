package br.com.ifra.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageUtil {
    private static final String BUNDLE_NAME = "messages";
    private static Locale locale;
    public static String getMessage(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        return bundle.getString(key);
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        MessageUtil.locale = locale;
    }
}
