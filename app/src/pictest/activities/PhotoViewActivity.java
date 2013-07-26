package pictest.activities;

import java.util.ArrayList;

import org.holoeverywhere.app.ProgressDialog;
import org.holoeverywhere.widget.Toast;

import pictest.adapters.PhotoPagerAdapter;
import pictest.connection.FbConnManager;
import pictest.objects.FbAlbum;
import pictest.objects.FbPhoto;
import pictest.util.HackyViewPager;
import pictest.util.SharedPreferencesManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.pictest.R;

public class PhotoViewActivity extends SherlockActivity {

	public static FbAlbum currentAlbum;
	private static boolean exit;

	private ViewPager mViewPager;
	private ArrayList<FbPhoto> list = new ArrayList<FbPhoto>();
	private PhotoPagerAdapter adapter;
	private ProgressDialog dialog;
	private FbConnManager conn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new HackyViewPager(this);
		setContentView(mViewPager);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	private class getPhotos extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(PhotoViewActivity.this);
			dialog.setCancelable(false);
			dialog.show();
			dialog.setMessage("loading photos...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			conn.getForRestImagesNoImage(list, currentAlbum);
			publishProgress();
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			synchronized (adapter) {
				adapter.notifyDataSetChanged();
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
		}
	}

	private class getPhotosImages extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			int i = 0;
			for (FbPhoto photo : list) {
				conn.getFbPhotoWithImage(photo);
				if (exit) {
					Log.d("debug", "exit  PhotoView");
					break;
				}
				Log.d("loading", photo.getSource() + "  all right : " + ++i
						+ " - " + photo.getImage());
				publishProgress();
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
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

	@Override
	protected void onResume() {
		super.onResume();
		exit = false;
		list.add(currentAlbum.getFbPhotoCover()); ///////// NullPointerException
		adapter = new PhotoPagerAdapter(list);
		mViewPager.setAdapter(adapter);

		SharedPreferencesManager prefs = new SharedPreferencesManager(this);
		conn = new FbConnManager(prefs.getFbAccessToken());
		new getPhotos().execute();
		new getPhotosImages().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		exit = true;
		list.clear();
		adapter.notifyDataSetChanged();
		System.gc();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_publish_image, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int i = mViewPager.getCurrentItem();
		switch (item.getItemId()) {
		case R.id.itemSetPrice:
			Log.d("setPrice ", "id: " + list.get(i).getId() + " - link: "
					+ list.get(i).getLink() + "");
			publishFeedDialog(NAME, CAPTION_SET_PRICE, DESC_SET_PRICE, list
					.get(i).getLink(), PICTURE);
			return true;
		case R.id.itemShare:
			Log.d("setPrice ", "id: " + list.get(i).getId() + " - link: "
					+ list.get(i).getLink() + "");
			publishFeedDialog(NAME, CAPTION_SHARE, DESC_SHARE, list.get(i)
					.getLink(), PICTURE);
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static void setExit(boolean e) {
		exit = e;
	}

	private final static String NAME = "PicTest App";
	private final static String CAPTION_SET_PRICE = "Price of my photo!";
	private final static String CAPTION_SHARE = "See my photo";
	private final static String DESC_SET_PRICE = "Recently I put a price on my photo, if you want to acquire";
	private final static String DESC_SHARE = "I just uploaded my photo in order to receive a donation";
	private final static String PICTURE = "http://farm3.staticflickr.com/2541/4057660716_18468d8905.jpg";

	private void publishFeedDialog(String name, String caption,
			String description, String link, String picture) {
		Bundle params = new Bundle();
		params.putString("name", name);
		params.putString("caption", caption);
		params.putString("description", description);
		params.putString("link", link);
		params.putString("picture", picture);
		Log.d("publish",
				String.format(
						"name: %s, caption: %s, description: %s, link: %s, picture: %s",
						name, caption, description, link, picture));

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(getApplicationContext(),
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(
										getApplicationContext()
												.getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(
									getApplicationContext()
											.getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							Toast.makeText(
									getApplicationContext()
											.getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
	}
}
