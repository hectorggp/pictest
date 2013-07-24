package pictest.objects;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

public class FbPhoto {
	public static final String TAG_ID = "id";
	public static final String TAG_SOURCE = "source";
	public static final String TAG_HEIGHT = "height";
	public static final String TAG_WIDTH = "width";

	private long id;
	private String source;
	private int height;
	private int width;
	private Drawable image;

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

	public boolean setJSONObject(JSONObject jobject){
		boolean ret = true;
		try {
			this.setHeight(jobject.getInt(TAG_HEIGHT));
		} catch (JSONException e) { ret = false; }
		try {
			this.setId(jobject.getLong(TAG_ID));
		} catch (JSONException e) { ret = false; }
		try {
			this.setSource(jobject.getString(TAG_SOURCE));
		} catch (JSONException e) { ret = false; }
		try {
			this.setWidth(jobject.getInt(TAG_WIDTH));
		} catch (JSONException e) { ret = false; }
		return ret;
	}

	public void setImage(Drawable image){
		this.image = image;
	}
	
	public Drawable getImage(){
		return image;
	}
}
