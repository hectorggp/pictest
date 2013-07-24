package pictest.objects;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.pictest.R;
import com.pictest.util.FacebookBridgeServerUtil;

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
	private Drawable cover;

	private Activity context;

	private FacebookBridgeServerUtil connection;

	public FbAlbum(JSONObject jobject,
			FacebookBridgeServerUtil connection, Activity context) {
		this.connection = connection;
		this.context = context;
		if (!setJSONObject(jobject))
			Log.e("album", jobject.toString());
	}

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
		getCoverDrawable(cover_photo);
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

	public Drawable getCover() {
		return cover;
	}

	public void setCover(Drawable cover) {
		this.cover = cover;
	}

	public boolean setJSONObject(JSONObject jobject) {
		boolean ret = true;
		if (jobject == null)
			return false;
		try {
			this.setCan_upload(jobject.getBoolean(TAG_CAN_UPLOAD));
		} catch (JSONException e) {
			ret = false;
		}
		try {
			this.setCount(jobject.getInt(TAG_COUNT));
		} catch (JSONException e) {
			ret = false;
		}
		try {
			this.setCover_photo(jobject.getLong(TAG_COVER_PHOTO));
		} catch (JSONException e) {
			ret = false;
		}
		try {
			this.setId(jobject.getLong(TAG_ID));
		} catch (JSONException e) {
			ret = false;
		}
		try {
			this.setLink(jobject.getString(TAG_LINK));
		} catch (JSONException e) {
			ret = false;
		}
		try {
			this.setName(jobject.getString(TAG_NAME));
		} catch (JSONException e) {
			ret = false;
		}
		try {
			this.setPrivacy(jobject.getString(TAG_PRIVACY));
		} catch (JSONException e) {
			ret = false;
		}

		return ret;
	}

	private void getCoverDrawable(long id) {
		// setCover(new FacebookPhoto(connection.getImage(id)).getImage());
		setCover(context.getResources().getDrawable(R.drawable.ic_launcher));
		Toast.makeText(context, "alaputa", Toast.LENGTH_LONG).show();
	}

}
