package pictest.adapters;

import java.util.List;

import org.holoeverywhere.widget.Toast;

import pictest.activities.PhotoViewActivity;
import pictest.objects.FbAlbum;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;

import com.pictest.R;

public class PhotoElementAdapter extends FancyCoverFlowAdapter {
	private List<FbAlbum> data;
	private Activity context;
	private Drawable defaultImage;

	public PhotoElementAdapter(Activity context, List<FbAlbum> data) {
		this.context = context;
		this.data = data;
		defaultImage = context.getResources().getDrawable(
				R.drawable.ic_launcher);
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
		final FbAlbum album = data.get(position);
		View view = null;
		if (reusableView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator
					.inflate(R.layout.activity_facebook_album_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.TXTAlbumName = (TextView) view
					.findViewById(R.id.TXTAlbumName);
			viewHolder.IMVContent = (ImageView) view
					.findViewById(R.id.IMVContent);
			view.setTag(viewHolder);
		} else {
			view = (View) reusableView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.TXTAlbumName.setText(album.getName());
		Drawable cover = album.getFbPhotoCover() != null
				&& album.getFbPhotoCover().getImage() != null ? album
				.getFbPhotoCover().getImage() : defaultImage;
		if (cover == null)
			Log.e("ERR", "cover is null");
		holder.IMVContent.setImageDrawable(cover);
		holder.TXTAlbumName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (album.getFbPhotoCover() != null
						&& album.getFbPhotoCover().getImage() != null) {
					Log.d("album", album.getName());
					PhotoViewActivity.currentAlbum = album;
					context.startActivity(new Intent(context,
							PhotoViewActivity.class));
				} else {
					Log.e("ERRor", "se intenta acceder a album no cargado");
					Toast.makeText(
							context,
							context.getResources().getString(
									R.string.WRN_NO_LOAD_ALBUM),
							Toast.LENGTH_LONG).show();
				}
			}
		});
		view.setLayoutParams(new FancyCoverFlow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		return view;
	}

	private class ViewHolder {
		TextView TXTAlbumName;
		ImageView IMVContent;
	}
}
