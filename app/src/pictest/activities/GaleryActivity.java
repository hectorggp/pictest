package pictest.activities;

import java.util.ArrayList;

import org.holoeverywhere.app.ProgressDialog;

import pictest.adapters.PhotoElementAdapter;
import pictest.connection.FbConnManager;
import pictest.objects.FbAlbum;
import pictest.util.SharedPreferencesManager;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.pictest.R;

public class GaleryActivity extends SherlockActivity {
	private FancyCoverFlow FCFAlbumsView;
	private ArrayList<FbAlbum> albums;
	private Activity activity;
	private FbConnManager connManager;
	private PhotoElementAdapter adapter;
	private static boolean canRun;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_fb);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		FCFAlbumsView = (FancyCoverFlow) findViewById(R.id.FCFAlbumsView);
		activity = this;
	}

	private class GetAlbumsCovers extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			for(int i = 0; i < albums.size(); i ++){
				FbAlbum album = albums.get(i);
				connManager.getCoverImage(album);
				if (album.getFbPhotoCover().getImage() == null) {
					Log.e("removio", "cover id: " + album.getCover_photo());
				}
				Log.d("loading", album.getName() + "  all right : " + i
						+ " - " + album.getFbPhotoCover().getImage());
				publishProgress();
				if (!canRun)
					break;
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			synchronized (adapter) {
				adapter.notifyDataSetChanged();
			}
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
		}

	}

	private class GetAlbums extends AsyncTask<Void, Integer, Void> {
		SharedPreferencesManager prefs;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(activity);
			dialog.setMessage("Loading albums...");
			dialog.setCancelable(false);
			dialog.show();
			prefs = new SharedPreferencesManager(activity);
			String accessToken = prefs.getFbAccessToken();
			Log.d("debug", "accessToken: " + accessToken);
			connManager = new FbConnManager(accessToken);
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.d("other", "");
			albums = connManager.getAlbumsNoImage();
			Log.d("other", "");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			adapter = new PhotoElementAdapter(activity, albums);
			FCFAlbumsView.setAdapter(adapter);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
		albums.clear();
		adapter.notifyDataSetChanged();
		System.gc();
		canRun = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		canRun = true;
		new GetAlbums().execute();
		new GetAlbumsCovers().execute();
	}

	public static void setCanRun(boolean e) {
		canRun = e;
	}
}
