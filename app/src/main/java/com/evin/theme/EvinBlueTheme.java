package com.evin.theme;

import com.evin.R;

/**
 * Created by amayababy
 * 2016-05-05
 * 下午11:55
 */
public class EvinBlueTheme implements IEvinTheme {
    @Override
    public int getColorPrimary() {
        return R.color.colorPrimary;
    }

    @Override
    public int getColorPrimaryDark() {
        return R.color.colorPrimaryDark;
    }

    @Override
    public int getColorAccent() {
        return R.color.colorAccent;
    }

    @Override
    public int getPoiItemBgNormal() {
        return R.drawable.poi_item_select;
    }

    @Override
    public int getPoiItemBgSelect() {
        return R.drawable.poi_item_normal;
    }

    @Override
    public int getLocationPin() {
        return R.mipmap.location_pin;
    }

    @Override
    public int getToastBackgroundRes() {
        return getColorPrimaryDark();
    }

    @Override
    public int getTextColorSelector() {
        return R.color.gray_blue_selector;
    }

    @Override
    public int getTextViewBackgroundResouce() {
        return R.drawable.gray_blue_bg_selector;
    }

    @Override
    public int getBotFocus() {
        return getColorPrimary();
    }

    @Override
    public int getProgressBarRes() {
        return R.drawable.abs__progress_medium_holo;
    }

    @Override
    public int getEditTextBg() {
        return R.drawable.amaya_edit_bg;
    }
}
