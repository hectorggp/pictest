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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_fb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

		FCFAlbumsView = (FancyCoverFlow) findViewById(R.id.FCFAlbumsView);
		activity = this;
		new GetAlbums().execute();
		new GetAlbumsCovers().execute();
	}

	private class GetAlbumsCovers extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			int i = 0;
			for (FbAlbum album : albums) {
				connManager.getCoverImage(album);
				Log.d("loading", album.getName() + " all right : " + ++i
						+ " - " + album.getFbPhotoCover().getImage());
				publishProgress();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			synchronized (adapter) {
				adapter.notifyDataSetChanged();
			}
		}

	}

	private class GetAlbums extends AsyncTask<Void, Integer, Void> {
		ProgressDialog dialog;
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
			albums = connManager.getAlbumsNoImage();
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

}
