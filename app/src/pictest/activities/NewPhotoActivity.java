package pictest.activities;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.holoeverywhere.widget.Toast;

import pictest.objects.FbAlbum;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.pictest.R;

public class NewPhotoActivity extends SherlockActivity implements
		OnClickListener, StatusCallback {

	public static FbAlbum album;
	private ImageView imageView;
	private Bitmap bitmap = null;
	private Session facebook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_photo);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		((ImageButton) findViewById(R.id.IBTLoad)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.IBTOtherPhoto))
				.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.IMVContent);
		other();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.IBTLoad:
			load();
			break;
		case R.id.IBTOtherPhoto:
			other();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void load() {
		if (bitmap != null && album.isCan_upload()) {
			PostImage();
		} else
			Toast.makeText(getApplicationContext(), "Can not ",
					Toast.LENGTH_LONG).show();
	}

	private static int RESULT_LOAD_IMAGE = 0x223;

	private void other() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			bitmap = BitmapFactory.decodeFile(picturePath);
			imageView.setImageBitmap(bitmap);
		}
		if (Session.getActiveSession() != null)
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
	}

	private void PostImage() {

		if (facebook == null || facebook.isClosed()) {
			OpenRequest open = new OpenRequest(this);
			open.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
			open.setPermissions(Arrays.asList(getResources().getStringArray(
					R.array.facebook_publish_permissions)));
			open.setCallback(this);
			facebook = new Session(this);
			facebook.openForPublish(open);
		} else {
			Log.i("session", " " + facebook);
			ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageBytes);

			String accessToken = facebook.getAccessToken();
			Bundle params = new Bundle();
			params.putByteArray("source", imageBytes.toByteArray());
			params.putString("message", "A wall picture");
			params.putString("access_token", accessToken);
			// Request face = new Request();
			// face.setParameters(params);
			// face.setHttpMethod(HttpMethod.POST);
			// face.setSession(facebook);
			// face.setGraphPath();
			// face.executeAsync();
			Request.setDefaultBatchApplicationId("137883406418849");
			RequestAsyncTask a = Request.executeRestRequestAsync(facebook,
					"https://graph.facebook.com/" + album.getId()
							+ "/photos?access_token=" + accessToken, params,
					HttpMethod.POST);
			Log.e("Facebook", a + "");
		}
	}

	@Override
	public void call(Session facebook, SessionState state, Exception exception) {

	}
}
