package com.pictest;

import com.pictest.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class FacebookAlbumsActivity extends Activity {

	private String accessToken;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_fb);
		
		// get accessToken
		accessToken = getIntent().getExtras().getString("AccessToken");
	}

}
