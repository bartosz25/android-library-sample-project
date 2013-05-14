package com.example.library.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * Thanks to this listener, we can avoid EditText - TabHost issues : 
 * http://stackoverflow.com/questions/3563482/edittext-and-tab-issue
 * http://stackoverflow.com/questions/8724803/how-to-display-softinput-edittext-in-tabhost-with-erratic-behavior
 * https://code.google.com/p/android/issues/detail?id=2516 (Solution was proposed in this thread)
 *
 */

/**
 * TODO : actually this issue occurs on Eclipse emulator and not at the real device (HTC Desire). Check it. After make that it works for emulator and real device.
 */

public class EditTextTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        view.requestFocusFromTouch();
        return true;
    }

}