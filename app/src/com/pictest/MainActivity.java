package com.pictest;

import com.auth.plus.PlusAuthActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
			startActivity(new Intent(this, PlusAuthActivity.class));
			break;
		case R.id.BTNPayPal:
			Toast.makeText(getApplicationContext(), "PayPal", Toast.LENGTH_LONG).show();
		}
	}

}
