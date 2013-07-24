package pictest.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesManager {
	
	private static final String PREF_NAME = "pictest_prefs";
	private static final String FB_ACCESS_TOKEN = "fb_access_token";

	private String mFbAccessToken;
	private final SharedPreferences prefs;
	private final Editor editor;
	
	/*
	 * Constructor
	 * Initializes the object
	 */
	public SharedPreferencesManager(Activity context){
		prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		editor = prefs.edit();
	}
	
	/**
	 * puts the facebook access token 
	 * @param accessToken the facebook access token
	 */
	public void setFbAccessToken(String accessToken){
		mFbAccessToken = accessToken;
		editor.putString(FB_ACCESS_TOKEN, accessToken);
		editor.commit();
	}
	
	/**
	 * checks if the session is initiated
	 * @return if this started, the token is not null
	 */
	public boolean loggedIn(){
		return getFbAccessToken() != null;
	}
	
	/**
	 * gets the facebook token
	 * @return the facebook token
	 */
	public String getFbAccessToken(){
		if(mFbAccessToken == null){
			if(prefs != null)
				mFbAccessToken = prefs.getString(FB_ACCESS_TOKEN, null);
			return mFbAccessToken;
		} else 
			return mFbAccessToken;
	}
}
