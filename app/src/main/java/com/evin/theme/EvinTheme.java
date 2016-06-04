package com.evin.theme;

import com.evin.R;

/**
 * Created by amayababy
 * 2016-05-05
 * 下午5:10
 */
public class EvinTheme implements IEvinTheme{

    private static IEvinTheme evinTheme;
    public static final int THEME_BLUE=520;
    private EvinTheme(){
    }

    public static IEvinTheme instance(){
        if(evinTheme == null) evinTheme = new EvinBlueTheme();
        return evinTheme;
    }
    public static IEvinTheme instance(int theme){
        updateTheme(theme);
        return evinTheme;
    }
    /**
     * 初始化皮肤
     */
    public static void init() {
        if(evinTheme == null) evinTheme = new EvinBlueTheme();
    }

    private static void updateTheme(int theme){
        switch (theme){
            default:
            case THEME_BLUE:
                if(!(evinTheme instanceof EvinBlueTheme)) evinTheme = new EvinBlueTheme();
                break;
        }
    }

    public int getPoiItemBgSelect(){
        return evinTheme.getPoiItemBgSelect();
    }

    @Override
    public int getLocationPin() {
        return evinTheme.getLocationPin();
    }

    @Override
    public int getToastBackgroundRes() {
        return evinTheme.getToastBackgroundRes();
    }

    @Override
    public int getTextColorSelector() {
        return evinTheme.getTextColorSelector();
    }

    @Override
    public int getTextViewBackgroundResouce() {
        return evinTheme.getTextViewBackgroundResouce();
    }

    @Override
    public int getBotFocus() {
        return evinTheme.getBotFocus();
    }

    @Override
    public int getProgressBarRes() {
        return evinTheme.getProgressBarRes();
    }

    @Override
    public int getEditTextBg() {
        return evinTheme.getEditTextBg();
    }

    @Override
    public int getColorPrimary() {
        return 0;
    }

    @Override
    public int getColorPrimaryDark() {
        return 0;
    }

    @Override
    public int getColorAccent() {
        return 0;
    }

    public int getPoiItemBgNormal() {
        return evinTheme.getPoiItemBgNormal();
    }
}
