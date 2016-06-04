package com.evin.theme;

/**
 * Created by amayababy
 * 2016-05-05
 * 下午11:54
 */
public interface IEvinTheme {

    int getColorPrimary();
    int getColorPrimaryDark();
    int getColorAccent();
    int getPoiItemBgNormal();
    int getPoiItemBgSelect();

    int getLocationPin();

    int getToastBackgroundRes();

    int getTextColorSelector();

    int getTextViewBackgroundResouce();


    /**
     * 选择位置页面底部滑动颜色
     */
    int getBotFocus();

    /**
     *
     */
    int getProgressBarRes();


    /**
     *
     *
     */
    int getEditTextBg();
}
