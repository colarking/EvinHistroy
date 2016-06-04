package com.evin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evin.R;
import com.evin.activity.XApplication;
import com.evin.bean.ImageFloder;
import com.evin.util.AmayaConstants;
import com.evin.util.UIUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by amayababy
 * 2015-07-16
 * 下午8:25
 */
public class ImgFolderAdapter extends AmayaAdapter<ImageFloder> {
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private int clickPos = 0;

    public ImgFolderAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        imageLoader = XApplication.getImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_dir_item, null);
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.id_dir_item_name);
            viewHolder.countView = (TextView) convertView.findViewById(R.id.id_dir_item_count);
            viewHolder.selectView = convertView.findViewById(R.id.id_dir_item_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageFloder item = getItem(position);
        viewHolder.nameView.setText(item.getName());
        viewHolder.countView.setText(UIUtil.getString(R.string.images_count, item.getCount()));
        viewHolder.selectView.setVisibility(clickPos == position ? View.VISIBLE : View.INVISIBLE);
        imageLoader.displayImage(AmayaConstants.PREFIX_FILE + item.getFirstImagePath(), viewHolder.imgView);
        return convertView;
    }

    public void setClickPos(int pos) {
        this.clickPos = pos;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        ImageView imgView;
        TextView nameView, countView;
        View selectView;
    }
}
