package com.pictest;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.pictest.R;

public class FacebookAuthActivity extends Activity {
	private ImageView iv;
	private String address;
	private ProgressDialog Dialog;
	private Activity context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth_fb);

		context = this;
		iv = (ImageView) findViewById(R.id.IMVPic);
		// start Facebook Login
		Dialog = new ProgressDialog(this);
		Dialog.setMessage("Please wait...");
		Dialog.show();
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					// make request to the /me API
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {
										// get name and say hello
										((TextView) findViewById(R.id.TXTHello))
												.setText("Welcome "
														+ user.getName() + "!");
										// get address of profile picture
										address = "https://graph.facebook.com/"
												+ user.getUsername()
												+ "/picture?type=large";
										new GetPictureProfileRequest()
												.execute();
									} else {
										Dialog.dismiss();
										Toast.makeText(
												getApplicationContext(),
												getResources().getString(
														R.string.ERR_AUTH_FB),
												Toast.LENGTH_LONG).show();
									}
								}
							});
				} 
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	private class GetPictureProfileRequest extends
			AsyncTask<String, Void, Void> {
		private Drawable d;

		@Override
		protected Void doInBackground(String... arg0) {
			try {
				URL url = new URL(address);
				InputStream content = (InputStream) url.getContent();
				d = Drawable.createFromStream(content, "src");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			iv.setImageDrawable(d);
			Dialog.dismiss();
			Intent albumsIntent = new Intent(context, FacebookAlbumsActivity.class);
			albumsIntent.putExtra("AccessToken", Session.getActiveSession().getAccessToken());
			startActivity(albumsIntent);
		}

	}
}
