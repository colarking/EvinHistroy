/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evin.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.evin.R;
import com.evin.fragment.BaseFragment;
import com.evin.view.AmayaEmptyView;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerTabListViewFragment extends BaseFragment {

    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for(int i=0;i<10;i++){
                adapter.add(i+"--"+getString(R.string.title_choose_img));
            }
        }
    };
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);


        AmayaEmptyView emptyView = (AmayaEmptyView) view.findViewById(R.id.empty_view);
        Activity parentActivity = getActivity();
        final ObservableListView listView = (ObservableListView) view.findViewById(R.id.scroll);
        listView.setEmptyView(emptyView);
        setDummyDataWithHeader(listView, inflater.inflate(R.layout.padding, listView, false));

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            // Scroll to the specified position after layout
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_INITIAL_POSITION)) {
                final int initialPosition = args.getInt(ARG_INITIAL_POSITION, 0);
                ScrollUtils.addOnGlobalLayoutListener(listView, new Runnable() {
                    @Override
                    public void run() {
                        // scrollTo() doesn't work, should use setSelection()
                        listView.setSelection(initialPosition);
                    }
                });
            }

            // TouchInterceptionViewGroup should be a parent view other than ViewPager.
            // This is a workaround for the issue #117:
            // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
            listView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.root));
            listView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        handler.sendEmptyMessageDelayed(0,5000);
        return view;
    }

    protected void setDummyDataWithHeader(ListView listView, View headerView) {
        listView.addHeaderView(headerView);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getDummyData());
        listView.setAdapter(adapter);
    }

    public List<String> getDummyData() {
        List<String> dummyData = new ArrayList<>();
//        for(int i=0;i<10;i++){
//            dummyData.add(i+"--"+getString(R.string.title_choose_img));
//        }
        return dummyData;
    }
}
