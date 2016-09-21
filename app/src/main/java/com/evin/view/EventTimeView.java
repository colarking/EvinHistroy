package com.evin.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.evin.R;
import com.evin.activity.XApplication;
import com.evin.bean.EvinTime;
import com.evin.util.TimeUtil;
import com.evin.util.UIUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by amayababy
 * 2016-06-05
 * 下午1:54
 */
public class EventTimeView extends TextView implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    private Calendar calendar = Calendar.getInstance();
    private EvinTime time;
    private String formator;

    public EventTimeView(Context context) {
        super(context);
        init(context);
    }

    public EventTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EventTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EventTimeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setText(R.string.choose_time);
        setGravity(Gravity.CENTER);
        int dp10 = UIUtil.dip2px(10);
        setPadding(0, dp10, 0, dp10);
        setOnClickListener(this);
        setBackgroundResource(R.drawable.trans_gray_bg_selector);

    }

    public EvinTime getTime() {
        return time;
    }

    public void setTime(EvinTime time) {
        this.time = time;
        calendar.setTimeInMillis(time.getTimeStamp());
    }

    @Override
    public void onClick(View view) {
        CalendarDatePickerDialogFragment datePicker = new CalendarDatePickerDialogFragment().setOnDateSetListener(this).setPreselectedDate(calendar.get(Calendar.ERA), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "FRAG_TAG_DATE_PICKER");

    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int ADorBC, int year, int monthOfYear, int dayOfMonth) {
        if (time == null) time = new EvinTime();
        time.setBcTime(ADorBC == GregorianCalendar.BC);
        calendar.set(Calendar.ERA, ADorBC);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        time.setTimeStamp(calendar.getTimeInMillis());
        time.setTimeYear((ADorBC == GregorianCalendar.BC ? -1 : 1) * year);
        long insert = XApplication.getDaoSession().getEvinTimeDao().insertOrReplace(time);
        time.setId(insert);

        updateText();
    }

    public void updateText() {
        String text = null;
        if (time.getBcTime()) {
            text = getContext().getString(R.string.year_bc) + TimeUtil.parseTime(TimeUtil.PATTERN_3, time.getTimeStamp()).replaceAll("^(0+)", "");
        } else {
            text = TimeUtil.parseTime(TimeUtil.PATTERN_3, time.getTimeStamp()).replaceAll("^(0+)", "");
        }

        if (TextUtils.isEmpty(formator)) {
            setText(text);
        } else {
            setText(String.format(formator, text));
        }
    }

    public void setTextFormater(String format) {
        this.formator = format;
    }


}
