package com.example.financialManagement.processor;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import financialManagement.R;

public class FabProcessor  {

    public void closeSubMenus(List<FloatingActionButton> fabList, FloatingActionButton fabSettings, List<TextView> tvList) {
        for (FloatingActionButton fab: fabList) {
            fab.setVisibility(View.INVISIBLE);
        }
        for (TextView tv: tvList) {
            tv.setVisibility(View.INVISIBLE);
        }
        fabSettings.setImageResource(R.drawable.ic_settings_black_24dp);
    }

    public void openSubMenus(List<FloatingActionButton> fabList, FloatingActionButton fabSettings, List<TextView> tvList) {
        for (FloatingActionButton fab: fabList) {
            fab.setVisibility(View.VISIBLE);
        }
        for (TextView tv: tvList) {
            tv.setVisibility(View.VISIBLE);
        }
        fabSettings.setImageResource(R.drawable.ic_close_black_24dp);
    }

}
