package com.alibaba.ailabs.tg.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/* JADX INFO: loaded from: classes.dex */
public class InputMethodUtils {
    public static void hideSoftKeyboard(Activity activity2) {
        InputMethodManager inputMethodManager;
        if (activity2 == null || activity2.isFinishing() || (inputMethodManager = (InputMethodManager) activity2.getSystemService("input_method")) == null || activity2.getWindow() == null || activity2.getWindow().getDecorView() == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(activity2.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity activity2, View view2) {
        InputMethodManager inputMethodManager;
        if (activity2 == null || activity2.isFinishing() || view2 == null || (inputMethodManager = (InputMethodManager) activity2.getSystemService("input_method")) == null) {
            return;
        }
        inputMethodManager.showSoftInput(view2, 2);
    }

    public static void showSoftKeyboard(Activity activity2) {
        InputMethodManager inputMethodManager;
        if (activity2 == null || activity2.isFinishing() || (inputMethodManager = (InputMethodManager) activity2.getSystemService("input_method")) == null) {
            return;
        }
        View currentFocus = activity2.getCurrentFocus();
        if (currentFocus == null) {
            currentFocus = new View(activity2);
        }
        inputMethodManager.showSoftInput(currentFocus, 2);
    }

    public static void toggleSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
        if (inputMethodManager == null) {
            return;
        }
        inputMethodManager.toggleSoftInput(2, 0);
    }
}
