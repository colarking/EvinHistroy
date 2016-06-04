package com.evin.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.evin.R;
import com.evin.activity.XApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amayababy
 * 2015-07-08
 * 上午11:45
 */
public class UIUtil {
    private static final float AMAYA_WIDTH = 720;
    private static final float AMAYA_HEIGHT = 1280;
    public static int amayaWidth = (int) AMAYA_WIDTH, amayaHeight = (int) AMAYA_HEIGHT;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    static float scale;
    static float fontScale;

    public static int dip2px(float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue) {
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dip2sp(float dipValue) {
        return (int) (dip2px(dipValue) / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    public static void initSystemParam(float density, float scaledDensity) {
        scale = density;
        fontScale = scaledDensity;

    }

    public static void initAmayaParams(int width, int height) {
        if (width > height) {
            amayaWidth = height;
            amayaHeight = width;
        } else {
            amayaWidth = width;
            amayaHeight = height;
        }
    }

    public static int getHeight(int height) {
        return (int) (AMAYA_HEIGHT / amayaHeight * height);
    }

    public static int getCommonWidth(int width) {
        return dip2px(AMAYA_WIDTH / amayaWidth * width);
    }

    public static int getCommonHeight(int height) {
        if (AMAYA_HEIGHT > amayaHeight) {

            return dip2px(amayaHeight / AMAYA_HEIGHT * height);
        } else {

            return dip2px(AMAYA_HEIGHT / amayaHeight * height);
        }
    }


    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 设置Selector。
     */
    public static StateListDrawable generateSelector(Context context, int idNormal, int idPressed, int idFocused,
                                                     int idUnable) {

        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
        Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_window_focused}, unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }


    public static ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }


//    public static void startShareActivity(Context context, String title, String content, String imgpath, String target) {
//        Intent intent = new Intent(context, ShareActivity.class);
//        intent.putExtra("title", title);
//        intent.putExtra("content", content);
//        intent.putExtra("path", imgpath);
//        intent.putExtra("targetUrl", TextUtils.isEmpty(target) ? "http://www.chuangbar.me" : target);
//        startActivity(context, intent);
////        if (context instanceof Activity) {
////            ((Activity) context).overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
////        }
//
//    }
    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static MaterialDialog showLoadiingDialog(Activity activity, int loadingRes) {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .content(loadingRes)
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(false)
                .show();
        return dialog;
    }

    public static Drawable getDrawable(int drawable) {
        Drawable drawable1 = XApplication.getContext().getResources().getDrawable(drawable);
        return drawable1;
    }

    public static String getString(int stringRes, int text) {
        return XApplication.getContext().getResources().getString(stringRes, text);
    }

    public static String getString(int stringRes, String text) {
        return XApplication.getContext().getResources().getString(stringRes, text);
    }


    public static boolean checkNetworkOrToast() {
        boolean available = NetUtil.isNetworkAvailable();
        if (!available) ToastUtil.show(R.string.amaya_net_error, true);
        return available;
    }

    public static void showEditToast() {
        ToastUtil.show("请填写分享的内容", true);
    }

    public static DisplayImageOptions getDIO(int resource) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(resource)
                .showImageOnFail(resource)
                .showImageOnLoading(resource)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .build();
        return options;
    }

    public static DisplayImageOptions getCicleDIO(float radius) {
        return getCicleDIO(radius, R.drawable.loading_icon);

    }

    public static DisplayImageOptions getCicleDIO(float radius, int res) {
        return new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(UIUtil.dip2px(radius)))
                .showImageOnFail(res)
                .considerExifParams(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(res)
                .showImageOnLoading(res)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }




    public static Pair<View, String>[] createSafeTransitionParticipants(@NonNull Activity activity,
                                                                        boolean includeStatusBar, @Nullable Pair... otherParticipants) {
        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb
        View decor = activity.getWindow().getDecorView();
        View statusBar = null;
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        }
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        // Create pair of transition participants.
        List<Pair> participants = new ArrayList<>(3);
        addNonNullViewToTransitionParticipants(statusBar, participants);
        addNonNullViewToTransitionParticipants(navBar, participants);
        // only add transition participants if there's at least one none-null element
        if (otherParticipants != null && !(otherParticipants.length == 1
                && otherParticipants[0] == null)) {
            participants.addAll(Arrays.asList(otherParticipants));
        }
        return participants.toArray(new Pair[participants.size()]);
    }

    private static void addNonNullViewToTransitionParticipants(View view, List<Pair> participants) {
        if (view == null) {
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            participants.add(new Pair<>(view, view.getTransitionName()));
        }
    }




    public static boolean grantPermission(Activity activity, String permission, int requestCode) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int hasWriteContactsPermission = activity.checkSelfPermission(permission);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(new String[]{permission}, requestCode);
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (NoSuchMethodError e) {
            return true;
        }
    }

    public static boolean grantPermission(Activity activity, String[] permission, int requestCode) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (permission != null && permission.length > 0) {
                    for (int i = 0; i < permission.length; i++) {
                        int hasWriteContactsPermission = activity.checkSelfPermission(permission[i]);
                        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                            activity.requestPermissions(permission, requestCode + i);
                            return false;
                        } else {
                            return true;
                        }
                    }
                    return true;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (NoSuchMethodError e) {
            return true;
        }
    }

    public static boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Manifest.permission.WRITE_EXTERNAL_STORAGE.hashCode()) {
            return checkPermission(permissions[0], grantResults[0], Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else if(requestCode == Manifest.permission.CALL_PHONE.hashCode()){
            return checkPermission(permissions[0], grantResults[0], Manifest.permission.CALL_PHONE);
        }else{
            return false;
        }
    }

    private static boolean checkPermission(String permission, int grantResult, String writeExternalStorage) {
        if (permission.equals(writeExternalStorage) && grantResult == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ToastUtil.show(R.string.shouquan_cancel, true);
            return false;
        }
    }


}
