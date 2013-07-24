package pictest.connection;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pictest.objects.FbAlbum;
import pictest.objects.FbPerson;
import pictest.objects.FbPhoto;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class FbConnManager {
	public static Drawable image;
	private final String LINK_INFO_USER = "https://graph.facebook.com/me?access_token=";
	private final String LINK_PICTURE_USER = "https://graph.facebook.com/me/picture?type=large&access_token=";
	private final String LINK_ALBUMS_USER = "https://graph.facebook.com/me/albums?access_token=";
	private final String LINK_PHOTO = "https://graph.facebook.com/";
	private final String accessToken;

	public FbConnManager(String accessToken) {
		this.accessToken = accessToken;
	}

	// for user information
	public FbPerson getUser() {
		FbPerson person = new FbPerson();
		try {
			person.setJSONObject(HttpHelper.readJsonFromUrl(LINK_INFO_USER
					+ accessToken));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// person.setImage(HttpHelper.LoadImageFromWebOperations(LINK_PICTURE_USER
		// + accessToken));
		person.setImage(image);
		return person;
	}

	public ArrayList<FbAlbum> getAlbumsNoImage() {
		ArrayList<FbAlbum> list = new ArrayList<FbAlbum>();
		try {
			JSONObject data = HttpHelper.readJsonFromUrl(LINK_ALBUMS_USER
					+ accessToken);
			JSONArray albums = data.getJSONArray("data");
			for (int i = 0; i < albums.length(); i++) {
				FbAlbum album = new FbAlbum();
				album.setJSONObject(albums.getJSONObject(i));
				list.add(album);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public FbAlbum getCoverImage(FbAlbum album) {
		FbPhoto photo = getFbPhotoNoImage(album.getCover_photo());
		getFbPhotoWithImage(photo);
		album.setFbPhotoCover(photo);
		if(photo.getImage() == null)
			Log.e("ERR", "FbConnManager, image null. URL: " + photo.getSource());
		return album;
	}

	public FbPhoto getFbPhotoNoImage(long id) {
		FbPhoto ret = new FbPhoto();
		try {
			ret.setJSONObject(HttpHelper.readJsonFromUrl(LINK_PHOTO + id
					+ "?access_token=" + accessToken));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public FbPhoto getFbPhotoWithImage(FbPhoto photo) {
		photo.setImage(HttpHelper.LoadImageFromWebOperations(photo.getSource()));
		return photo;
	}
}
