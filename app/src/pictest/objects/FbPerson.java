package pictest.objects;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

public class FbPerson {

	public static final String TAG_USER_ID = "id";
	public static final String TAG_USER_NAME = "name";
	public static final String TAG_USER_FIRST_NAME = "first_name";
	public static final String TAG_USER_LAST_NAME = "last_name";
	public static final String TAG_USER_LINK = "link";
	public static final String TAG_USER_USERNAME = "username";
	public static final String TAG_USER_GENDER = "gender";
	public static final String TAG_USER_TIMEZONE = "timezone";
	public static final String TAG_USER_LOCALE = "locale";
	public static final String TAG_USER_VERIFIED = "verified";
	public static final String TAG_USER_UPDATED_TIME = "updated_time";

	private long id;
	private String name;
	private String first_name;
	private String last_name;
	private String link;
	private String username;
	private String gender;
	private String timezone;
	private String locale;
	private String verified;
	private String updated_time;

	private Drawable image; 
	
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

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getUpdated_time() {
		return updated_time;
	}

	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}

	public boolean setJSONObject(JSONObject jobject){
		boolean res = true;
		try {
			setFirst_name(jobject.getString(TAG_USER_FIRST_NAME));
		} catch (JSONException e){ res = false; }
		try {
			setGender(jobject.getString(TAG_USER_GENDER));
		} catch (JSONException e){ res = false; }
		try {
			setId(jobject.getLong(TAG_USER_ID));
		} catch (JSONException e){ res = false; }
		try {
			setLast_name(jobject.getString(TAG_USER_LAST_NAME));
		} catch (JSONException e){ res = false; }
		try {
			setLink(jobject.getString(TAG_USER_LINK));
		} catch (JSONException e){ res = false; }
		try {
			setLocale(jobject.getString(TAG_USER_LOCALE));
		} catch (JSONException e){ res = false; }
		try {
			setName(jobject.getString(TAG_USER_NAME));
		} catch (JSONException e){ res = false; }
		try {
			setTimezone(jobject.getString(TAG_USER_TIMEZONE));
		} catch (JSONException e){ res = false; }
		try {
			setUpdated_time(jobject.getString(TAG_USER_UPDATED_TIME));
		} catch (JSONException e){ res = false; }
		try {
			setUsername(jobject.getString(TAG_USER_USERNAME));
		} catch (JSONException e){ res = false; }
		try {
			setVerified(jobject.getString(TAG_USER_VERIFIED));
		} catch (JSONException e){ res = false; }
		return res;
	}
	
	public void setImage(Drawable image){
		this.image = image;
	}
	
	public Drawable getImage(){
		return image;
	}
}
