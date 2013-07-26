package pictest.activities;

import java.util.ArrayList;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.ProgressDialog;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.Toast;

import pictest.adapters.PhotoPagerAdapter;
import pictest.connection.FbConnManager;
import pictest.connection.ServerConnManager;
import pictest.objects.FbAlbum;
import pictest.objects.FbPhoto;
import pictest.objects.ServerOwner;
import pictest.objects.ServerPhoto;
import pictest.util.HackyViewPager;
import pictest.util.SharedPreferencesManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

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
	private SharedPreferencesManager prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new HackyViewPager(this);
		setContentView(mViewPager);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		prefs = new SharedPreferencesManager(this);
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
		list.add(currentAlbum.getFbPhotoCover()); // ///////
													// NullPointerException
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
			getPrice(i);
			return true;
		case R.id.itemShare:
			Log.d("setPrice ", "id: " + list.get(i).getId() + " - link: "
					+ list.get(i).getLink() + "");
			publishFeedDialog(NAME, CAPTION_SHARE, DESC_SHARE, list.get(i)
					.getLink(), PICTURE);
			return true;
		case R.id.itemLoadImage:
			if (currentAlbum.isCan_upload()) {
				NewPhotoActivity.album = currentAlbum;
				startActivity(new Intent(this, NewPhotoActivity.class));
			} else
				Toast.makeText(getApplicationContext(),
						"Can not upload to this album", Toast.LENGTH_LONG)
						.show();
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void getPrice(final int i) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("PicTest");
		alert.setMessage("Much would you charge? ");

		// Set an EditText view to get user input
		final EditText inputPrice = new EditText(this);
		inputPrice.setHint("Base price");
		final EditText inputEmail = new EditText(this);
		inputEmail.setHint("Paypal email");
		String pp_owner_id = prefs.getPpOwnerId();
		if (pp_owner_id != null && !pp_owner_id.equals("null"))
			inputEmail.setText(pp_owner_id);

		LinearLayout a = new LinearLayout(this);
		a.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		a.setLayoutParams(params);
		a.addView(inputPrice);
		a.addView(inputEmail);
		alert.setView(a);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				setPrice(inputPrice.getText().toString(), inputEmail.getText()
						.toString(), i);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
	}

	private void setPrice(String value, String email, int i) {
		Double val = isDouble(value);
		if (val != null) {
			// update info account
			if (!email.equals(prefs.getPpOwnerId())) {
				prefs.setPpOwnerId(email);
				ServerOwner owner = new ServerOwner();
				owner.setOwner_id(prefs.getOwnerId());
				owner.setPp_owner_id(email);
				new UpdateOwnerInfo().execute(owner);
			}

			// save photo price
			ServerPhoto photo = new ServerPhoto();
			photo.setFb_photo_id(list.get(i).getId());
			photo.setOwner_id(prefs.getOwnerId());
			photo.setPrice(val);
			new addOwnerPhoto().execute(photo);
			publishFeedDialog(NAME, CAPTION_SET_PRICE, DESC_SET_PRICE + val,
					list.get(i).getLink(), PICTURE);
		} else
			Toast.makeText(getApplicationContext(), "Invalid price",
					Toast.LENGTH_LONG).show();
	}

	private Double isDouble(String v) {
		try {
			Double ret = Double.valueOf(v);
			return ret;
		} catch (Exception e) {
			try {
				Integer ret = Integer.valueOf(v);
				return (double) ret;
			} catch (Exception ex) {
				return null;
			}
		}
	}

	public static void setExit(boolean e) {
		exit = e;
	}

	private final static String NAME = "PicTest App";
	private final static String CAPTION_SET_PRICE = "Price of my photo!";
	private final static String CAPTION_SHARE = "See my photo";
	private final static String DESC_SET_PRICE = "Recently I put a price on my photo, if you want to acquire for ";
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

	private class addOwnerPhoto extends AsyncTask<ServerPhoto, Void, Boolean> {
		ServerConnManager conn = new ServerConnManager();

		@Override
		protected Boolean doInBackground(ServerPhoto... params) {
			return conn.addPhoto(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Toast.makeText(getApplicationContext(), "Photo added!",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Error while adding photo", Toast.LENGTH_LONG).show();
			}
		}
	}

	private class UpdateOwnerInfo extends AsyncTask<ServerOwner, Void, Boolean> {
		ServerConnManager conn = new ServerConnManager();

		@Override
		protected Boolean doInBackground(ServerOwner... params) {
			return conn.updateAccount(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Toast.makeText(getApplicationContext(), "Account uploaded!",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Error with your account", Toast.LENGTH_LONG).show();
			}
		}

	}
}
