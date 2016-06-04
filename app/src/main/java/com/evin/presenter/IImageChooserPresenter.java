package com.evin.presenter;

import com.evin.adapter.AmayaImgsAdapter;
import com.evin.bean.ImageFloder;

import java.util.ArrayList;

/**
 * Created by amayababy
 * 2015-07-16
 * 下午6:13
 */
public interface IImageChooserPresenter extends IAmayaPresenter {


    void loadImages();

    AmayaImgsAdapter getAdapter();

    void setMaxCount(int maxCount);

    void notifyDataSetChanged(String floder);

    ArrayList<ImageFloder> getmImageFloders();

    ImageFloder getFirstFolder();

    void insertNewPicture(String path);
}
