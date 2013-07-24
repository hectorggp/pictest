package pictest.activities;

import java.util.Arrays;

import org.holoeverywhere.app.ProgressDialog;

import pictest.connection.FbConnManager;
import pictest.objects.FbPerson;
import pictest.util.SharedPreferencesManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.pictest.R;

public class FragmentFbSignInActivity extends Fragment implements
		OnClickListener {
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	private ImageView IMVUserPicture;
	private Button BTNGoToGalery;
	private String accessToken;
	private SharedPreferencesManager sharedManager;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		IMVUserPicture = (ImageView) getActivity().findViewById(
				R.id.IMVUserPicture);
		BTNGoToGalery = (Button) getActivity().findViewById(R.id.BTNGoToGalery);
		BTNGoToGalery.setOnClickListener(this);
		sharedManager = new SharedPreferencesManager(getActivity());
		if (Session.getActiveSession() != null
				&& Session.getActiveSession().isOpened()) {
			accessToken = sharedManager.getFbAccessToken();
			new GetUserPicture().execute();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_auth_fb, container,
				false);

		LoginButton authButton = (LoginButton) view
				.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList(getActivity()
				.getResources().getStringArray(R.array.facebook_permissions)));

		return view;
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
			accessToken = session.getAccessToken();
			sharedManager.setFbAccessToken(accessToken);
			BTNGoToGalery.setEnabled(true);
			new GetUserPicture().execute();
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
			BTNGoToGalery.setEnabled(false);
			IMVUserPicture.setBackground(getActivity().getResources()
					.getDrawable(R.drawable.ic_launcher));
			getActivity().setTitle(
					getActivity().getResources().getString(
							R.string.login_facebook_title));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private class GetUserPicture extends AsyncTask<Void, Void, Void> {
		FbConnManager fbConn;
		FbPerson user;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.show();
			fbConn = new FbConnManager(accessToken);
		}

		@Override
		protected Void doInBackground(Void... params) {
			user = fbConn.getUser();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			IMVUserPicture.setBackground(user.getImage() != null ? user
					.getImage() : getActivity().getResources().getDrawable(
					R.drawable.ic_launcher));
			Activity activity = getActivity();
			if (activity != null)
				activity.setTitle("Hello " + user.getName() + "!");
			dialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.BTNGoToGalery) {
			Log.d("Galery", "Go to galery");
			Activity activity = getActivity();
			if (activity != null)
				getActivity().startActivity(
						new Intent(getActivity(), GaleryActivity.class));
			else
				Log.e("ERR", "activity is null..???");
		}
	}
}
