package com.pictest.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class FacebookPhoto {

	public static final String TAG_ID = "id";
	public static final String TAG_SOURCE = "source";
	public static final String TAG_HEIGHT = "height";
	public static final String TAG_WIDTH = "width";

	private long id;
	private String source;
	private int height;
	private int width;
	private Drawable image;
	
	public FacebookPhoto(JSONObject jobject){
		if(!setJSONObject(jobject))
			Log.e("album", jobject.toString());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Drawable getImage() {
		return image;
	}

	public void setImage(Drawable image) {
		this.image = image;
	}
	
	private boolean setJSONObject(JSONObject jobject){
		boolean ret = true;
		try {
			this.setHeight(jobject.getInt(TAG_HEIGHT));
		} catch (JSONException e) { ret = false; }
		try {
			this.setId(jobject.getLong(TAG_ID));
		} catch (JSONException e) { ret = false; }
		try {
			this.setSource(jobject.getString(TAG_SOURCE));
			getDrawable(getSource());
		} catch (JSONException e) { ret = false; }
		try {
			this.setWidth(jobject.getInt(TAG_WIDTH));
		} catch (JSONException e) { ret = false; }
		return ret;
	}

	private void getDrawable(String address){
		try {
			URL url = new URL(address);
			InputStream content;
			content = (InputStream) url.getContent();
			setImage(Drawable.createFromStream(content, "src"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
