package pictest.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class FbAlbum {
	
	public static final String TAG_ID = "id";
	public static final String TAG_NAME = "name";
	public static final String TAG_LINK = "link";
	public static final String TAG_COVER_PHOTO = "cover_photo";
	public static final String TAG_PRIVACY = "privacy";
	public static final String TAG_COUNT = "count";
	public static final String TAG_CAN_UPLOAD = "can_upload";
	
	private long id;
	private String name;
	private String link;
	private long cover_photo;
	private String privacy;
	private int count;
	private boolean can_upload;
	
	private FbPhoto fbPhotoCover;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public long getCover_photo() {
		return cover_photo;
	}

	public void setCover_photo(long cover_photo) {
		this.cover_photo = cover_photo;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isCan_upload() {
		return can_upload;
	}

	public void setCan_upload(boolean can_upload) {
		this.can_upload = can_upload;
	}
	
	public boolean setJSONObject(JSONObject jobject){
		boolean ret = true;
		try {
			setCan_upload(jobject.getBoolean(TAG_CAN_UPLOAD));
		} catch (JSONException e) { ret = false; }
		try {
			setCount(jobject.getInt(TAG_COUNT));
		} catch (JSONException e) { ret = false; }
		try {
			setCover_photo(jobject.getLong(TAG_COVER_PHOTO));
		} catch (JSONException e) { ret = false; }
		try {
			setId(jobject.getLong(TAG_ID));
		} catch (JSONException e) { ret = false; }
		try {
			setLink(jobject.getString(TAG_LINK));
		} catch (JSONException e) { ret = false; }
		try {
			setName(jobject.getString(TAG_NAME));
		} catch (JSONException e) { ret = false; }
		try {
			setPrivacy(jobject.getString(TAG_PRIVACY));
		} catch (JSONException e) { ret = false; }
		return ret;
	}	
	
	public void setFbPhotoCover(FbPhoto fbPhotoCover){
		this.fbPhotoCover = fbPhotoCover;
	}
	
	public FbPhoto getFbPhotoCover(){
		return fbPhotoCover;
	}
}
