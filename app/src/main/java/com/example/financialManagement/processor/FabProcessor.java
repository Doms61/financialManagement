package com.example.financialManagement.processor;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import financialManagement.R;

/**
 * This class servers as a helper for common Floating Action Button methods
 */
public class FabProcessor {

    /**
     * Closes all the sub-menus.
     *
     * @param fabList     Floating Action Button list
     * @param fabSettings Floating Action Button setting
     * @param tvList      Text View list corresponding to the fabList
     */
    public void closeSubMenus(List<FloatingActionButton> fabList, FloatingActionButton fabSettings, List<TextView> tvList) {
        for (FloatingActionButton fab : fabList) {
            fab.setVisibility(View.INVISIBLE);
        }
        for (TextView tv : tvList) {
            tv.setVisibility(View.INVISIBLE);
        }
        fabSettings.setImageResource(R.drawable.ic_settings_black_24dp);
    }

    /**
     * Opens all the sub-menus.
     *
     * @param fabList     Floating Action Button list
     * @param fabSettings Floating Action Button setting
     * @param tvList      Text View list corresponding to the fabList
     */
    public void openSubMenus(List<FloatingActionButton> fabList, FloatingActionButton fabSettings, List<TextView> tvList) {
        for (FloatingActionButton fab : fabList) {
            fab.setVisibility(View.VISIBLE);
        }
        for (TextView tv : tvList) {
            tv.setVisibility(View.VISIBLE);
        }
        fabSettings.setImageResource(R.drawable.ic_close_black_24dp);
    }

}
