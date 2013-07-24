package com.pictest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class FacebookBridgeServerUtil {
	
	public static final String URL_ALBUMS = "https://graph.facebook.com/me/albums";
	public static final String URL = "https://graph.facebook.com/";
	public static final String data = "data";
	
	private static final String access_token = "access_token";
	// prefs name
	private static final String PREF_NAME = "pictest_prefs";
	// access token name
	private static final String PREF_ACCESS_TOKEN = "facebook_access_token";
	private final SharedPreferences prefs;
	private static String mAccessToken;

	public FacebookBridgeServerUtil(Context context) {
		prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		if(prefs != null)
			mAccessToken = prefs.getString(PREF_ACCESS_TOKEN, "");
	}
	
	public boolean loggedIn(){
		return getAccessToken() != null;
	}
	
	public String getAccessToken(){
		if(mAccessToken == null){
			if(prefs != null)
				mAccessToken = prefs.getString(PREF_ACCESS_TOKEN, "");
			return mAccessToken;
		} else 
			return mAccessToken;
	}
	
	public void saveAccessToken(String token){
		Editor mEditor = prefs.edit();
		mEditor.putString(PREF_ACCESS_TOKEN, token);
		mEditor.commit();
	}

	public JSONObject getFacebookAlbums() throws NullPointerException{
		if(loggedIn())
			return readJson(URL_ALBUMS + "?" + access_token + "=" + mAccessToken);
		else{
			Log.d("ERR", "no loggedIn get albums");
			throw new NullPointerException();
		}
	}
	
	public JSONObject getImage(long id) throws NullPointerException{
		if(loggedIn())
			return readJson(URL + id + "?" + access_token + "=" + mAccessToken);
		else {
			Log.e("ERR", "no loggedIn getimage");
			throw new NullPointerException();
		}
	}
	
	public Drawable getImage(String address) throws IOException{
		URL url = new URL(address);
		InputStream content = (InputStream) url.getContent();
		return Drawable.createFromStream(content, "src");
	}

	// static methods
	
	private static JSONObject readJsonFromUrl(String url) {
		try {
			InputStream is = new URL(url).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static JSONObject readJson(String token) {
		return readJsonFromUrl(token);
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
}
