package com.pictest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;

import com.pictest.util.ArrayAdapterFacebookAlbums;
import com.pictest.util.FacebookAlbum;
import com.pictest.util.FacebookBridgeServerUtil;

public class FacebookAlbumsActivity extends Activity {

	private FacebookBridgeServerUtil connection;
	private Activity context;
	private List<FacebookAlbum> list = new ArrayList<FacebookAlbum>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_fb);
		this.context = this;
		
		connection = new FacebookBridgeServerUtil(this);
		if(list.isEmpty())
			new FacebookAlbumsRequest().execute();
		else {
			ArrayAdapterFacebookAlbums adapter = new ArrayAdapterFacebookAlbums(context, list);
			((FancyCoverFlow) findViewById(R.id.FCFAlbumsView)).setAdapter(adapter);
		}
	}
	
	private class FacebookAlbumsRequest extends AsyncTask<Void, Double, Boolean> {
		ProgressDialog Dialog = new ProgressDialog(FacebookAlbumsActivity.this);
		int list_size;
		
		@Override
		protected void onPreExecute() {
			Dialog.setMessage("Loading albums...");
			Dialog.setCancelable(true);
			Dialog.setCanceledOnTouchOutside(true);
			Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			Dialog.setIndeterminate(false);
			Dialog.show();
			Dialog.setProgress(0);
			Log.e("onPreExecute", "all right");
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				JSONObject ret = connection.getFacebookAlbums();
				JSONArray albums = ret.getJSONArray(FacebookBridgeServerUtil.data);
				list_size = albums.length();
				for(int i = 0; i < list_size; i++){
					JSONObject album = albums.getJSONObject(i);
					list.add(new FacebookAlbum(album, connection, context));
					publishProgress((double) i);
					if(!Dialog.isShowing())
						return true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onProgressUpdate(Double... progress) {
			Double div = progress[0] / list_size;
			int pro = (int) (div * 100);
			Dialog.setProgress(pro);
			Log.e("onProgress", "all right: " + progress[0] + " - pro: " + pro);
		}
		
		@Override
		protected void onPostExecute(Boolean cancel) {
			Log.e("onPostExecute", "allright");
			Dialog.dismiss();
			ArrayAdapterFacebookAlbums adapter = new ArrayAdapterFacebookAlbums(context, list);
			((FancyCoverFlow) findViewById(R.id.FCFAlbumsView)).setAdapter(adapter);
			if(cancel){
				Log.e("Cancel", "cancel load");
				Toast.makeText(getApplicationContext(), getResources().
						getString(R.string.WRN_NO_ALL_ALBUMS), Toast.LENGTH_LONG).show();
			}
		}
	}

}
