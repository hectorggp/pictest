package com.pictest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setWidgets();
		setFunctionality();
	}

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
		
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
		for (Account account : accounts) {
		    if (emailPattern.matcher(account.name).matches()) {
		        String possibleEmail = account.name;
		        Log.d("email:", possibleEmail + " - type: " + account.type);
		    } else
		    	Log.d("not email:", account.name + " - type: " + account.type);
		}

	}

	protected void setFunctionality() {
		findViewById(R.id.BTNGalery).setOnClickListener(this);
		findViewById(R.id.BTNPayPal).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.BTNGalery:
			startActivity(new Intent(this, FacebookAuthActivity.class));
			break;
		case R.id.BTNPayPal:
			startActivity(new Intent(this, PlusAuthActivity.class));
		}
	}

}
