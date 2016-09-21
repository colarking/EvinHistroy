package com.evin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import com.evin.R;
import com.evin.activity.ImgChooserActivity;
import com.evin.activity.XApplication;
import com.evin.bean.EvinEvent;
import com.evin.bean.EvinTime;
import com.evin.util.AmayaConstants;
import com.evin.util.AmayaEvent;
import com.evin.util.AmayaSPUtil;
import com.evin.util.UIUtil;
import com.evin.view.EventTimeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amayababy
 * 2016-06-04
 * 下午3:06
 */
public class EditEventFragment extends BaseFragment implements MenuItem.OnMenuItemClickListener {


    @BindView(R.id.event_time_start)
    EventTimeView timeStartView;
    @BindView(R.id.event_time_end)
    EventTimeView timeEndView;
    @BindView(R.id.event_image)
    ImageView imageView;
    @BindView(R.id.event_type)
    MultiAutoCompleteTextView typeView;
    @BindView(R.id.event_name)
    EditText nameView;
    @BindView(R.id.event_desc)
    EditText contentView;
    private EvinEvent bean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_edit_event, null);
        view.findViewById(R.id.event_time_start).requestFocus();
        ButterKnife.bind(this, view);
        timeStartView.setText(R.string.choose_time_start_tip);
        timeEndView.setText(R.string.choose_time_end_tip);
        timeStartView.setTextFormater(getString(R.string.choose_time_start));
        timeEndView.setTextFormater(getString(R.string.choose_time_end));
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = UIUtil.amayaWidth / 2;
        imageView.setLayoutParams(params);

        setHasOptionsMenu(true);
        initAutoTypeView();
        return view;
    }

    private void initAutoTypeView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1, getResources().getStringArray(
                R.array.type_string));
        typeView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        initBean();
        initUI();
    }

    private void initBean() {
        long eventId = AmayaSPUtil.getLong("eventId", -1);
        if (eventId > -1) {
            bean = XApplication.getDaoSession().getEvinEventDao().load(eventId);
        } else {
            bean = new EvinEvent();
            long insert = XApplication.getDaoSession().getEvinEventDao().insert(bean);
            bean.setId(insert);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        saveAll();
    }

    @OnClick(R.id.event_image)
    public void onClick(View v) {
        jumpToChooser();
    }

    private void jumpToChooser() {
        Intent intent = new Intent(getActivity(), ImgChooserActivity.class);
        intent.putExtra("max", 1);
        intent.putExtra("hash", hashCode());
        UIUtil.startActivity(getActivity(), intent);
    }


    @Subscribe
    public void onEventMainThread(AmayaEvent.ImageChooseEvent event) {
        if (event.requestHash != hashCode()) return;
        for (int i = 0; i < event.pics.size(); i++) {
            String path = event.pics.get(i);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            bean.setHeaderImage(path);
            XApplication.getImageLoader().displayImage(AmayaConstants.PREFIX_FILE + path, imageView);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem add = menu.add(R.string.ok);
        add.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        add.setOnMenuItemClickListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        saveAll();
        getActivity().finish();
        return false;
    }

    private void saveAll() {
        saveTextAll();
        EvinTime time = timeStartView.getTime();
        bean.setTimeStart(time);
        EvinTime time2 = timeStartView.getTime();
        bean.setTimeEnd(time2);

    }

    private void saveTextAll() {
        String type = typeView.getText().toString().trim();
        if (!TextUtils.isEmpty(type)) {
            bean.setType(type);
        }
        String name = nameView.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            bean.setName(name);
        }
        String content = contentView.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            bean.setContent(content);
        }
        XApplication.getDaoSession().getEvinEventDao().insertOrReplace(bean);
    }

    public void initUI() {
        if (!TextUtils.isEmpty(bean.getHeaderImage())) {
            XApplication.getImageLoader().displayImage(AmayaConstants.PREFIX_FILE + bean.getHeaderImage(), imageView);
        }
        if (!TextUtils.isEmpty(bean.getName())) {
            nameView.setText(bean.getName());
        }
        if (!TextUtils.isEmpty(bean.getContent())) {
            contentView.setText(bean.getContent());
        }
        if (!TextUtils.isEmpty(bean.getType())) {
            typeView.setText(bean.getType());
        }

        if (bean.getTimeStart() != null) {
            timeStartView.setTime(bean.getTimeStart());
            timeStartView.updateText();
        }
        if (bean.getTimeEnd() != null) {
            timeEndView.setTime(bean.getTimeEnd());
            timeEndView.updateText();
        }

    }
}
