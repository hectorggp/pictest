package pictest.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerPhoto {
	
	private static final String TAG_PHOTO_ID = "photo_id";
	private static final String TAG_FB_PHOTO_ID = "fb_photo_id";
	private static final String TAG_OWNER_ID = "owner_id";
	private static final String TAG_PRICE = "price";

	private long photo_id;
	private long fb_photo_id;
	private long owner_id;
	private double price;

	public long getPhoto_id() {
		return photo_id;
	}

	public void setPhoto_id(long photo_id) {
		this.photo_id = photo_id;
	}

	public long getFb_photo_id() {
		return fb_photo_id;
	}

	public void setFb_photo_id(long fb_photo_id) {
		this.fb_photo_id = fb_photo_id;
	}

	public long getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(long owner_id) {
		this.owner_id = owner_id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean setJSONObject(JSONObject jobject) {
		boolean ret = true;
		try {
			setFb_photo_id(jobject.getLong(TAG_FB_PHOTO_ID));
		} catch (JSONException e) { ret = false; }
		try {
			setOwner_id(jobject.getLong(TAG_OWNER_ID));
		} catch (JSONException e) { ret = false; }
		try {
			setPhoto_id(jobject.getLong(TAG_PHOTO_ID));
		} catch (JSONException e) { ret = false; }
		try {
			setPrice(jobject.getDouble(TAG_PRICE));
		} catch (JSONException e) { ret = false; }
		return ret;
	}
	
	public JSONObject getJSONObject() {
		JSONObject ret = new JSONObject();
		try {
			ret.put(TAG_FB_PHOTO_ID, getFb_photo_id());
		} catch (JSONException e) { }
		try {
			ret.put(TAG_OWNER_ID, getOwner_id());
		} catch (JSONException e) { }
		try {
			ret.put(TAG_PHOTO_ID, getPhoto_id());
		} catch (JSONException e) { }
		try {
			ret.put(TAG_PRICE, getPrice());
		} catch (JSONException e) { }
		return ret;
	}
}
