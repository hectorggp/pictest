package com.pictest;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.pictest.util.FacebookBridgeServerUtil;

public class FacebookAuthActivity extends Activity implements StatusCallback {

	private static final int REAUTH_ACTIVITY_CODE = 100;

	private ImageView iv;
	private String address;
	private ProgressDialog Dialog;
	private Activity context;
	private Session mSession;
	private FacebookBridgeServerUtil connection;
	private List<String> PERMISSIONS;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth_fb);

		context = this;
		connection = new FacebookBridgeServerUtil(context);
		PERMISSIONS = Arrays.asList(getResources().getStringArray(
				R.array.facebook_permissions));

		//iv = (ImageView) findViewById(R.id.IMVPic);
		// start Facebook Login
		if(Session.getActiveSession() == null || Session.getActiveSession().isClosed()){
			Log.d("session", "is active");
		}
		Dialog = new ProgressDialog(this);
		Dialog.setMessage("Please wait...");
		Dialog.setCancelable(false);
		Dialog.show();
		if(isActiveSession()) {
			SavePreferences();
			AccessPermissions();
		} else
		Session.openActiveSession(this, true, new StatusCallback() {

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
//										((TextView) findViewById(R.id.TXTHello))
//												.setText("Welcome "
//														+ user.getName() + "!");
										// get address of profile picture
										address = "https://graph.facebook.com/"
												+ user.getUsername()
												+ "/picture?type=large";
										new GetPictureProfileRequest()
												.execute();

										SavePreferences();
										AccessPermissions();
									} else {
										// can not get for user's info
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
	
	private boolean isActiveSession(){
		return Session.getActiveSession() == null || Session.getActiveSession().isOpened();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
		if(requestCode == REAUTH_ACTIVITY_CODE)
			Log.d("requestcode", requestCode + "");
	}

	private void SavePreferences() {
		mSession = Session.getActiveSession();
		if (mSession != null) {
			String access_token = mSession.getAccessToken();
			connection.saveAccessToken(access_token);
			Log.d("access_token", access_token);
		}
	}

	private void AccessPermissions() {
		if (mSession != null) {
			Session.NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(
					this, PERMISSIONS).setRequestCode(REAUTH_ACTIVITY_CODE);
			mSession.requestNewPublishPermissions(reauthRequest);
		}
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
			Intent albumsIntent = new Intent(context,
					FacebookAlbumsActivity.class);
			startActivity(albumsIntent);
		}

	}

	@Override
	public void call(Session session, SessionState state, Exception exception) {
		if(session.isOpened()){
			Log.d("session", "is opened!!");
			List<String> prs = session.getPermissions();
			for(String pr : prs)
				Log.d("per", pr);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
}
