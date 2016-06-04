package com.evin.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.evin.R;
import com.evin.activity.XApplication;
import com.evin.adapter.ImgFolderAdapter;
import com.evin.bean.ImageFloder;

import java.util.List;

public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFloder>
{
	private ListView mListDir;
	private OnImageDirSelected mImageDirSelected;

	public ListImageDirPopupWindow(int width, int height,
								   List<ImageFloder> datas, View convertView)
	{
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews()
	{
		mListDir = (ListView) findViewById(R.id.id_list_dir);
		ImgFolderAdapter adapter = new ImgFolderAdapter(LayoutInflater.from(XApplication.getContext()));
		adapter.addAll(mDatas);
		mListDir.setAdapter(adapter);
//		mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,
//				R.layout.list_dir_item)
//		{
//			@Override
//			public void convert(ViewHolder helper, ImageFloder item)
//			{
//				helper.setText(R.id.id_dir_item_name, item.getName());
//				helper.setImageByUrl(R.id.id_dir_item_image,
//						item.getFirstImagePath());
//				helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
//			}
//		});
	}

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected)
	{
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents()
	{
		mListDir.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id)
			{

				if (mImageDirSelected != null)
				{
					mImageDirSelected.selected(mDatas.get(position));
				}
				ImgFolderAdapter imgFolderAdapter = (ImgFolderAdapter) mListDir.getAdapter();
				imgFolderAdapter.setClickPos(position);
			}
		});
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	public interface OnImageDirSelected {
		void selected(ImageFloder floder);
	}
}
