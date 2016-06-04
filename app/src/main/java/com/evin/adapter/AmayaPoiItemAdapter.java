package com.evin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.evin.R;
import com.evin.bean.AmayaPoi;
import com.evin.theme.EvinTheme;
import com.evin.util.SpeedScrollListener;

import java.util.ArrayList;
import java.util.List;

public class AmayaPoiItemAdapter<T extends AmayaPoi> extends AbsPlusListAdapter {


    private ArrayList<AmayaPoi> beans = new ArrayList<AmayaPoi>();
    private Context mContext;
    private int currentPos = -1;

    public AmayaPoiItemAdapter(Context context, SpeedScrollListener scrollListener) {
        super(context, scrollListener);
        this.mContext = context;
    }

    public AmayaPoiItemAdapter(Context context, SpeedScrollListener scrollListener, ArrayList<AmayaPoi> list) {
        super(context, scrollListener);
        this.mContext = context;
        beans = list;
    }

    public void addAll(List<AmayaPoi> list) {
        if (beans == null) {
            beans = new ArrayList<AmayaPoi>();
        }
        beans.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(List<AmayaPoi> list, boolean clear, int index) {
        if (list == null || list.size() == 0) return;
        currentPos = index;
        if (beans != null) {
            if (clear) beans.clear();
            beans.addAll(list);
        } else {
            beans = new ArrayList<AmayaPoi>();
            beans.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    protected View getRowView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.amaya_topic_poi_item, null);
//            AbsListView.LayoutParams al = (AbsListView.LayoutParams) convertView.getLayoutParams();
//            if(al == null){
//                if(showPic){
//
//                    al = new AbsListView.LayoutParams(width,gridHeight);
//                }else{
//                    al = new AbsListView.LayoutParams(width,AbsListView.LayoutParams.WRAP_CONTENT);
//                }
//
//            }
//            convertView.setLayoutParams(al);
            viewHolder = new ViewHolder();
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.amaya_poi_name);
//          viewHolder.timeView = (TextView) convertView.findViewById(R.id.topic_create_time);
            viewHolder.distanceView = (TextView) convertView.findViewById(R.id.amaya_poi_distance);
//          viewHolder.titleView = (TextView) convertView.findViewById(R.id.topic_create_title);
//          viewHolder.imgView= (ImageView) convertView.findViewById(R.id.topic_create_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AmayaPoi alp = beans.get(position);
        if (currentPos == position) {
            convertView.setBackgroundResource(EvinTheme.instance().getPoiItemBgSelect());
        } else {
            convertView.setBackgroundResource(EvinTheme.instance().getPoiItemBgNormal());
        }

        viewHolder.nameView.setText(alp.name);
        viewHolder.distanceView.setText(alp.distance);

        return convertView;
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public AmayaPoi getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        beans.clear();
//        notifyDataSetChanged();
    }

    public void add(AmayaPoi ap) {
        beans.add(ap);
    }

    public void setSelected(int pos) {
        currentPos = pos;
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<PoiItem> poiItems, boolean clear) {
        if (beans != null) {
            if (clear) beans.clear();
            for (int i = 0; i < poiItems.size(); i++) {
                PoiItem poiItem = poiItems.get(i);
                beans.add(new AmayaPoi(poiItem.toString(), poiItem.getLatLonPoint()));
            }
        } else {
            beans = new ArrayList<AmayaPoi>();
            for (int i = 0; i < poiItems.size(); i++) {
                PoiItem poiItem = poiItems.get(i);
                beans.add(new AmayaPoi(poiItem.toString(), poiItem.getLatLonPoint()));
            }
        }
        notifyDataSetChanged();
    }


    public ArrayList<AmayaPoi> getBeans() {
        return beans;
    }


    static class ViewHolder {
        TextView nameView, timeView, distanceView, titleView;
        ImageView imgView;
    }
}
