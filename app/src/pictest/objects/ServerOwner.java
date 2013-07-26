package pictest.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerOwner {

	private static final String TAG_OWNER_ID = "owner_id";
	private static final String TAG_FB_OWNER_ID = "fb_owner_id";
	private static final String TAG_PP_OWNER_ID = "pp_owner_id";

	private long owner_id;
	private long fb_owner_id;
	private String pp_owner_id;

	public long getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(long owner_id) {
		this.owner_id = owner_id;
	}

	public long getFb_owner_id() {
		return fb_owner_id;
	}

	public void setFb_owner_id(long fb_owner_id) {
		this.fb_owner_id = fb_owner_id;
	}

	public String getPp_owner_id() {
		return pp_owner_id;
	}

	public void setPp_owner_id(String pp_owner_id) {
		this.pp_owner_id = pp_owner_id;
	}

	public JSONObject getJSONObject() {
		JSONObject jobject = new JSONObject();
		try {
			jobject.put(TAG_FB_OWNER_ID, getFb_owner_id());
		} catch (JSONException e) { }
		try {
			jobject.put(TAG_OWNER_ID, getOwner_id());
		} catch (JSONException e) { }
		try {
			jobject.put(TAG_PP_OWNER_ID, getPp_owner_id());
		} catch (JSONException e) { }
		return jobject;
	}
	
	public boolean setJSONObject(JSONObject jobject){
		boolean ret = true;
		try {
			setFb_owner_id(jobject.getLong(TAG_FB_OWNER_ID));
		} catch (JSONException e) { ret = false; }
		try {
			setOwner_id(jobject.getLong(TAG_OWNER_ID));
		} catch (JSONException e) { ret = false; }
		try {
			setPp_owner_id(jobject.getString(TAG_PP_OWNER_ID));
		} catch (JSONException e) { ret = false; }
		return ret;
	}
}
