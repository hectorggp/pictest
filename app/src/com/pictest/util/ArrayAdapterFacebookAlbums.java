package com.pictest.util;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;

import com.pictest.R;

public class ArrayAdapterFacebookAlbums extends FancyCoverFlowAdapter {
	private int[] images = { R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, };
	private List<FacebookAlbum> data;
	private Activity context;

	public ArrayAdapterFacebookAlbums(Activity context, List<FacebookAlbum> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getId();
	}

	@Override
	public View getCoverFlowItem(final int position, View reusableView,
			ViewGroup parent) {
		View view = null;
		if(reusableView == null){
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.activity_facebook_album_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.TXTAlbumName = (TextView) view.findViewById(R.id.TXTAlbumName);
			viewHolder.IMVContent = (ImageView) view.findViewById(R.id.IMVContent);
			view.setTag(viewHolder);
		} else {
			view = (View) reusableView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.TXTAlbumName.setText(data.get(position).getName());
		Drawable cover = data.get(position).getCover();
		if(cover == null)
			Log.e("ERR", "cover is null");
		holder.IMVContent.setImageDrawable(cover);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, data.get(position).getName(), Toast.LENGTH_LONG).show();
			}
		});
		view.setLayoutParams(new FancyCoverFlow.LayoutParams(parent.getLayoutParams()));
		Log.e("error", "aca q");
		return view;
	}

	private class ViewHolder {
		TextView TXTAlbumName;
		ImageView IMVContent;
	}
}
