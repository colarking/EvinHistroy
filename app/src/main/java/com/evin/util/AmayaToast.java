
package com.evin.util;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.evin.activity.XApplication;
import com.evin.theme.EvinTheme;

/**
 * Created with IntelliJ IDEA.
 * User: colarking
 * Date: 13-12-22
 * Time: 下午3:15
 * To change this template use File | Settings | File Templates.
 */
public class AmayaToast {

        private static Toast toast = new Toast(XApplication.getContext());

        static {
                toast.setGravity(Gravity.BOTTOM, 0, UIUtil.dip2px(48));
        }

        public static void show(int resId, boolean shortShow) {
                TextView tv = new TextView(XApplication.getContext());
                int height = UIUtil.dip2px(10);
                tv.setPadding(height, height, height, height);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv.setBackgroundResource(EvinTheme.instance().getToastBackgroundRes());
                toast.setView(tv);
                toast.setDuration(shortShow ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
                tv.setText(resId);
                toast.show();
        }

        public static void show(String text, boolean shortShow) {
                TextView tv = new TextView(XApplication.getContext());
                int height = UIUtil.dip2px(10);
                tv.setPadding(height, height, height, height);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv.setBackgroundResource(EvinTheme.instance().getToastBackgroundRes());
                toast.setView(tv);
                toast.setDuration(shortShow ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
                tv.setText(text);
                toast.show();
        }

        public static void release() {
//        tv = null;
                toast = null;
        }

}