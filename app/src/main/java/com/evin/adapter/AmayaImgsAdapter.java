package com.evin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.evin.R;
import com.evin.activity.XApplication;
import com.evin.util.AmayaConstants;
import com.evin.util.ToastUtil;
import com.evin.util.UIUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amayababy
 * 2015-07-16
 * 下午4:34
 */
public class AmayaImgsAdapter extends AmayaAdapter<String> {
    private final DisplayImageOptions dio;
    private final Drawable drawableUnselect, drawableSelect;
    private final RelativeLayout.LayoutParams imgLP;
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public ArrayList<String> mSelectedImage = new ArrayList<String>();
    private int maxCount;

    private LayoutInflater inflater;
    private String dirPath;

    public AmayaImgsAdapter(Context context) {
        super(null);
        inflater = LayoutInflater.from(context);
        int width = (UIUtil.amayaWidth - UIUtil.dip2px(6)) / 3;
        imgLP = new RelativeLayout.LayoutParams(width, width);
        Drawable drawable = UIUtil.getDrawable(R.drawable.loading_icon);
        drawableUnselect = context.getResources().getDrawable(R.mipmap.select_no);
        drawableSelect = context.getResources().getDrawable(R.mipmap.select_yes);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        dio = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawable)
                .showImageForEmptyUri(drawable)
                .showImageOnFail(drawable)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .decodingOptions(options)
                .considerExifParams(true)
                .build();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_item, null);
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.id_item_image);
            viewHolder.imgView.setLayoutParams(imgLP);

            viewHolder.checkBtn = (ImageButton) convertView.findViewById(R.id.id_item_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkBtn.setImageDrawable(drawableUnselect);
        viewHolder.checkBtn.setColorFilter(null);
        //设置ImageView的点击事件
        viewHolder.checkBtn.setOnClickListener(new View.OnClickListener() {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                String item = getItem(position);
                if (mSelectedImage.contains(item)) {
                    // 已经选择过该图片
                    mSelectedImage.remove(item);
                    viewHolder.checkBtn.setImageDrawable(drawableUnselect);
                    viewHolder.imgView.setColorFilter(null);
                } else if (mSelectedImage.size() >= maxCount) {
                    ToastUtil.show(UIUtil.getString(R.string.images_count_max, maxCount), true);
                } else {
                    // 未选择该图片
                    mSelectedImage.add(0, item);
                    viewHolder.checkBtn.setImageDrawable(drawableSelect);
                    viewHolder.imgView.setColorFilter(Color.parseColor("#77000000"));
                }
            }
        });

        String item = getItem(position);
        if (mSelectedImage.contains(item)) {
            viewHolder.checkBtn.setImageDrawable(drawableSelect);
            viewHolder.imgView.setColorFilter(Color.parseColor("#77000000"));
        } else {
            viewHolder.checkBtn.setImageDrawable(drawableUnselect);
            viewHolder.imgView.setColorFilter(null);
        }
        if (position == 0) {
            viewHolder.checkBtn.setVisibility(View.GONE);
            viewHolder.imgView.setImageResource(R.mipmap.pic_add_camera);
        } else if (item.startsWith(AmayaConstants.AMAYA_DIR_CACHE)) {
            viewHolder.checkBtn.setVisibility(View.VISIBLE);
            XApplication.getImageLoader().displayImage(AmayaConstants.PREFIX_FILE + getItem(position), viewHolder.imgView, dio);
        } else {
            viewHolder.checkBtn.setVisibility(View.VISIBLE);
            XApplication.getImageLoader().displayImage(AmayaConstants.PREFIX_FILE + getItem(position), viewHolder.imgView, dio);
        }

        return convertView;
    }

    public void setFileDir(String dir) {
        this.dirPath = dir;
    }


    public List<String> getSelectedImage() {
        return mSelectedImage;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void clearSelectedList() {
        mSelectedImage.clear();
    }

    public String getDirPath() {
        return dirPath;
    }

    static class ViewHolder {
        public ImageView imgView;
        public ImageButton checkBtn;
    }
}
