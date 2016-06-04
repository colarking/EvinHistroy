package com.codetroopers.betterpickers.calendardatepicker;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.R;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.GregorianCalendar;

/**
 * Created by amayababy
 * 2016-05-25
 * 上午2:43
 */
public class YearChooseLayout extends RelativeLayout implements View.OnClickListener, TextWatcher {
    private TextView textADView,textBCView,yearView;
    private EditText editYearView;
//    private View lineView;
    private AnimationSet as;
    private boolean showBC = true;
    private int colorBlue;
    private YearClickListener listener;
    private int year;

    public YearChooseLayout(Context context) {
        super(context);
        init(context);
    }

    public YearChooseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public YearChooseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public YearChooseLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
//        addViewBC(context);
        addViewYearEdit(context);
        addViewYear(context);
        addViewAD(context);
//        addViewLine(context);
    }

    private void addViewYearEdit(Context context) {
        editYearView = new EditText(context);
        editYearView.setGravity(Gravity.CENTER);
        editYearView.setSingleLine(true);
        editYearView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editYearView.setBackgroundResource(R.drawable.transparent);
        editYearView.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        editYearView.setMaxEms(4);
        editYearView.setHint(R.string.year_change);
        editYearView.setFocusableInTouchMode(true);
        editYearView.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
//        editYearView.setBackgroundColor(Color.MAGENTA);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        editYearView.setId(R.id.id_text_year_edit);
        editYearView.setPadding(dip2px(context,20),0,0,0);
//        lp.addRule(RelativeLayout.RIGHT_OF,R.id.id_text_ad);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        addView(editYearView,lp);
        editYearView.addTextChangedListener(this);
        editYearView.setVisibility(INVISIBLE);
    }

    private void addViewYear(Context context) {
        yearView = new TextView(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        yearView.setId(R.id.id_text_year);
        yearView.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
        yearView.setHint("1900");
        yearView.setGravity(Gravity.CENTER);
//        lp.addRule(RelativeLayout.RIGHT_OF,R.id.id_text_ad);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        addView(yearView,lp);
        yearView.setOnClickListener(this);
    }

    private void addViewBC(Context context) {
        textBCView= new TextView(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textBCView.setText("BC");
        textBCView.setGravity(Gravity.CENTER);
        textBCView.setId(R.id.id_text_bc);
        lp.topMargin = lp.bottomMargin = dip2px(context,3);
        int dp10= dip2px(context,10);
        int dp5= dp10/2;
        textBCView.setPadding(dp10,dp5,dp10,dp5);
        addView(textBCView,lp);
        textBCView.setOnClickListener(this);
    }
/*
    private void addViewLine(Context context) {
        lineView = new View(context);
        colorBlue = context.getResources().getColor(R.color.blue);
        lineView.setBackgroundColor(colorBlue);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, dip2px(context,3));
        lp.addRule(RelativeLayout.BELOW,R.id.id_text_ad);
        lp.addRule(RelativeLayout.ALIGN_LEFT,R.id.id_text_ad);
        lp.addRule(RelativeLayout.ALIGN_RIGHT,R.id.id_text_ad);
        addView(lineView,lp);
    }*/

    private void addViewAD(Context context) {
        textADView = new TextView(context);
        textADView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
        textADView.setId(R.id.id_text_ad);
        int dp10= dip2px(context,10);
        int dp5= dp10/2;
        textADView.setPadding(dp10,dp5,dp10,dp5);
        addView(textADView,lp);
        textADView.setOnClickListener(this);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.id_text_ad){
//            animateLine();
            animateText();
        }else if(v.getId() == R.id.id_text_year){
            yearView.setVisibility(GONE);
            editYearView.setVisibility(VISIBLE);
            editYearView.requestFocus();
            editYearView.findFocus();
            editYearView.performClick();
            yearClick();
        }
    }

    private void animateLine() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            Keyframe kf0 = Keyframe.ofFloat(0f, 1f);
            Keyframe kf1 = Keyframe.ofFloat(.2f, 0.8f);
            Keyframe kf2 = Keyframe.ofFloat(.5f, .2f);
            Keyframe kf3 = Keyframe.ofFloat(.6f, .0f);
            Keyframe kf4 = Keyframe.ofFloat(.7f, .1f);
            Keyframe kf5 = Keyframe.ofFloat(1f, 1f);
            PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe("scaleX", kf0, kf1, kf2, kf3, kf4, kf5);
//            ObjectAnimator startCenterAnim = ObjectAnimator.ofPropertyValuesHolder(lineView, pvhScaleX);
//            startCenterAnim.setInterpolator(new LinearOutSlowInInterpolator());
//            startCenterAnim.setDuration(1000);
//            startCenterAnim.start();
            animateText();
        }
    }

    private void animateText() {
        if (as == null) {
            ScaleAnimation sa = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            as = new AnimationSet(true);
            as.addAnimation(sa);
            as.addAnimation(ra);
            as.setDuration(250);
            as.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ScaleAnimation sa2 = new ScaleAnimation(0f, 1.0f, 0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    RotateAnimation ra2 = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    AnimationSet as2 = new AnimationSet(true);
                    as2.addAnimation(sa2);
                    as2.addAnimation(ra2);
                    as2.setDuration(250);
                    textADView.startAnimation(as2);
                    showBC = !showBC;
                    textADView.setText(showBC?R.string.year_bc:R.string.year_ad);
                    if(yearView.getVisibility() ==VISIBLE){
                        yearBack();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        textADView.startAnimation(as);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String trim = editYearView.getText().toString().trim();

        if(!TextUtils.isEmpty(trim) && trim.length() == 4){
            setYearText(trim);
            yearView.setVisibility(VISIBLE);
            editYearView.setVisibility(GONE);
            yearBack();
        }
    }

    private void yearBack() {
        if(listener != null) listener.yearChoose(showBC?GregorianCalendar.BC:GregorianCalendar.AD,year);
    }
    private void yearClick() {
        if(listener != null) listener.yearClick();
    }

    public void setTextColor(int textColor) {
        yearView.setTextColor(textColor);
    }

    public void setOnClickBackListener(YearClickListener listener){
        this.listener = listener;
    }

    public void setYearText(String yearText) {
        year = Integer.parseInt(yearText);
//        editYearView.setText(yearText);
        yearView.setText(yearText);
        if(editYearView.getVisibility() ==VISIBLE){
            editYearView.setVisibility(GONE);
            yearView.setVisibility(VISIBLE);
        }
    }

    public void setADorBC(int ADorBC){
        showBC = ADorBC == GregorianCalendar.BC;
        textADView.setText(showBC?R.string.year_bc:R.string.year_ad);
    }
    public void setYearText(int year) {
        yearView.setText(Integer.toString(year));
        editYearView.setText(Integer.toString(year));

        this.year = year;
        if(editYearView.getVisibility() ==VISIBLE){
            editYearView.setVisibility(GONE);
            yearView.setVisibility(VISIBLE);
        }
    }

    public int getYear() {
        if(editYearView.getVisibility() == VISIBLE){
            String trim = editYearView.getText().toString().trim();
            if(!TextUtils.isEmpty(trim)){
                year = Integer.parseInt(trim);
            }
        }
        return year;
    }

    public static interface YearClickListener{
        void yearClick();
        void yearChoose(int ADorBC,int year);
    }
}
