package pictest.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesManager {

	private static final String PREF_NAME = "pictest_prefs";
	private static final String FB_ACCESS_TOKEN = "fb_access_token";
	private static final String SV_OWNER_ID = "sv_owner_id";
	private static final String SV_PP_OWNER_ID = "sv_pp_owner_id";

	private String mFbAccessToken;
	private long owner_id = -1;
	private String pp_owner_id;
	private final SharedPreferences prefs;
	private final Editor editor;

	/*
	 * Constructor Initializes the object
	 */
	public SharedPreferencesManager(Activity context) {
		prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		editor = prefs.edit();
	}

	/**
	 * puts the facebook access token
	 * 
	 * @param accessToken
	 *            the facebook access token
	 */
	public void setFbAccessToken(String accessToken) {
		mFbAccessToken = accessToken;
		editor.putString(FB_ACCESS_TOKEN, accessToken);
		editor.commit();
	}

	/**
	 * checks if the session is initiated
	 * 
	 * @return if this started, the token is not null
	 */
	public boolean loggedIn() {
		return getFbAccessToken() != null;
	}

	/**
	 * gets the facebook token
	 * 
	 * @return the facebook token
	 */
	public String getFbAccessToken() {
		if (mFbAccessToken == null) {
			mFbAccessToken = prefs.getString(FB_ACCESS_TOKEN, null);
			return mFbAccessToken;
		} else
			return mFbAccessToken;
	}

	/**
	 * puts the owner id
	 * 
	 * @param id
	 *            the owner id on server
	 */
	public void setOwnerId(long id) {
		owner_id = id;
		editor.putLong(SV_OWNER_ID, id);
		editor.commit();
	}

	/**
	 * gets the server owner id
	 * 
	 * @return the owner id
	 */
	public long getOwnerId() {
		if (owner_id == -1) {
			owner_id = prefs.getLong(SV_OWNER_ID, -1);
			return owner_id;
		} else
			return owner_id;
	}

	/**
	 * puts the paypal owner id
	 * 
	 * @param pp_owner_id
	 *            the paypal owner id
	 */
	public void setPpOwnerId(String pp_owner_id) {
		this.pp_owner_id = pp_owner_id;
		if (pp_owner_id != null) {
			editor.putString(SV_PP_OWNER_ID, pp_owner_id);
			editor.commit();
		}
	}

	public String getPpOwnerId() {
		if (pp_owner_id == null) {
			pp_owner_id = prefs.getString(SV_PP_OWNER_ID, null);
			return pp_owner_id;
		} else
			return pp_owner_id;
	}
}
