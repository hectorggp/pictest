package pictest.connection;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import pictest.objects.FbPerson;
import pictest.objects.ServerOwner;
import pictest.util.Encoder;
import android.util.Log;

public class ServerConnManager {
	
	private static final String LINK_GET_OWNER = "http://hectorggp.byethost18.com/owner.php";
	private static final String LINK_CREATE_OWNER = "http://hectorggp.byethost18.com/ownerNew.php";
	private static final String LINK_GET_OWNER_PHTOS = "http://hectorggp.byethost18.com/ownerPhotos.php";
	
	// create owner on server or only get information
	public ServerOwner getOwner(FbPerson fbOwner){
		ServerOwner svOwner = new ServerOwner();

		// attempting to create new owner on server
		svOwner.setFb_owner_id(fbOwner.getId());
		svOwner.setPicture_base64(Encoder.encodeToString(fbOwner.getImage()));
		JSONObject response = HttpHelper.sendData(LINK_CREATE_OWNER, svOwner.getJSONObject());
		Log.i("response getOwner", response.toString());
		
		// get for the owner's info in server 
		ServerOwner ret = null;
		try {
			JSONObject jowner = HttpHelper.readJsonFromUrl(LINK_GET_OWNER + "?owner=" + fbOwner.getId());
			Log.i("jowner:", jowner.toString());
			ret = new ServerOwner();
			ret.setJSONObject(jowner);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

}
