package com.pictest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.auth.fb.FacebookAuthActivity;
import com.auth.plus.PlusAuthActivity;

public class MainActivity extends _CustomActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setWidgets();
		setFunctionality();
	}

	@Override
	protected void setWidgets() {
		try {
			Log.d("key", "1ra");
	        PackageInfo info = getPackageManager().getPackageInfo("com",
	                PackageManager.GET_SIGNATURES);
	        Log.d("key", "2da");
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("YOURHASH KEY:",
	                    Base64.encodeToString(md.digest(), Base64.DEFAULT));
	        }
	    } catch (NameNotFoundException e) {
	    	e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	    	e.printStackTrace();
	    }
	}

	@Override
	protected void setFunctionality() {
		findViewById(R.id.BTNGalery).setOnClickListener(this);
		findViewById(R.id.BTNPayPal).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.BTNGalery:
			startActivity(new Intent(this, FacebookAuthActivity.class));
			break;
		case R.id.BTNPayPal:
			startActivity(new Intent(this, PlusAuthActivity.class));
		}
	}

}
