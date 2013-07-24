package pictest.connection;

import java.io.IOException;

import org.json.JSONException;

import pictest.objects.FbPerson;

public class FbConnManager {
	private final String LINK_INFO_USER = "https://graph.facebook.com/me?access_token=";
	private final String LINK_PICTURE_USER = "https://graph.facebook.com/me/picture?type=large&access_token=";
	private final String LINK_ALBUMS_USER = "https://graph.facebook.com/me/albums?access_token=";
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
		person.setImage(HttpHelper.LoadImageFromWebOperations(LINK_PICTURE_USER
				+ accessToken));
		return person;
	}
}
